package com.example.pbsm3.ui.screens.accountTransactions

import com.example.pbsm3.model.Transaction

data class AccountTransactionsScreenState(
    val transactions:List<Transaction> = listOf()
)