package com.example.pbsm3.ui.screens.accounts

import androidx.compose.runtime.mutableStateOf
import com.example.pbsm3.data.defaultAccounts
import com.example.pbsm3.data.defaultTransactions
import com.example.pbsm3.model.Account
import com.example.pbsm3.model.Transaction
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.repository.Repository
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "AccountsScreenViewModel"

@HiltViewModel
class AccountScreenViewModel @Inject constructor(
    private val accountRepository: Repository<Account>,
    logService: LogService
) : CommonViewModel(logService) {
    //TODO difference between mutable state and flow?
    //Flow seems harder to handle, so is it needed?
    var uiState = mutableStateOf(AccountScreenState())

    fun onAccountsUpdated() {
        //TODO: Link to Firebase and get accounts.
        uiState.value.accounts = defaultAccounts
    }

    fun onTransactionsUpdated() {
        //TODO: Link to Firebase and get transactions.
        uiState.value.transactions = defaultTransactions
    }

    fun getAccounts(): List<Account> {
        return uiState.value.accounts
    }

    fun getTransactions(): List<Transaction> {
        return uiState.value.transactions
    }

    fun calculateAccountBalance(account:Account):Double{
        //TODO Link to Firebase to get saved balance
        return 0.0
    }

    fun readAccountRepository() {
        uiState.value = uiState.value.copy(
            //actually no date to in accounts, but reusing a function in common interface.
            accounts = accountRepository.getListByDate(LocalDate.now())
        )
    }
}