package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.data.getCarryover
import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.Category
import com.example.pbsm3.model.Unassigned
import com.example.pbsm3.model.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

private const val TAG = "BudgetItemRepository"

@Singleton
class BudgetItemRepository @Inject constructor(
    private val budgetItemDataSource: DataSource<BudgetItem>,
    private val userRepo: Provider<ProvideUser>,
    private val catRepo: Provider<Repository<Category>>,
    private val unaRepo: Provider<Repository<Unassigned>>,
    private val unaCarryover: Provider<Carryover<Unassigned>>,
    private val catCarryover: Provider<Carryover<Category>>
):Repository<BudgetItem>,Carryover<BudgetItem> {
    private var budgetItems:List<BudgetItem> = listOf()
    private set(value) {
        field = value
        for(listener in listeners){
            listener(field)
        }
    }
    private var listeners:MutableList<(List<BudgetItem>)->Unit> = mutableListOf()

    private val userRepository:ProvideUser
    get() = userRepo.get()
    private val unassignedRepository:Repository<Unassigned>
        get() = unaRepo.get()
    private val unassignedCarryover:Carryover<Unassigned>
        get() = unaCarryover.get()
    private val categoryRepository:Repository<Category>
        get() = catRepo.get()
    private val categoryCarryover:Carryover<Category>
        get() = catCarryover.get()

    override suspend fun loadData(docRefs:List<String>, onError:(Exception)->Unit){
        if(docRefs.isEmpty()) {
            Log.d(TAG, "No budgetItems to retrieve.")
            return
        }
        for(docRef in docRefs){
            try{
                val item = budgetItemDataSource.get(docRef)

                budgetItems = budgetItems + item
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
        budgetItems = budgetItems + item
        //TODO add a whole bunch of algorithms
    }

    override suspend fun updateLocalData(item: BudgetItem) {
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

    override fun getListByRef(ref: String): List<BudgetItem> {
        return budgetItems.filter { it.categoryRef == ref }
    }

    override fun getAll(): List<BudgetItem> {
        return budgetItems
    }

    override fun onItemsChanged(callback: (List<BudgetItem>) -> Unit):()->Unit {
        listeners.add(callback)
        return {listeners.remove(callback)}
    }

    override suspend fun createAndSaveNewItem(item: BudgetItem) {
        if (budgetItems.any { it.date == item.date })
            throw IllegalStateException("Budget Item already exists!")
        val list = budgetItems.toMutableList()
        list.sortBy { it.date }
        val lastDate = list.last().date
        if (lastDate > item.date)
            throw IllegalStateException(
                "latest Unassigned has newer date!"
            )
        item.linkItems(lastDate)
    }

    override suspend fun processModifiedItem(item: BudgetItem) {
        Log.i(TAG,"processModifiedItem called. Start.")
        val beforeModify = getByRef(item.id)
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
        val list = budgetItems.toMutableList()
        list[indexOfModifiedInFullList] = item
        Log.i(TAG,"Item modified. updated:\n${list[indexOfModifiedInFullList]}")
        var indexToRecalculate = indexOfModifiedInSameNameItems+1
        while(indexToRecalculate < sameNameItems.size){
            val itemToRecalculate = sameNameItems[indexToRecalculate]
            val indexInFullList = list.indexOf((getByRef(itemToRecalculate.id)))
            Log.i(TAG,"recalculating. before recalculate: ${sameNameItems[indexToRecalculate]}")
            val recalculated = sameNameItems[indexToRecalculate]
                .calculateCarryover()
            list[indexInFullList] = recalculated
            Log.i(TAG,"after recalculate: ${list[indexInFullList]}")
            indexToRecalculate++
        }
        budgetItems = list

        updateCategory(beforeModify,item)
        updateUnassigned(beforeModify,item)
    }

    private suspend fun updateCategory(beforeModify:BudgetItem, modifiedItem:BudgetItem){
        val categoryList = categoryRepository.getListByDate(modifiedItem.date)
        var category = categoryList.first { it.budgetItemsRef.contains(modifiedItem.id) }
        val oldBudgeted = category.totalBudgeted
        val oldExpenses = category.totalExpenses
        val changeInBudgeted = (modifiedItem.totalBudgeted).minus(beforeModify.totalBudgeted)
        val changeInExpenses = (modifiedItem.totalExpenses).minus(beforeModify.totalExpenses)
        category = category.copy(
            totalBudgeted = oldBudgeted.plus(changeInBudgeted),
            totalExpenses = oldExpenses.plus(changeInExpenses)
        )
        categoryCarryover.processModifiedItem(category)
    }

    private suspend fun updateUnassigned(beforeModify:BudgetItem, modifiedItem:BudgetItem){
        val unassignedList = unassignedRepository.getListByDate(modifiedItem.date)
        var unassigned = unassignedList.first()
        val oldBudgeted = unassigned.totalBudgeted
        val changeInBudgeted = (modifiedItem.totalBudgeted).minus(beforeModify.totalBudgeted)
        unassigned = unassigned.copy(
            totalBudgeted = oldBudgeted.minus(changeInBudgeted)
        )
        unassignedCarryover.processModifiedItem(unassigned)
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