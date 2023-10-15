package com.example.pbsm3.ui.screens.splash

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

private const val TAG = "SplashScreen"

//Firebase Implementation
@Composable
fun SplashScreen(
    onStartupComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    onBackPressed:()->Unit={}
) {
    val start = System.currentTimeMillis()
    Log.d(TAG,"screen start: $start")
    BackHandler(onBack = onBackPressed)
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uiState by viewModel.uiState
        if (uiState.showError) {
            Text(text = uiState.errorMessage)

            Button(
                modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp),
                onClick = { viewModel.onAppStart(onStartupComplete) }
            ){
                Text(text = "Try again", fontSize = 16.sp)
            }
        } else {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
        }
    }

    LaunchedEffect(true) {
//        delay(10000L)
        viewModel.onAppStart(onStartupComplete = onStartupComplete)
    }

    val end = System.currentTimeMillis()
    Log.d(TAG,"screen end: $end")
    Log.d(TAG,"screen time: ${end-start}")
}

//Room implementation
/*
@Composable
fun SplashScreen(
    onStartupComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel(),
    onBackPressed:()->Unit={}
) {
    BackHandler(onBack = onBackPressed)
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val uiState by viewModel.uiState
        if (uiState.showError) {
            Text(text = uiState.errorMessage)

            Button(
                modifier = Modifier.fillMaxWidth().padding(16.dp, 8.dp),
                onClick = { viewModel.onAppStart(onStartupComplete) }
            ){
                Text(text = "Try again", fontSize = 16.sp)
            }
        } else {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.onBackground)
        }
    }

    LaunchedEffect(true) {
//        delay(10000L)
        viewModel.onAppStart(onStartupComplete = onStartupComplete)
    }
}*/