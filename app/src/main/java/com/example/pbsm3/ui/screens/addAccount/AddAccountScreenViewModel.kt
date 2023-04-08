package com.example.pbsm3.ui.screens.addAccount

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.pbsm3.data.isZero
import com.example.pbsm3.model.Account
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.repository.Repository
import com.example.pbsm3.model.service.repository.UserRepository
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

private const val TAG = "AddAccountScreenViewModel"

@HiltViewModel
class AddAccountScreenViewModel @Inject constructor(
    private val accountRepository: Repository<Account>,
    private val userRepository: UserRepository,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(AddAccountScreenState())

    fun getAccountBalance(): String {
        val balance = uiState.value.accountBalance
        return if(balance.isZero()) ""
        else balance.toString()
    }

    fun onAddAccount(onError: (Exception) -> Unit, onComplete: () -> Unit) {
        if(uiState.value.accountName.isBlank() || uiState.value.accountBalance.isZero()){
            onError(IllegalArgumentException("Please input values for account name and balance."))
            return
        }

        Log.d(TAG, "account to be added: \n" +
                "name: ${uiState.value.accountName} \n" +
                "balance: ${uiState.value.accountBalance}")

        val newAccount = Account(
            name = uiState.value.accountName,
            balance = uiState.value.accountBalance
        )

        viewModelScope.launch(NonCancellable) {
            val accountRef = accountRepository.saveData(newAccount, onError = onError)
            val firestoredAccount = newAccount.copy(id=accountRef)
            accountRepository.saveLocalData(firestoredAccount)
            userRepository.addNewAccount(firestoredAccount)
            onComplete()
        }
    }

    fun onAccountNameChange(newName: String) {
        uiState.value = uiState.value.copy(accountName = newName)
    }

    fun onBalanceChange(newValue: String) {
        val balance = if(newValue == "") BigDecimal.ZERO
        else BigDecimal(newValue)
        uiState.value = uiState.value.copy(accountBalance = balance)
    }

    fun hideAfterDelay(onComplete: () -> Unit) {
        viewModelScope.launch {
            delay(2000L)
            onComplete()
        }
    }
}