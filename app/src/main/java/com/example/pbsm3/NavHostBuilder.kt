package com.example.pbsm3

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.pbsm3.ui.screens.accountTransactions.AccountTransactionsScreen
import com.example.pbsm3.ui.screens.accounts.AccountsScreen
import com.example.pbsm3.ui.screens.addAccount.AddAccountScreen
import com.example.pbsm3.ui.screens.addTransaction.AddTransactionScreen
import com.example.pbsm3.ui.screens.budget.BudgetScreen
import com.example.pbsm3.ui.screens.budgetItem.BudgetItemScreen
import com.example.pbsm3.ui.screens.login.SignInScreen
import com.example.pbsm3.ui.screens.signup.SignUpScreen
import com.example.pbsm3.ui.screens.splash.SplashScreen
import java.time.LocalDate

@Composable
fun NavHostBuilder(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String,
    selectedDate: LocalDate,
    appState: AppState,
    onScreenChange: (Screen) -> Unit,
    onTopBarChange: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = Screen.SignInScreen.name) {
            SignInScreen(
                onSignUpClick = {
                    onScreenChange(Screen.SignUpScreen)
                    appState.navigateAndPopUp(Screen.SignUpScreen.name, Screen.SignInScreen.name)
                },
                onComplete = {
                    appState.navigateAndPopUp(Screen.Splash.name, Screen.SignInScreen.name)
                }
            )
        }
        composable(route = Screen.SignUpScreen.name) {
            SignUpScreen(
                onComplete = {
                    onScreenChange(Screen.Splash)
                    appState.navigateAndPopUp(Screen.Splash.name, Screen.SignUpScreen.name)
                },
                onBackPressed = {
                    onScreenChange(Screen.SignInScreen)
                    appState.navigateAndPopUp(Screen.SignInScreen.name, Screen.SignUpScreen.name)
                }
            )
        }

        composable(route = Screen.Splash.name) {
            SplashScreen(
                onStartupComplete = {
                    onScreenChange(Screen.Budget)
                    appState.navigateAndPopUp(Screen.Budget.name, Screen.Splash.name)
                },
                onBackPressed = { appState.onBackPressed(Screen.Splash, onScreenChange) }
            )
        }
        composable(route = Screen.Budget.name) {
            BudgetScreen(
                selectedDate = selectedDate,
                onItemClicked = { _, budgetItem ->
                    onTopBarChange(budgetItem.name)
                    onScreenChange(Screen.BudgetItem)
                    appState.navigate("${Screen.BudgetItem.name}/${budgetItem.name}")
                },
                onBackPressed = { appState.onBackPressed(Screen.Budget, onScreenChange) }
            )
        }

        composable(
            //TODO: convert to Firebase doc ID
            route = "${Screen.BudgetItem.name}/{budgetItemName}"
        ) {
            BudgetItemScreen(
                budgetItemName = it.arguments?.getString("budgetItemName") ?: "",
                onBackPressed = { appState.onBackPressed(Screen.BudgetItem, onScreenChange) }
            )
        }

        composable(route = Screen.AddTransaction.name) {
            AddTransactionScreen(
                onBackPressed = { appState.onBackPressed(Screen.AddTransaction, onScreenChange) }
            )
        }

        composable(route = Screen.Accounts.name) {
            AccountsScreen(
                onAccountClicked = { account ->
                    onTopBarChange(account.name)
                    onScreenChange(Screen.AccountTransactions)
                    appState.navigate("${Screen.AccountTransactions.name}/${account.id}")
                },
                onBackPressed = { appState.onBackPressed(Screen.Accounts, onScreenChange) }
            )
        }

        composable(
            route = "${Screen.AccountTransactions.name}/{accountRef}"
        ) {
            AccountTransactionsScreen(
                accountRef = it.arguments?.getString("accountRef") ?: "",
                onBackPressed = {
                    appState.onBackPressed(
                        Screen.AccountTransactions, onScreenChange)
                }
            )
        }

        composable(route = Screen.AddAccountScreen.name) {
            AddAccountScreen(
                onBackPressed = { appState.onBackPressed(Screen.AddAccountScreen, onScreenChange) },
                onAddAccountComplete = {
                    onScreenChange(Screen.Accounts)
                    appState.navigate(Screen.Accounts.name)
                }
            )
        }
    }
}