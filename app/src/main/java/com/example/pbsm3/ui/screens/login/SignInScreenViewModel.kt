package com.example.pbsm3.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.pbsm3.firebaseModel.service.LogService
import com.example.pbsm3.firebaseModel.service.repository.UserRepository
import com.example.pbsm3.ui.commonScreenComponents.snackbar.SnackbarManager
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInScreenViewModel @Inject constructor(
    private val userRepo: UserRepository,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(SignInScreenState())
        private set

    private val username
        get() = uiState.value.username
    private val password
        get() = uiState.value.password

    var signInClickInProgress = mutableStateOf(false)
        private set
    var isPasswordWrong = mutableStateOf(false)
        private set

    fun onUsernameChange(newValue: String) {
        uiState.value = uiState.value.copy(username = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(onComplete: () -> Unit) {
        if (username.isBlank()) {
            SnackbarManager.showMessage("Please insert a valid email.")
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage("Password cannot be empty.")
            return
        }
        signInClickInProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val complete = userRepo.signIn(username, password, onError = {
                SnackbarManager.showMessage("Invalid Username or Password.")
                isPasswordWrong.value = true
                signInClickInProgress.value = false
            })
            if(complete) {
                signInClickInProgress.value = false
                withContext(Dispatchers.Main){ onComplete() }
            }
        }
    }

    fun onPasswordWrong() {
        viewModelScope.launch {
            delay(3000L)
            isPasswordWrong.value=false
        }
    }
}
//
//    fun onForgotPasswordClick() {
//        if (!email.isValidEmail()) {
//            SnackbarManager.showMessage("Invalid email address.")
//            return
//        }
//
//        launchCatching {
//            accountService.sendRecoveryEmail(email)
//            SnackbarManager.showMessage("Recovery instructions sent to your email.")
//        }
//    }