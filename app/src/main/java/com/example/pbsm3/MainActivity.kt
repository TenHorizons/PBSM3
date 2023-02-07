@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.pbsm3

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.pbsm3.ui.MainViewModel
import com.example.pbsm3.ui.commonScreenComponents.PBSBottomNav
import com.example.pbsm3.ui.commonScreenComponents.PBSTopBar
import com.example.pbsm3.ui.navhost.NavHostBuilder
import com.example.pbsm3.ui.navhost.Screen
import com.example.pbsm3.ui.theme.PBSM3Theme
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PBSM3Theme {
                Main()
            }
        }
    }
}

@Composable
fun Main(viewModel: MainViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val navController = rememberNavController()
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background) {
        Scaffold(
            topBar = {
                PBSTopBar(
                    screen = uiState.currentScreen,
                    onDateSelected ={ newDate ->
                        viewModel.setSelectedDate(newDate)
                    })
                     },
            bottomBar = {
                PBSBottomNav { _, screen ->
                    navController.navigate(screen.name)
                    viewModel.updateCurrentScreen(screen)
                }
            }
        ) { innerPadding ->
            NavHostBuilder(
                navController = navController,
                startDestination = uiState.currentScreen.name,
                modifier = Modifier.padding(innerPadding),
                uiState = uiState
            )
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

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    PBSM3Theme {
        Main()
    }
}