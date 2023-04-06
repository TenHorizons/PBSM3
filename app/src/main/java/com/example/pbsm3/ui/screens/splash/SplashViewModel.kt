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

private const val TAG = "SplashViewModel"

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    logService: LogService
) : CommonViewModel(logService) {
    val showError = mutableStateOf(false)
    val errorMessage = mutableStateOf("")

    fun onAppStart(onStartupComplete: () -> Unit) {
        showError.value = false
        errorMessage.value=""
        viewModelScope.launch {
            if (!userRepository.hasUser()) {
                showError.value = true
                errorMessage.value = "user was not saved in user repository."
            }
            else {
                loadUserData()
            }
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
        Log.d(TAG, "user data loaded. User: ${userRepository.currentUser}")
    }

 /*   /**Calls account service to create an anonymous account, then
     * saves a user document into the collection.
     * The authentication does not add a document to the
     * user collection, so this method also
     * initializes the default fields in a user object*/
    private suspend fun createAnonymousAccount() {
        try {
            accountService.createAnonymousAccount()
        } catch (ex: FirebaseAuthException) {
            showError.value = true
            throw ex
        } catch (exc: Exception) {
            throw exc
        }

    }*/
}