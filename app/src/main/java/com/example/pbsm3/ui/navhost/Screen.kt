package com.example.pbsm3.ui.navhost

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Savings
import androidx.compose.ui.graphics.vector.ImageVector

//Based on bottom navigation docs:
//https://developer.android.com/jetpack/compose/navigation#bottom-nav
enum class Screen(val route:String, val icon:ImageVector? = null){
    Budget(route = "Budget",icon = Icons.Filled.Savings),
    BudgetItem(route = "Budget_Item"),
    Accounts(route = "Account", icon = Icons.Filled.AccountBalance),
    AddTransaction(route = "AddTransaction", icon = Icons.Filled.AddCircle),
    AccountTransactions(route = "AccountTransactions"),
    Login(route = "Login")
}

val bottomNavItems = listOf(
    Screen.Budget,
    Screen.AddTransaction,
    Screen.Accounts
)