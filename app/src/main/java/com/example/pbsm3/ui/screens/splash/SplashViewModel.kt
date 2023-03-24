package com.example.pbsm3.ui.screens.splash

import androidx.compose.runtime.mutableStateOf
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

    fun onAppStart(onStartupComplete: () -> Unit) {
        showError.value = false
        if (accountService.hasUser) onStartupComplete()
        else createAnonymousAccount(onStartupComplete)
    }

    private fun createAnonymousAccount(onStartupComplete: () -> Unit) {
        launchCatching(snackbar = false) {
            try {
                accountService.createAnonymousAccount()
            } catch (ex: FirebaseAuthException) {
                showError.value = true
                throw ex
            }
            onStartupComplete()
        }
    }
}