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
import com.example.pbsm3.data.getFirstDayOfMonth
import com.example.pbsm3.ui.screens.AccountScreen
import com.example.pbsm3.ui.screens.BudgetItemScreen
import com.example.pbsm3.ui.screens.BudgetScreen
import com.example.pbsm3.ui.screens.TransactionScreen
import com.example.pbsm3.ui.theme.PBSM3Theme

@Composable
fun NavHostBuilder(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: String = Screen.Budget.name
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(route = Screen.Budget.name) {
            BudgetScreen(
                budget = defaultBudget,
                date = getFirstDayOfMonth())
        }

        composable(route = Screen.BudgetItem.name) {
            BudgetItemScreen(budgetItem = defaultBudgetItem)
        }

        composable(route = Screen.Transaction.name) {
            TransactionScreen(onNavigateUp = {})
        }

        composable(route = Screen.Account.name) {
            AccountScreen()
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
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NavHostPreview2() {
    PBSM3Theme {
        NavHostBuilder(
            navController = rememberNavController(),
            startDestination = Screen.Budget.name,
            modifier = Modifier.fillMaxSize()
        )
    }
}