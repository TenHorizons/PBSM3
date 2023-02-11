package com.example.pbsm3.ui.screens.budget

import androidx.lifecycle.ViewModel
import com.example.pbsm3.data.defaultCategories
import com.example.pbsm3.model.Budget
import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.Category
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class BudgetScreenViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(BudgetScreenState())
    val uiState: StateFlow<BudgetScreenState> = _uiState.asStateFlow()
    fun updateBudget(budget:Budget){
        _uiState.update { currentState ->
            currentState.copy(budget = budget)
        }
    }

    fun updateDate(date: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(selectedDate = date)
        }
    }

    fun getCategoriesByDate(date: LocalDate):List<Category> {
        return _uiState.value.budget.monthlyBudgets[date] ?: defaultCategories
    }

    fun updateBudgetItem(category:Category, newItem:BudgetItem, itemIndex:Int){
        //not-null assertion operator should succeed.
        //If not, check why budget for selected date was not generated
        val budget = _uiState.value.budget
        //budget.monthlyBudgets as MutableMap doesn't work here:
        //https://stackoverflow.com/questions/69850726/unsupportedoperationexception-when-adding-to-a-map-in-kotlin
        val monthlyBudgets = budget.monthlyBudgets.toMutableMap()
        val selectedDate = _uiState.value.selectedDate
        val budgetCategories = monthlyBudgets[selectedDate]!! as MutableList
        var oldCategory = budgetCategories[budgetCategories.indexOf(category)]
        val items = oldCategory.items as MutableList

        items[itemIndex] = newItem
        oldCategory = oldCategory.copy(items = items.toList())
        budgetCategories[budgetCategories.indexOf(category)] = oldCategory
        monthlyBudgets[selectedDate] = budgetCategories.toList()
        _uiState.update {currentState ->
            currentState.copy(
                budget = currentState.budget.copy(
                    monthlyBudgets = monthlyBudgets.toMap()
                )
            )
        }
    }
}