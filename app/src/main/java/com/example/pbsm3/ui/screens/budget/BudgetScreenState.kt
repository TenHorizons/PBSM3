package com.example.pbsm3.ui.screens.budget

import com.example.pbsm3.data.defaultBudget
import com.example.pbsm3.data.getFirstDayOfMonth
import com.example.pbsm3.model.Budget
import java.time.LocalDate

data class BudgetScreenState(
    val budget: Budget = defaultBudget,
    val selectedDate: LocalDate = getFirstDayOfMonth()
)