package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.model.NewBudgetItem
import com.example.pbsm3.model.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "BudgetItemRepository"

@Singleton
class BudgetItemRepository @Inject constructor(
    private val budgetItemDataSource: DataSource<NewBudgetItem>
):Repository<NewBudgetItem> {
    var budgetItems:MutableList<NewBudgetItem> = mutableListOf()

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

    override fun updateLocalData(item: NewBudgetItem) {
        val oldItem = getByRef(item.id)
        val oldItemIndex = budgetItems.indexOf(oldItem)
        budgetItems[oldItemIndex] = item
    }

    override suspend fun updateData(item: NewBudgetItem, onError:(Exception)->Unit) =
        budgetItemDataSource.update(item)

    override suspend fun saveData(item: NewBudgetItem, onError: (Exception) -> Unit): String =
        try {
            budgetItemDataSource.save(item)
        }catch (ex:Exception){
            Log.d(TAG, "error at BudgetItemRepository::saveData:String")
            onError(ex)
            ""
        }

    override fun getListByDate(date: LocalDate): List<NewBudgetItem> {
        return budgetItems.filter { it.date.month == date.month && it.date.year == date.year }
    }

    override fun getByRef(ref: String): NewBudgetItem {
        return budgetItems.first { it.id == ref }
    }
}