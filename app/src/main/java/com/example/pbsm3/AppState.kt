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
class AppState(
    val snackbarHostState: SnackbarHostState,
    val navController: NavHostController,
    private val snackbarManager: SnackbarManager,
    private val resources: Resources,
    coroutineScope: CoroutineScope,
    val onScreenChange:(Screen?)->Unit
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
        onScreenChange(getCurrentScreen())
    }

    fun navigate(route: String) {
        navController.navigate(route) { launchSingleTop = true }
        onScreenChange(getCurrentScreen())
    }

    fun navigateAndPopUp(route: String, popUp: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(popUp) { inclusive = true }
        }
        onScreenChange(getCurrentScreen())
    }

    fun clearAndNavigate(route: String) {
        navController.navigate(route) {
            launchSingleTop = true
            popUpTo(0) { inclusive = true }
        }
        onScreenChange(getCurrentScreen())
    }
    private fun getCurrentScreen():Screen?{
        if(navController.currentBackStackEntry?.destination?.route != null){
            return Screen.valueOf(
                navController.currentBackStackEntry?.destination?.route!!)
        }
        return null
    }
}