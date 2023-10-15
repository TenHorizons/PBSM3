package com.example.pbsm3

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.pbsm3.data.getFirstDayOfMonth
import com.example.pbsm3.theme.PBSM3Theme
import com.example.pbsm3.ui.commonScreenComponents.PBSBottomNav
import com.example.pbsm3.ui.commonScreenComponents.PBSTopBar
import com.example.pbsm3.ui.commonScreenComponents.snackbar.SnackbarManager
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate


private const val TAG = "Main"
@Composable
fun Main() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var currentScreen: Screen by remember { mutableStateOf(Screen.SignInScreen) }
        val appState = rememberAppState()
        var selectedDate: LocalDate by remember { mutableStateOf(getFirstDayOfMonth()) }
        var customTopBarText: String by remember { mutableStateOf("") }

        Scaffold(
            topBar = {
                PBSTopBar(
                    screen = currentScreen,
                    onDateSelected = { newDate ->
                        selectedDate = newDate
                    },
                    onActionClicked = {
                        manageActions(currentScreen, appState,
                            onScreenChange = { newScreen -> currentScreen = newScreen }
                        )
                    },
                    onNavigateUp = {
                        appState.onBackPressed(
                            currentScreen = currentScreen,
                            onScreenChange = {
                                    newScreen -> currentScreen = newScreen
                            }
                        )
                    },
                    customTopBarText = customTopBarText
                )
            },
            bottomBar = {
                PBSBottomNav(
                    onClick = { screen ->
                        appState.navigate(screen.name)
                        currentScreen = screen
                    },
                    screen = currentScreen
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = appState.snackbarHostState,
                    modifier = Modifier.padding(8.dp),
                    snackbar = { snackbarData ->
                        //TODO: Use Snackbar to confirm actions and show errors
                        Snackbar(
                            snackbarData,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                )
            }
        ) { innerPadding ->
            NavHostBuilder(
                navController = appState.navController,
                startDestination = Screen.SignInScreen.name,
                modifier = Modifier.padding(innerPadding),
                selectedDate = selectedDate,
                appState = appState,
                onScreenChange = { screen -> currentScreen = screen },
                onTopBarChange = { customTopBarText = it }
            )
        }
    }
}

fun manageActions(
    screen: Screen,
    appState: AppState,
    onScreenChange: (Screen) -> Unit
) {
    when (screen) {
        Screen.Accounts -> {
            Log.d(TAG,"Add Account Button clicked")
            onScreenChange(Screen.AddAccountScreen)
            appState.navigate(Screen.AddAccountScreen.name)
        }
        else -> {}
    }
}

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(
        snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        AppState(
            snackbarHostState, navController, snackbarManager, resources, coroutineScope)
    }

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PBSM3Theme {
        Main()
    }
}