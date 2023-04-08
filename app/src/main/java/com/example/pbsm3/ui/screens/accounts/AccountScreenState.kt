package com.example.pbsm3.ui.screens.accounts

import com.example.pbsm3.model.Account
import com.example.pbsm3.model.Transaction

data class AccountScreenState(
    var accounts:List<Account> = listOf(),
    var transactions:List<Transaction> = listOf()
)