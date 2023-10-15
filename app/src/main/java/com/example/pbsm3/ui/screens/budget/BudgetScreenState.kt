package com.example.pbsm3.ui.screens.budget

import com.example.pbsm3.firebaseModel.BudgetItem
import com.example.pbsm3.firebaseModel.Category
import com.example.pbsm3.firebaseModel.Unassigned
import java.time.LocalDate

data class BudgetScreenState(
    val selectedDate: LocalDate = LocalDate.now(),
    val unassigned: Unassigned = Unassigned(),
    val displayedCategories:List<Category> = listOf(),
    val displayedBudgetItems:List<BudgetItem> = listOf(),
    val categoryItemMapping: Map<Category, List<BudgetItem>> = mapOf()
)