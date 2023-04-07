package com.example.pbsm3.ui.screens.budget

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.pbsm3.model.Available
import com.example.pbsm3.model.NewBudgetItem
import com.example.pbsm3.model.NewCategory
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.repository.Repository
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject


private const val TAG = "BudgetScreenViewModel"

@HiltViewModel
class BudgetScreenViewModel @Inject constructor(
    private val categoryRepository: Repository<NewCategory>,
    private val budgetItemRepository: Repository<NewBudgetItem>,
    private val availableRepository: Repository<Available>,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(BudgetScreenState())
        private set

    fun setSelectedDate(selectedDate: LocalDate) {
        uiState.value = uiState.value.copy(
            selectedDate = selectedDate
        )
    }
    fun getRepositoryData(selectedDate: LocalDate) {
        val categoryList = categoryRepository.getListByDate(selectedDate)
        val itemList = budgetItemRepository.getListByDate(selectedDate)
        val availableList = availableRepository.getListByDate(selectedDate)

        Log.d(TAG, "Getting repository data in budget screen view model.")
        Log.d(TAG, "categoryList: $categoryList")
        Log.d(TAG, "itemList: $itemList")
        Log.d(TAG, "availableList: $availableList")

        if(availableList.size > 1){
            Log.d(TAG, "Error: Available list size large than 1 in $TAG.")
        }

        for (category in categoryList) {
            val relatedItems = itemList.filter { it.categoryRef == category.id }
            uiState.value.categoryItemMapping[category] = relatedItems
        }

        //add just in case. might remove later in favor of memory
        uiState.value = uiState.value.copy(
            displayedCategories = categoryList,
            displayedBudgetItems = itemList,
            available = availableList.first()
        )
    }

    fun getAvailableValueToDisplay(): BigDecimal =
        uiState.value.available.totalCarryover
            .plus(uiState.value.available.totalBudgeted)
            //using plus, but value stored in expenses should be negative.
            .plus(uiState.value.available.totalExpenses)

    //returning function for now to see new values in log.
    fun updateBudgetItem(category: NewCategory, item: NewBudgetItem) {
        Log.d(TAG, "updateBudgetItem starting.")
        Log.d(TAG, "oldItem: $item,")
        Log.d(TAG, "category:$category,")
        Log.d(TAG, "available: ${uiState.value.available}")

        val oldBudgeted = budgetItemRepository.getByRef(item.id)
        //(new - old) to get change to be reflected in category and available.
        val changeInBudgeted = item.totalBudgeted.minus(oldBudgeted.totalBudgeted)
        budgetItemRepository.updateLocalData(item)

        val oldCatBudgeted = category.totalBudgeted
        //TODO do I need to check for negative here? most likely,
        // or just show red in available.
        val newCatBudgeted = oldCatBudgeted.plus(changeInBudgeted)
        val updatedCategory = category.copy(totalBudgeted = newCatBudgeted)
        categoryRepository.updateLocalData(category)

        val oldAvaBudgeted = uiState.value.available.totalBudgeted
        val newAvaBudgeted = oldAvaBudgeted.plus(changeInBudgeted)
        val updatedAvailable = uiState.value.available.copy(totalBudgeted = newAvaBudgeted)
        uiState.value = uiState.value.copy(available = updatedAvailable)
        availableRepository.updateLocalData(updatedAvailable)

        Log.d(TAG, "updateBudgetItem completed.")
        Log.d(TAG, "oldItem: $item,")
        Log.d(TAG, "category:$category,")
        Log.d(TAG, "available: ${uiState.value.available}")
    }


//    fun onSelectedDateChange(date: LocalDate) {
//        getRepositoryData(date)
//    }
}