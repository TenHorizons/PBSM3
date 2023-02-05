@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pbsm3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pbsm3.ui.commonScreenComponents.PBSBottomNav
import com.example.pbsm3.ui.commonScreenComponents.PBSTopBar
import com.example.pbsm3.ui.navhost.NavHostBuilder
import com.example.pbsm3.ui.navhost.Screen
import com.example.pbsm3.ui.theme.PBSM3Theme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "Main"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PBSM3Theme {
                val db = Firebase.firestore
                firestoreTest(db)
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
                            startDestination = currentScreen.name,
                            modifier = Modifier.padding(innerPadding))
                    }
                }
            }
        }
    }

    private fun firestoreTest(db: FirebaseFirestore) {
        // Create a new user with a first, middle, and last name
        val user = hashMapOf(
            "first" to "Alan",
            "middle" to "Mathison",
            "last" to "Turing",
            "born" to 1912
        )

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
            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PBSM3Theme {
        val navController:NavHostController = rememberNavController()
        val backStackEntry by navController.currentBackStackEntryAsState()
        var currentScreen = Screen.valueOf(
            backStackEntry?.destination?.route ?: Screen.Budget.name
        )
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
                NavHostBuilder(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding))
            }
        }
    }
}