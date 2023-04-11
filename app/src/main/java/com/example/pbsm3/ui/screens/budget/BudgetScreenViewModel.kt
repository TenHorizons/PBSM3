package com.example.pbsm3.ui.screens.budget

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.Category
import com.example.pbsm3.model.Unassigned
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.repository.Carryover
import com.example.pbsm3.model.service.repository.Repository
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject


private const val TAG = "BudgetScreenViewModel"

@HiltViewModel
class BudgetScreenViewModel @Inject constructor(
    private val categoryRepository: Repository<Category>,
    private val budgetItemRepository: Repository<BudgetItem>,
    private val unassignedRepository: Repository<Unassigned>,
    private val budgetItemCarryover: Carryover<BudgetItem>,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(BudgetScreenState())
        private set

    fun setSelectedDate(selectedDate: LocalDate) {
        uiState.value = uiState.value.copy(
            selectedDate = selectedDate
        )
        getRepositoryData(uiState.value.selectedDate)
    }

    private fun getRepositoryData(selectedDate: LocalDate) {
        val categoryList = categoryRepository.getListByDate(selectedDate)
        val itemList = budgetItemRepository.getListByDate(selectedDate)
        val availableList = unassignedRepository.getListByDate(selectedDate)

        Log.d(TAG, "Getting repository data in budget screen view model.")
        Log.d(TAG, "categoryList: $categoryList")
        Log.d(TAG, "itemList: $itemList")
        Log.d(TAG, "availableList: $availableList")

        if (availableList.size > 1) {
            Log.e(TAG, "Error: Available list size large than 1 in $TAG.")
        }

        mapItemsToCategory(categoryList, itemList)

        //add just in case. might remove later in favor of memory
        uiState.value = uiState.value.copy(
            displayedCategories = categoryList,
            displayedBudgetItems = itemList,
            unassigned = availableList.first()
        )
    }

    fun registerListeners(){
        val unregisterBIListener = budgetItemRepository.onItemsChanged { budgetItems ->
            Log.i(TAG,"BIListener: newItems: \n${budgetItems}")
            val oldMap = uiState.value.categoryItemMapping
            Log.i(TAG,"BIListener: oldMap: \n${oldMap}")
            mapItemsToCategory(
                categoryList = oldMap.keys.toList(),
                itemList = budgetItems.filter {
                    it.date.month == uiState.value.selectedDate.month &&
                            it.date.year == uiState.value.selectedDate.year
                }
            )
            Log.i(TAG,"BIListener: newMap: \n${uiState.value.categoryItemMapping}")
        }
        val unregisterCatListener = categoryRepository.onItemsChanged { categories ->
            Log.i(TAG,"catListener: newItems: \n${categories}")
            val oldMap = uiState.value.categoryItemMapping
            Log.i(TAG,"catListener: oldMap: \n${oldMap}")
            mapItemsToCategory(
                categoryList = categories.filter {
                    it.date.month == uiState.value.selectedDate.month &&
                            it.date.year == uiState.value.selectedDate.year
                },
                itemList = oldMap.values.flatten()
            )
            Log.i(TAG,"catListener: newMap: \n${uiState.value.categoryItemMapping}")
        }
        val unregisterUnaListener = unassignedRepository.onItemsChanged { unassignedList ->
            Log.i(TAG,"unaListener: newItems: \n${unassignedList}")
            val unassigned = unassignedList.first {
                it.date.month == uiState.value.selectedDate.month &&
                        it.date.year == uiState.value.selectedDate.year
            }
            Log.i(TAG,"unaListener: newItem: \n${unassigned}")
            uiState.value = uiState.value.copy(
                unassigned = unassigned
            )
            Log.i(TAG,"unaListener: updated?: \n${uiState.value.unassigned}")
        }
        this.addCloseable {
            unregisterBIListener()
            unregisterCatListener()
            unregisterUnaListener()
        }
    }

    private fun mapItemsToCategory(
        categoryList: List<Category>,
        itemList: List<BudgetItem>
    ) {
        val map = mutableMapOf<Category,List<BudgetItem>>()
        val sortedCategoryList = categoryList.sortedBy { it.position }
        for (category in sortedCategoryList) {
            val relatedItems = itemList.filter { it.categoryRef == category.id }
            relatedItems.sortedBy { it.position }
            map[category] = relatedItems
        }
        uiState.value = uiState.value.copy(categoryItemMapping = map)
    }

    fun getUnassignedValueToDisplay(): BigDecimal =
        uiState.value.unassigned.totalCarryover
            .plus(uiState.value.unassigned.totalBudgeted)
            //using plus, but value stored in expenses should be negative.
            .plus(uiState.value.unassigned.totalExpenses)

    //returning function for now to see new values in log.
    //TODO when typing, only change uistate. when hide keyboard/ focus changed, register change.
    fun updateBudgetItem(category: Category, item: BudgetItem) {
        Log.d(TAG, "updateBudgetItem starting.")
        Log.d(TAG, "updatedItem: $item,")
        Log.d(TAG, "old category:$category,")
        Log.d(TAG, "old unassigned: ${uiState.value.unassigned}")


        //processed in background, no loading indicator.
        viewModelScope.launch(Dispatchers.Default + NonCancellable) {
            budgetItemCarryover.processModifiedItem(item)
        }.invokeOnCompletion {
            Log.d(TAG, "updateBudgetItem completed.")
            Log.d(TAG, "updatedItem: " +
                    "${uiState.value.categoryItemMapping.values
                        .flatten()
                        .filter{ it.id == item.id }},")
            Log.d(TAG, "category:" +
                    "${uiState.value.categoryItemMapping.keys.toList()
                        .filter { it.id == category.id }},")
            Log.d(TAG, "available: ${uiState.value.unassigned}")
        }
    }

    fun updateBudgetItemDisplay(category: Category, item: BudgetItem){
        val items = uiState.value.categoryItemMapping[category]?.toMutableList()
        val itemIndex = items?.indexOf(items.first{it.id == item.id}) ?:
        throw IllegalStateException(
            "updateBudgetItemDisplay no items for category! category: $category"
        )
        items[itemIndex] = item
        val map = uiState.value.categoryItemMapping.toMutableMap()
        map[category] = items
        uiState.value = uiState.value.copy(categoryItemMapping = map)
    }
}