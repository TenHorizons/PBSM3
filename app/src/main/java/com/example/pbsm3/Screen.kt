package com.example.pbsm3

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.graphics.vector.ImageVector

//Based on bottom navigation docs:
//https://developer.android.com/jetpack/compose/navigation#bottom-nav
enum class Screen(val icon:ImageVector? = null){
    Budget(icon = Icons.Filled.Savings),
    BudgetItem,
    Accounts(icon = Icons.Filled.AccountBalance),
    AddTransaction(icon = Icons.Filled.AddCircle),
    AccountTransactions,
    Login,
    Splash,
    AddAccountScreen
}

val bottomNavItems = listOf(
    Screen.Budget,
    Screen.AddTransaction,
    Screen.Accounts
)