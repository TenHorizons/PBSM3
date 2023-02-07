package com.example.pbsm3.ui.navhost

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.pbsm3.data.defaultBudget
import com.example.pbsm3.data.defaultBudgetItem
import com.example.pbsm3.ui.MainState
import com.example.pbsm3.ui.screens.*
import com.example.pbsm3.ui.theme.PBSM3Theme

@Composable
fun NavHostBuilder(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Screen.Budget.name,
    uiState:MainState
) {
//    val backStack = navController.get
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = Screen.Budget.name) {
            BudgetScreen(budget = defaultBudget, date = uiState.selectedDate)
        }

        composable(route = Screen.BudgetItem.name) {
            BudgetItemScreen(
                budgetItem = defaultBudgetItem,
                onNavigateUp = {
                    navController.navigate(Screen.Budget.name)
                })
        }

        composable(route = Screen.AddTransaction.name) {
            AddTransactionScreen()
        }

        composable(route = Screen.Account.name) {
            AccountScreen(onNavigateUp = {})
        }

        composable(route = Screen.AccountTransactions.name){
            AccountTransactionsScreen(onNavigateUp = {})
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NavHostPreview() {
    PBSM3Theme {
        NavHostBuilder(
            navController = rememberNavController(),
            startDestination = Screen.Budget.name,
            modifier = Modifier.fillMaxSize(),
            uiState = MainState()
        )
    }
}