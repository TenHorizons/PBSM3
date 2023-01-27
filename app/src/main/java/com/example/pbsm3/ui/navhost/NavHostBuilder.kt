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
import com.example.pbsm3.ui.screens.BudgetScreen
import com.example.pbsm3.ui.screens.TransactionScreen
import com.example.pbsm3.ui.screens.BudgetItemScreen
import com.example.pbsm3.ui.theme.PBSM3Theme

@Composable
fun NavHostBuilder(
    navController: NavHostController,
    onScreenChange: (Screen) -> Unit,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Screen.Budget.name) {
            BudgetScreen(
                budget = defaultBudget,
                date = getFirstDayOfMonth(),
                modifier = modifier)
        }

        composable(Screen.BudgetItem.name) {
            BudgetItemScreen(
                budgetItem = defaultBudgetItem,
                modifier = modifier)
        }

        composable(Screen.Transaction.name) {
            TransactionScreen(
                onNavigateUp = {},
                modifier = modifier)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun NavHostPreview() {
    PBSM3Theme {
        NavHostBuilder(
            navController = rememberNavController(),
            onScreenChange = {},
            startDestination = Screen.Budget.name,
            modifier = Modifier.fillMaxSize()
        )
    }
}