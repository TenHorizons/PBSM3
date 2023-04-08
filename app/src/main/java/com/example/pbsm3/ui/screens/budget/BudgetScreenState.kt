package com.example.pbsm3.ui.screens.budget

import com.example.pbsm3.model.Unassigned
import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.Category
import java.time.LocalDate

data class BudgetScreenState(
    //val budget: Budget = defaultBudget,
    val selectedDate: LocalDate = LocalDate.now(),
    val unassigned: Unassigned = Unassigned(),
    val displayedCategories:List<Category> = listOf(),
    val displayedBudgetItems:List<BudgetItem> = listOf(),
    val categoryItemMapping: MutableMap<Category, List<BudgetItem>> = mutableMapOf()
)