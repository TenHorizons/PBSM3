package com.example.pbsm3.ui.screens.splash

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.repository.UserRepository
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "SplashScreenViewModel"

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    logService: LogService
) : CommonViewModel(logService) {
    val uiState = mutableStateOf(SplashViewModelState())

    fun onAppStart(onStartupComplete: () -> Unit) {
        val start = System.currentTimeMillis()
        Log.d(TAG, "load start: $start")
        uiState.value = uiState.value.copy(showError = false)
        uiState.value = uiState.value.copy(errorMessage = "")

        viewModelScope.launch {
            loadUserData()

            onStartupComplete()
        }
    }

    /**Launches IO thread to get user data*/
    private suspend fun loadUserData() {
        userRepository.loadUserData(onError = {
            showError.value = true
            errorMessage.value = "error loading user data. Error: $it."
            Log.d(TAG, "error loading user data. Error: $it.")
        })
    }
}

data class SplashViewModelState(
    val showError: Boolean = false,
    val errorMessage: String = ""
)