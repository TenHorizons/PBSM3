package com.example.pbsm3

import com.example.pbsm3.data.defaultBudget
import com.example.pbsm3.data.defaultCategories
import com.example.pbsm3.data.getFirstDayOfMonth
import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.Category
import com.example.pbsm3.ui.screens.viewmodel.BudgetScreenViewModel
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class BudgetScreenUnitTest {
    @Test
    fun budgetScreen_dateWithExistingBudgetSelected_DisplaysExistingBudget() {
        val existingCategories = defaultCategories as MutableList
        existingCategories[0] = existingCategories[0].copy(totalBudgeted = 20.0)
        val existingMonthlyBudget = mapOf(
            getFirstDayOfMonth() to existingCategories
        )
        val existingBudget = defaultBudget.copy(
            monthlyBudgets = existingMonthlyBudget
        )
        val viewModel = BudgetScreenViewModel()
        viewModel.updateBudget(existingBudget)
        viewModel.updateDate(getFirstDayOfMonth())
        assert(viewModel.getCategoriesByDate(getFirstDayOfMonth()) == existingCategories)
    }
    @Test
    fun budgetScreen_changeBudgetItem_DisplaysChangedItem() {
        val viewModel = BudgetScreenViewModel()
        viewModel.updateBudget(defaultBudget)
        val selectedDate = getFirstDayOfMonth()
        viewModel.updateDate(selectedDate)
        val category:Category = viewModel.uiState.value.budget.monthlyBudgets[selectedDate]!![0]
        var item: BudgetItem = category.items[0]
        item = item.copy(budgeted = 20.0)
        viewModel.updateBudgetItem(category = category, newItem = item, itemIndex = 0)
        assert(
            viewModel.uiState.value.budget
                .monthlyBudgets[selectedDate]!![0].items[0].budgeted == 20.0)
    }
}