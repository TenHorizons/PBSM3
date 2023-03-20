package com.example.pbsm3

import android.content.res.Resources
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@Composable
fun Main() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        var currentScreen: Screen by remember { mutableStateOf(Screen.Splash) }
        val appState = rememberAppState(onScreenChange = {
            if (it != null) currentScreen = it
        })
        var selectedDate: LocalDate by remember { mutableStateOf(getFirstDayOfMonth()) }

        Scaffold(
            topBar = {
                PBSTopBar(
                    screen = currentScreen,
                    onDateSelected = { newDate ->
                        selectedDate = newDate
                    })
            },
            bottomBar = {
                PBSBottomNav(
                    onClick = { screen ->
                        appState.navigate(screen.name)
                    },
                    screen = currentScreen
                )
            },
            snackbarHost = {
                SnackbarHost(
                    hostState = appState.snackbarHostState,
                    modifier = Modifier.padding(8.dp),
                    snackbar = { snackbarData ->
                        Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.onPrimary)
                    }
                )
            }
        ) { innerPadding ->
            NavHostBuilder(
                navController = appState.navController,
                startDestination = Screen.Splash.name,
                modifier = Modifier.padding(innerPadding),
                selectedDate = selectedDate,
                appState = appState,
                onScreenChange = { screen -> currentScreen = screen }
            )
        }
    }
}

@Composable
fun rememberAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    onScreenChange: (Screen?) -> Unit
) =
    remember(
        snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        AppState(
            snackbarHostState, navController, snackbarManager, resources, coroutineScope,
            onScreenChange)
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