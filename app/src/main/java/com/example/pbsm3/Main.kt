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
import androidx.lifecycle.viewmodel.compose.viewModel
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
fun Main(viewModel: MainViewModel = viewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val appState = rememberAppState()

        val selectedDate: LocalDate by remember{ mutableStateOf(getFirstDayOfMonth()) }
        val currentScreen:Screen by remember{ mutableStateOf(Screen.Login) }

        Scaffold(
            topBar = {
                PBSTopBar(
                    screen = currentScreen,
                    onDateSelected = { newDate ->
                        viewModel.setSelectedDate(newDate)
                    })
            },
            bottomBar = {
                PBSBottomNav(
                    onClick = { screen ->
                        appState.navController.navigate(screen.name)
                        viewModel.updateCurrentScreen(screen)
                        appState.navController.popBackStack()
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
                onScreenChange = { screen -> viewModel.updateCurrentScreen(screen) }
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
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(snackbarHostState, navController, snackbarManager, resources, coroutineScope) {
        AppState(snackbarHostState, navController, snackbarManager, resources, coroutineScope)
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