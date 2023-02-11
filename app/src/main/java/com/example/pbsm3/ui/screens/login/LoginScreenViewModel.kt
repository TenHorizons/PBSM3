package com.example.pbsm3.ui.screens.login

import androidx.compose.runtime.mutableStateOf
import com.example.pbsm3.model.service.AccountService
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.ui.commonScreenComponents.isValidEmail
import com.example.pbsm3.ui.commonScreenComponents.snackbar.SnackbarManager
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginScreenViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(LoginScreenState())
        private set

    private val email
        get() = uiState.value.email
    private val password
        get() = uiState.value.password

    fun onEmailChange(newValue: String) {
        uiState.value = uiState.value.copy(email = newValue)
    }

    fun onPasswordChange(newValue: String) {
        uiState.value = uiState.value.copy(password = newValue)
    }

    fun onSignInClick(openAndPopUp: (String, String) -> Unit) {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage("Please insert a valid email.")
            return
        }

        if (password.isBlank()) {
            SnackbarManager.showMessage("Password cannot be empty.")
            return
        }

        launchCatching {
            accountService.authenticate(email, password)
            openAndPopUp("SettingsScreen", "LoginScreen")
        }
    }

    fun onForgotPasswordClick() {
        if (!email.isValidEmail()) {
            SnackbarManager.showMessage("Invalid email address.")
            return
        }

        launchCatching {
            accountService.sendRecoveryEmail(email)
            SnackbarManager.showMessage("Recovery instructions sent to your email.")
        }
    }
}