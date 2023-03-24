package com.example.pbsm3.ui.screens.accounts

import com.example.pbsm3.data.defaultAccounts
import com.example.pbsm3.data.defaultTransactions
import com.example.pbsm3.model.Account
import com.example.pbsm3.model.Transaction

data class AccountScreenState(
    var accounts:List<Account> = defaultAccounts,
    var transactions:List<Transaction> = defaultTransactions
)