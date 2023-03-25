package com.example.pbsm3.ui.screens.splash

import java.time.LocalDate

data class TransactionScreenState(
    val amount: Double = 0.0,
    val selectedCategory: String="",//BudgetItem?=null,
    val selectedAccount: String="",//Account?=null,
    val selectedDate:LocalDate = LocalDate.now(),
    val memoText: String = "",
)