package com.example.pbsm3.ui.screens.signup

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.repository.UserRepository
import com.example.pbsm3.ui.commonScreenComponents.snackbar.SnackbarManager
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.concurrent.CancellationException
import javax.inject.Inject

@HiltViewModel
class SignUpScreenViewModel @Inject constructor(
    private val userRepo: UserRepository,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(SignUpScreenState())
        private set

    private val username
        get() = uiState.value.username
    private val password
        get() = uiState.value.password

    var signUpClickInProgress = mutableStateOf(false)
        private set
    var usernameExist = mutableStateOf(false)
    private set

    fun onUsernameChange(newValue: String) {
        uiState.value = uiState.value.copy(username = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignUpClick(onComplete: () -> Unit) {
        if (username.isBlank()) {
            SnackbarManager.showMessage("Username cannot be empty.")
            return
        }
        if (password.isBlank()) {
            SnackbarManager.showMessage("Password cannot be empty.")
            return
        }
        signUpClickInProgress.value = true
        viewModelScope.launch(Dispatchers.IO) {
            if(userRepo.isUsernameExists(username)) {
                signUpClickInProgress.value = false
                usernameExist.value = true
                SnackbarManager.showMessage("Username already exists!")
                this.cancel(cause = CancellationException("Username already Exists!"))
            } else{
                usernameExist.value = false
                userRepo.signUp(username, password, onError ={
                    signUpClickInProgress.value = false
                    SnackbarManager.showMessage("Error saving new user to database.")
                    this.cancel(cause =
                    CancellationException("Error saving new user to database."))
                })
                signUpClickInProgress.value = false
            }
        }
        viewModelScope.launch{
            while(signUpClickInProgress.value){
                delay(100L)
            }
            if(!usernameExist.value){ onComplete() }
        }
    }

    fun onUsernameExists(){
        viewModelScope.launch {
            delay(3000L)
            usernameExist.value=false
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
}
