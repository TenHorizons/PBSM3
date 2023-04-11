package com.example.pbsm3.ui.screens.budget

import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.Category
import com.example.pbsm3.model.Unassigned
import java.time.LocalDate

data class BudgetScreenState(
    val selectedDate: LocalDate = LocalDate.now(),
    val unassigned: Unassigned = Unassigned(),
    val displayedCategories:List<Category> = listOf(),
    val displayedBudgetItems:List<BudgetItem> = listOf(),
    val categoryItemMapping: Map<Category, List<BudgetItem>> = mapOf()
)