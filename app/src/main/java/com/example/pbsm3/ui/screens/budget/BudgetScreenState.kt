package com.example.pbsm3.ui.screens.budget

import com.example.pbsm3.model.Unassigned
import com.example.pbsm3.model.NewBudgetItem
import com.example.pbsm3.model.NewCategory
import java.time.LocalDate

data class BudgetScreenState(
    //val budget: Budget = defaultBudget,
    val selectedDate: LocalDate = LocalDate.now(),
    val unassigned: Unassigned = Unassigned(),
    val displayedCategories:List<NewCategory> = listOf(),
    val displayedBudgetItems:List<NewBudgetItem> = listOf(),
    val categoryItemMapping: MutableMap<NewCategory, List<NewBudgetItem>> = mutableMapOf()
)