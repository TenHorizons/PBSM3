package com.example.pbsm3.ui.screens.addTransaction

import java.math.BigDecimal
import java.time.LocalDate

data class AddTransactionScreenState(
    val amount: BigDecimal = BigDecimal.ZERO,
    val selectedCategoryName: String = "",
    val selectedAccountName: String = "",
    val selectedDate:LocalDate = LocalDate.now(),
    val memoText: String = "",
)