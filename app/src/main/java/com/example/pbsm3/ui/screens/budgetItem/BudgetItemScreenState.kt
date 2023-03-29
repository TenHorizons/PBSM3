package com.example.pbsm3.ui.screens.budgetItem

import java.math.BigDecimal
import java.time.LocalDate

data class BudgetItemScreenState(
    val budgetItemName:String = "",
    val totalCarryover: BigDecimal = BigDecimal("0"),
    val totalExpenses:BigDecimal = BigDecimal("0"),
    val totalBudgeted:BigDecimal = BigDecimal("0"),
    val date:LocalDate = LocalDate.now()
)
