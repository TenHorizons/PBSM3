package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.data.getCarryover
import com.example.pbsm3.model.Category
import com.example.pbsm3.model.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "CategoryRepository"

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDataSource: DataSource<Category>
) : Repository<Category>, Carryover<Category> {

    //TODO confirm that all data source calls are surrounded with try catch.
    var categories: MutableList<Category> = mutableListOf()
        private set(value) {
            field = value
            for (listener in listeners) {
                listener(field)
            }
        }
    private var listeners: MutableList<(List<Category>) -> Unit> = mutableListOf()

    override suspend fun loadData(docRefs: List<String>, onError: (Exception) -> Unit) {
        if (docRefs.isEmpty()) {
            Log.d(TAG, "No categories to retrieve.")
            return
        }
        for (docRef in docRefs) {
            try {
                val category = categoryDataSource.get(docRef)
                categories.add(category)
            } catch (ex: Exception) {
                Log.d(TAG, "error at CategoryRepository::loadCategories")
                Log.d(TAG, "Exception: $ex")
                onError(ex)
            }
        }
        Log.d(TAG, "Categories loaded. Categories: $categories")
    }

    override suspend fun saveData() {
        TODO("Not yet implemented")
    }

    override suspend fun saveLocalData(item: Category) {
        categories.add(item)
        //TODO add a whole bunch of algorithms.
    }

    override suspend fun updateLocalData(item: Category) {
        val oldCat = getByRef(item.id)
        val oldCatIndex = categories.indexOf(oldCat)
        categories[oldCatIndex] = item
    }

    override suspend fun updateData(item: Category, onError: (Exception) -> Unit) {
        try {
            categoryDataSource.update(item)
        } catch (ex: Exception) {
            Log.d(TAG, "error at CategoryRepository::updateData")
            onError(ex)
        }
    }

    override suspend fun saveData(item: Category, onError: (Exception) -> Unit): String =
        try {
            categoryDataSource.save(item)
        } catch (ex: Exception) {
            Log.d(TAG, "error at CategoryRepository::saveData:String")
            onError(ex)
            ""
        }

    override fun getListByDate(date: LocalDate): List<Category> {
        return categories.filter { it.date.month == date.month && it.date.year == date.year }
    }

    override fun getByRef(ref: String): Category {
        return categories.first { it.id == ref }
    }

    override fun getListByRef(ref: String): List<Category> {
        //not supposed to return by ref. return entire list
        return categories
    }

    override fun getAll(): List<Category> {
        return categories
    }

    override fun onItemsChanged(callback: (List<Category>) -> Unit):()->Unit {
        listeners.add(callback)
        return {listeners.remove(callback)}
    }

    override suspend fun createAndSaveNewItem(item: Category) {
        TODO("Not yet implemented")
    }

    override suspend fun processModifiedItem(item: Category) {
        Log.i(TAG,"processModifiedItem called. Start.")
        val beforeModify = categories.first {
            it.date == item.date && it.name == item.name
        }
        if(beforeModify.totalCarryover != item.totalCarryover)
            throw IllegalStateException(
                "carryover was modified outside repository"
            )
        val sameNameItems = categories.filter { it.name == item.name }.toMutableList()
        sameNameItems.sortBy { it.date }
        val indexOfModifiedInFullList = categories.indexOf(beforeModify)
        val indexOfModifiedInSameNameItems = sameNameItems.indexOf(beforeModify)
        Log.i(TAG,
            "processModifiedItem before modify:\n" +
                    "${categories[indexOfModifiedInFullList]}\n" +
                    "item: $item"
        )
        val list = categories.toMutableList()
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
        categories = list
    }

    private fun Category.calculateCarryover(): Category {
        val lastMonth = this.getPreviousMonthCategory()
        val currentCarryover = this.totalCarryover
        val lastMonthCarryover = lastMonth.getCarryover()
        val updatedCarryover = currentCarryover.plus(lastMonthCarryover)
        return this.copy(totalCarryover = updatedCarryover)
    }

    private fun Category.getPreviousMonthCategory():Category{
        return categories.first {
            it.date == this.date.minusMonths(1) &&
                    it.name == this.name
        }
    }
}