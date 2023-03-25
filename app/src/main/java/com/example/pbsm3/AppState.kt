package com.example.pbsm3

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.navigation.NavHostController
import com.example.pbsm3.ui.commonScreenComponents.snackbar.SnackbarManager
import com.example.pbsm3.ui.commonScreenComponents.snackbar.SnackbarMessage.Companion.toMessage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

//TODO: Declare as stable when add variables?
//TODO: find time to properly clean and manage backstack
class AppState(
    val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope
) {
    init {
        coroutineScope.launch {
            snackbarManager.snackbarMessages.filterNotNull().collect { snackbarMessage ->
                val text = snackbarMessage.toMessage(resources)
                snackbarHostState.showSnackbar(text)
            }
        }
    }

    fun popUp() {
        navController.popBackStack()
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
    }

    private fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
    }
    
    fun onBackPressed(
        currentScreen:Screen,
        onScreenChange:(Screen)->Unit
    ){
        when(currentScreen){
            Screen.BudgetItem -> {
                onScreenChange(Screen.Budget)
                clearAndNavigate(Screen.Budget.name)
            }
            Screen.AddAccountScreen -> {
                onScreenChange(Screen.Accounts)
                clearAndNavigate(Screen.Accounts.name)
            }
            Screen.AccountTransactions -> {
                onScreenChange(Screen.Accounts)
                clearAndNavigate(Screen.Accounts.name)
            }
            else -> {
                navController.popBackStack(navController.graph.id,true)
                navController.navigateUp()
            }
        }
    }
}