@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pbsm3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.pbsm3.ui.commonScreenComponents.PBSBottomNav
import com.example.pbsm3.ui.commonScreenComponents.PBSTopBar
import com.example.pbsm3.ui.navhost.NavHostBuilder
import com.example.pbsm3.ui.navhost.Screen
import com.example.pbsm3.ui.theme.PBSM3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PBSM3Theme {
                // A surface container using the 'background' color from the theme
                var currentScreen = Screen.Budget
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    Scaffold(
                        topBar = { PBSTopBar(screen = currentScreen) },
                        bottomBar = {
                            PBSBottomNav(
                                onClick = {_,screen ->
                                    navController.navigate(screen.name)
                                    currentScreen = screen
                                }
                            )
                        }
                    ) { innerPadding ->
                        NavHostBuilder(
                            navController = navController,
                            onScreenChange = { currentScreen = it },
                            startDestination = currentScreen.name,
                            modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PBSM3Theme {
        var currentScreen = Screen.Budget
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background) {
            Scaffold(
                topBar = { PBSTopBar(screen = currentScreen) },
                bottomBar = {
                    PBSBottomNav(
                        onClick = {_, screen ->
                            navController.navigate(screen.name)
                            currentScreen = screen
                        }
                    )
                }
            ) { innerPadding ->
//                BudgetScreen(budget = defaultBudget,
//                    date = currentDate,
//                    modifier = Modifier.padding(innerPadding))
                NavHostBuilder(
                    navController = navController,
                    onScreenChange = { currentScreen = it },
                    startDestination = currentScreen.name,
                    modifier = Modifier.padding(innerPadding))
            }
        }
    }
}