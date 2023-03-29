package com.example.pbsm3.ui.screens.addAccount

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.module.StorageService
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import javax.inject.Inject

private const val TAG = "AddAccountScreenViewModel"

@HiltViewModel
class AddAccountScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(AddAccountScreenState())

    fun onAccountNameChange(newName: String) {
        uiState.value = uiState.value.copy(accountName = newName)
    }

    fun getAccountBalance(): String {
        val balance = uiState.value.accountBalance
        return if((balance.compareTo(BigDecimal("0")) == 0)) ""
        else balance.toString()
    }

    fun onBalanceChange(newValue: String) {
        val balance = if(newValue == "") BigDecimal("0")
        else BigDecimal(newValue)
        uiState.value = uiState.value.copy(accountBalance = balance)
    }

    fun onAddAccount() {
        //TODO: add account to Firebase

        Log.d(TAG,
            "account to be added: " +
                    "name: ${uiState.value.accountName} " +
                    "balance: ${uiState.value.accountBalance}")
    }


}