package com.example.pbsm3.ui.screens.viewmodel

import com.example.pbsm3.data.defaultAccount
import com.example.pbsm3.model.Account

data class AccountScreenState(
    val accountList:List<Account> = listOf(defaultAccount)
)