package com.example.pbsm3

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
import com.example.pbsm3.data.getFirstDayOfMonth
import com.example.pbsm3.theme.PBSM3Theme
import com.example.pbsm3.ui.screens.accountTransactions.AccountTransactionsScreen
import com.example.pbsm3.ui.screens.accounts.AccountsScreen
import com.example.pbsm3.ui.screens.addTransaction.AddTransactionScreen
import com.example.pbsm3.ui.screens.budget.BudgetScreen
import com.example.pbsm3.ui.screens.budgetItem.BudgetItemScreen
import com.example.pbsm3.ui.screens.login.LoginScreen
import com.example.pbsm3.ui.screens.splash.SplashScreen
import java.time.LocalDate

@Composable
fun NavHostBuilder(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    selectedDate: LocalDate,
    appState: AppState,
    onScreenChange:(Screen)->Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = Screen.Splash.name){
            SplashScreen(
                navigateAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp)
                }
            )
        }
        composable(route = Screen.Login.name){
            LoginScreen(
                navigateAndPopUpInclusive = { route, popUp ->
                    appState.navigateAndPopUp(route, popUp)
                }
            )
        }

        composable(route = Screen.Budget.name) {
            BudgetScreen(budget = defaultBudget, date = selectedDate)
        }

        composable(route = Screen.BudgetItem.name) {
            BudgetItemScreen(
                budgetItem = defaultBudgetItem,
                onNavigateUp = {
                    navController.navigate(Screen.Budget.name)
                    onScreenChange(Screen.Budget)
                    navController.popBackStack()
                })
        }

        composable(route = Screen.AddTransaction.name) {
            AddTransactionScreen()
        }

        composable(route = Screen.Accounts.name) {
            AccountsScreen()
        }

        composable(route = Screen.AccountTransactions.name){
            AccountTransactionsScreen(onNavigateUp = {
                navController.navigate(Screen.Accounts.name)
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
            startDestination = Screen.Budget.name,
            modifier = Modifier.fillMaxSize(),
            selectedDate = getFirstDayOfMonth(),
            onScreenChange = {},
            appState = rememberAppState()
        )
    }
}