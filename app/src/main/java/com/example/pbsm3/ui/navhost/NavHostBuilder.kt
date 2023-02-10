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
    startDestination: String,
    uiState:MainState,
    onScreenChange:(Screen)->Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = Screen.Login.route){
            LoginScreen(onVerified = {userId ->
                navController.navigate(Screen.Budget.route)
                onScreenChange(Screen.Budget)
                //TODO: load user data.
                navController.popBackStack()
            })
        }

        composable(route = Screen.Budget.route) {
            BudgetScreen(budget = defaultBudget, date = uiState.selectedDate)
        }

        composable(route = Screen.BudgetItem.route) {
            BudgetItemScreen(
                budgetItem = defaultBudgetItem,
                onNavigateUp = {
                    navController.navigate(Screen.Budget.route)
                    onScreenChange(Screen.Budget)
                    navController.popBackStack()
                })
        }

        composable(route = Screen.AddTransaction.route) {
            AddTransactionScreen()
        }

        composable(route = Screen.Accounts.route) {
            AccountsScreen()
        }

        composable(route = Screen.AccountTransactions.route){
            AccountTransactionsScreen(onNavigateUp = {
                navController.navigate(Screen.Accounts.route)
                onScreenChange(Screen.Accounts)
                navController.popBackStack()
            })
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NavHostPreview() {
    PBSM3Theme {
        NavHostBuilder(
            navController = rememberNavController(),
            startDestination = Screen.Budget.route,
            modifier = Modifier.fillMaxSize(),
            uiState = MainState(),
            onScreenChange = {}
        )
    }
}