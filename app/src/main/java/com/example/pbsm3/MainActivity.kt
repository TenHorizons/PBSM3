@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pbsm3

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.example.pbsm3.theme.PBSM3Theme
import com.example.pbsm3.ui.commonScreenComponents.PBSBottomNav
import com.example.pbsm3.ui.commonScreenComponents.PBSTopBar
import com.example.pbsm3.ui.commonScreenComponents.snackbar.SnackbarManager
import com.example.pbsm3.ui.navhost.NavHostBuilder
import com.example.pbsm3.ui.navhost.Screen
import kotlinx.coroutines.CoroutineScope

private const val TAG = "MainActivity"

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PBSM3Theme {
                Main()
                /*//Firestore Test
                // Create a new user with a first, middle, and last name
                val user = hashMapOf(
                    "first" to "Alan",
                    "middle" to "Mathison",
                    "last" to "Turing",
                    "born" to 1912
                )
                val db = Firebase.firestore
                // Add a new document with a generated ID
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    }

                db.collection("users")
                    .get()
                    .addOnSuccessListener { result ->
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.w(TAG, "Error getting documents.", exception)
                    }*/
            }
        }
    }
}

@Composable
fun Main(viewModel: MainViewModel = viewModel()) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val appState = rememberAppState()
        val uiState by viewModel.uiState.collectAsState()
        Scaffold(
            topBar = {
                PBSTopBar(
                    screen = uiState.currentScreen,
                    onDateSelected = { newDate ->
                        viewModel.setSelectedDate(newDate)
                    })
            },
            bottomBar = {
                PBSBottomNav(
                    onClick = { screen ->
                        appState.navController.navigate(screen.route)
                        viewModel.updateCurrentScreen(screen)
                        appState.navController.popBackStack()
                    },
                    screen = uiState.currentScreen
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
                startDestination = Screen.Login.route,
                modifier = Modifier.padding(innerPadding),
                uiState = uiState,
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