package com.example.pbsm3.ui.screens.accountTransactions

import androidx.compose.runtime.mutableStateOf
import com.example.pbsm3.model.Account
import com.example.pbsm3.model.Transaction
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.repository.Repository
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountTransactionsScreenViewModel @Inject constructor(
    private val accountRepository: Repository<Account>,
    private val transactionsRepository: Repository<Transaction>,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(AccountTransactionsScreenState())

    fun loadTransactions(accountRef:String) {
//        if(accountRef == ALL_ACCOUNTS)
        //TODO add fun getAll() to interface
        //TODO add fun getListByRef() to interface
//        val transactionRefs = transactionsRepository.
        uiState.value = uiState.value.copy(transactions = listOf())
    }

    fun getAccountNameByRef(accountRef: String):String {
        return accountRepository.getByRef(accountRef).name
    }
}