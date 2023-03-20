package com.example.pbsm3.ui.screens.splash

import androidx.compose.runtime.mutableStateOf
import com.example.pbsm3.Screen
import com.example.pbsm3.model.service.AccountService
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.ui.screens.CommonViewModel
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val accountService: AccountService,
    logService: LogService
) : CommonViewModel(logService) {
    val showError = mutableStateOf(false)

    fun onAppStart(navigateAndPopUp: (String, String) -> Unit) {
        showError.value = false
        if (accountService.hasUser) navigateAndPopUp(Screen.Budget.name, Screen.Splash.name)
        else createAnonymousAccount(navigateAndPopUp)
    }

    private fun createAnonymousAccount(openAndPopUp: (String, String) -> Unit) {
        launchCatching(snackbar = false) {
            try {
                accountService.createAnonymousAccount()
            } catch (ex: FirebaseAuthException) {
                showError.value = true
                throw ex
            }
            openAndPopUp(Screen.Budget.name, Screen.Splash.name)
        }
    }
}