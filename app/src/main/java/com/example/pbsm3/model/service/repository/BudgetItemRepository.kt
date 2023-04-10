package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.data.getCarryover
import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

private const val TAG = "BudgetItemRepository"

@Singleton
class BudgetItemRepository @Inject constructor(
    private val budgetItemDataSource: DataSource<BudgetItem>,
    private val userRepo: Provider<ProvideUser>
):Repository<BudgetItem>,Carryover<BudgetItem> {
    var budgetItems:MutableList<BudgetItem> = mutableListOf()
    private val userRepository:ProvideUser
    get() = userRepo.get()

    override suspend fun loadData(docRefs:List<String>, onError:(Exception)->Unit){
        if(docRefs.isEmpty()) {
            Log.d(TAG, "No budgetItems to retrieve.")
            return
        }
        for(docRef in docRefs){
            try{
                val item = budgetItemDataSource.get(docRef)

                budgetItems.add(item)
            }
            catch (ex:Exception){
                Log.d(TAG, "error at BudgetItemRepository::loadBudgetItems")
                Log.d(TAG, "Exception: $ex")
                onError(ex)
            }
        }
        Log.d(TAG, "Budget Items loaded. Items: $budgetItems")
    }

    override suspend fun saveData() {
        TODO("Not yet implemented")
    }

    override suspend fun saveLocalData(item: BudgetItem) {
        budgetItems.add(item)
        //TODO add a whole bunch of algorithms
    }

    override suspend fun updateLocalData(item: BudgetItem) {
        val oldItem = getByRef(item.id)
        Log.i(TAG,"budget item update start.Before update:\n$oldItem\nupdated item:\n$item")
        val oldItemIndex = budgetItems.indexOf(oldItem)
        budgetItems[oldItemIndex] = item
        Log.i(TAG,"budget item added.\nindex: $oldItemIndex\nlist: \n$budgetItems")
        processModifiedItem(item)
    }

    override suspend fun updateData(item: BudgetItem, onError:(Exception)->Unit) =
        budgetItemDataSource.update(item)

    override suspend fun saveData(item: BudgetItem, onError: (Exception) -> Unit): String =
        try {
            budgetItemDataSource.save(item)
        }catch (ex:Exception){
            Log.d(TAG, "error at BudgetItemRepository::saveData:String")
            onError(ex)
            ""
        }

    override fun getListByDate(date: LocalDate): List<BudgetItem> {
        return budgetItems.filter { it.date.month == date.month && it.date.year == date.year }
    }

    override fun getByRef(ref: String): BudgetItem {
        return budgetItems.first { it.id == ref }
    }

    override suspend fun createAndSaveNewItem(item: BudgetItem) {
        if (budgetItems.any { it.date == item.date })
            throw IllegalStateException("Budget Item already exists!")
        budgetItems.sortBy { it.date }
        val lastDate = budgetItems.last().date
        if (lastDate > item.date)
            throw IllegalStateException(
                "latest Unassigned has newer date!"
            )
        item.linkItems(lastDate)
    }

    override suspend fun processModifiedItem(item: BudgetItem) {
        Log.i(TAG,"processModifiedItem called. Start.")
        val beforeModify = budgetItems.first {
            it.date == item.date && it.name == item.name
        }
        if(beforeModify.totalCarryover != item.totalCarryover)
            throw IllegalStateException(
                "carryover was modified outside repository"
            )
        val sameNameItems = budgetItems.filter { it.name == item.name }.toMutableList()
        sameNameItems.sortBy { it.date }
        val indexOfModifiedInFullList = budgetItems.indexOf(beforeModify)
        val indexOfModifiedInSameNameItems = sameNameItems.indexOf(beforeModify)
        Log.i(TAG,
            "processModifiedItem before modify:\n" +
                    "${budgetItems[indexOfModifiedInFullList]}\n" +
                    "item: $item"
        )
        budgetItems[indexOfModifiedInFullList] = item
        Log.i(TAG,"Item modified. updated:\n${budgetItems[indexOfModifiedInSameNameItems]}")
        var indexToRecalculate = indexOfModifiedInSameNameItems+1
        while(indexToRecalculate < sameNameItems.size){
            val itemToRecalculate = sameNameItems[indexToRecalculate]
            val indexInFullList = budgetItems.indexOf((getByRef(itemToRecalculate.id)))
            Log.i(TAG,"recalculating. before recalculate: ${sameNameItems[indexToRecalculate]}")
            val recalculated = sameNameItems[indexToRecalculate]
                .calculateCarryover()
            budgetItems[indexInFullList] = recalculated
            Log.i(TAG,"after recalculate: ${budgetItems[indexInFullList]}")
            indexToRecalculate++
        }
    }

    private suspend fun BudgetItem.linkItems(
        lastDate: LocalDate
    ): BudgetItem {
        Log.i(TAG,"linking items start.")
        var lastD = lastDate
        lastD = lastD.plusMonths(1)
        while (lastD < this.date) {
            Log.i(TAG,"create new budget item of date $lastD")
            BudgetItem(date = lastD)
                .calculateCarryover()
                .saveLocalAndUpdateWithRef()
            lastD.plusMonths(1)
        }
        Log.i(TAG,"linking items end.")
        return this.calculateCarryover().saveLocalAndUpdateWithRef()
    }

    private fun BudgetItem.calculateCarryover():BudgetItem{
        val lastMonth = this.getPreviousMonthBudgetItem()
        val currentCarryover = this.totalCarryover
        val lastMonthCarryover = lastMonth.getCarryover()
        val updatedCarryover = currentCarryover.plus(lastMonthCarryover)
        return this.copy(totalCarryover = updatedCarryover)
    }

    private suspend fun BudgetItem.saveLocalAndUpdateWithRef():BudgetItem{
        Log.i(TAG,"saving budget item start.")
        val unassignedRef = saveData(this, onError = {
            throw RuntimeException(
                "failed to save unassigned to firestore."
            )
        })
        Log.i(TAG,"saving budget item complete. adding ref to user")
        val updatedUnassigned = this.copy(id = unassignedRef)
        userRepository.addReference(updatedUnassigned)
        Log.i(TAG,"ref update complete.")
        return updatedUnassigned
    }

    private fun BudgetItem.getPreviousMonthBudgetItem():BudgetItem{
        return budgetItems.first {
            it.date == this.date.minusMonths(1) &&
                    it.name == this.name
        }
    }
}