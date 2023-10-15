package com.example.pbsm3.ui.screens.accountTransactions

import androidx.compose.runtime.mutableStateOf
import com.example.pbsm3.data.ALL_ACCOUNTS
import com.example.pbsm3.firebaseModel.Account
import com.example.pbsm3.firebaseModel.BudgetItem
import com.example.pbsm3.firebaseModel.Transaction
import com.example.pbsm3.firebaseModel.Unassigned
import com.example.pbsm3.firebaseModel.service.LogService
import com.example.pbsm3.firebaseModel.service.repository.Repository
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
class AccountTransactionsScreenViewModel @Inject constructor(
    private val accountRepository: Repository<Account>,
    private val transactionsRepository: Repository<Transaction>,
    private val budgetItemRepo: Provider<Repository<BudgetItem>>,
    private val unassignedRepo: Provider<Repository<Unassigned>>,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(AccountTransactionsScreenState())

    private val budgetItemRepository: Repository<BudgetItem>
        get() = budgetItemRepo.get()
    private val unassignedRepository: Repository<Unassigned>
        get() = unassignedRepo.get()

    fun loadTransactions(accountRef: String) {
        val transactions: List<Transaction> =
            if (accountRef == ALL_ACCOUNTS) {
                transactionsRepository.getAll()
            } else {
                transactionsRepository.getListByRef(accountRef)
            }
        uiState.value = uiState.value.copy(
            accountRef = accountRef,
            transactions = transactions
        )
    }

    fun getAccountNameByRef(accountRef: String): String {
        return accountRepository.getByRef(accountRef).name
    }

    //adding listener in case the screen isn't destroyed.
    fun addListeners(){
        val removeTransactionRepoListener =
            transactionsRepository.onItemsChanged {
                loadTransactions(uiState.value.accountRef)
            }
        this.addCloseable { removeTransactionRepoListener() }
    }

    fun getAssignedToNameByRef(assignedToRef: String):String {
        return try{
            budgetItemRepository.getByRef(assignedToRef).name
        }catch (ex:Exception){
            try {
                unassignedRepository.getByRef(assignedToRef)
                return "Unassigned"
            }catch (exc:Exception){
                throw exc
            }
        }
    }
}