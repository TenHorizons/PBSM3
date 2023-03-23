package com.example.pbsm3.ui.screens.splash

import com.example.pbsm3.model.Account
import com.example.pbsm3.model.BudgetItem
import java.time.LocalDate

data class TransactionScreenState(
    val amount: Double = 0.0,
    val selectedCategory: BudgetItem?=null,
    val selectedAccount: Account?=null,
    val selectedDate:LocalDate = LocalDate.now(),
    val memoText: String = "",
)