package com.example.pbsm3.ui.screens.addTransaction

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.pbsm3.data.UNASSIGNED
import com.example.pbsm3.data.fromDigitString
import com.example.pbsm3.data.isZero
import com.example.pbsm3.firebaseModel.Account
import com.example.pbsm3.firebaseModel.BudgetItem
import com.example.pbsm3.firebaseModel.Transaction
import com.example.pbsm3.firebaseModel.Unassigned
import com.example.pbsm3.firebaseModel.service.LogService
import com.example.pbsm3.firebaseModel.service.repository.Carryover
import com.example.pbsm3.firebaseModel.service.repository.Repository
import com.example.pbsm3.firebaseModel.service.repository.UserRepository
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import java.io.FileNotFoundException
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "AddTransactionViewModel"

@HiltViewModel
class AddTransactionScreenViewModel @Inject constructor(
    private val transactionRepository: Repository<Transaction>,
    private val accountRepository: Repository<Account>,
    private val budgetItemRepository: Repository<BudgetItem>,
    private val unassignedRepository: Repository<Unassigned>,
    private val unassignedCarryover: Carryover<Unassigned>,
    private val budgetItemCarryover: Carryover<BudgetItem>,
    private val userRepository: UserRepository,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(AddTransactionScreenState())

    private val NO_ACCOUNTS = "No Accounts!"

    //TODO doing too much on main thread when loading transaction screen.
    // see what processes to optimize or move to default thread.

    fun getCategoryOptions(): List<String> {
        //category names from user repo plus unassigned.
        if(uiState.value.categoryOptions.isEmpty()){
            uiState.value = uiState.value.copy(
                categoryOptions = listOf(UNASSIGNED) + userRepository.getBudgetItemNames(),
                selectedCategoryName = UNASSIGNED
            )
        }

        return uiState.value.categoryOptions
    }

    fun getAccountOptions(): List<String> {
        if(uiState.value.accountOptions.isEmpty()){
            val options = userRepository.getAccountNames().toMutableList()
            if(options.isEmpty()) options.add(NO_ACCOUNTS)
            uiState.value = uiState.value.copy(
                accountOptions = options,
                selectedAccountName = options.first()
            )
        }
        return uiState.value.accountOptions
    }

    fun onAddTransaction(onError: (Exception) -> Unit, onComplete: () -> Unit) {
        Log.i(TAG, "adding transaction start")
        if(uiState.value.amount.isZero()){
            onError(IllegalArgumentException("Please enter on-zero value for amount!"))
            return
        }
        if (uiState.value.selectedAccountName == NO_ACCOUNTS) {
            onError(IllegalArgumentException("Please add an account before adding transactions!"))
            return
        }

        val account = getAccount(onError) ?: return
        Log.i(TAG, "account retrieved. account: $account")

        var assignedToObject:Any? = null
        var newTransaction:Transaction? = null
        viewModelScope.launch(Dispatchers.Default + NonCancellable) {
            assignedToObject =
                try {
                    when (uiState.value.selectedCategoryName) {
                        UNASSIGNED -> getUnassigned(onError) ?: throw IllegalStateException(
                            "could not get Unassigned."
                        )
                        else -> getBudgetItem(onError) ?: throw IllegalStateException(
                            "could not get Budget Item."
                        )
                    }
                } catch (ex: Exception) {
                    Log.e(TAG,"error getting assignedTo obj. error:\n$ex")
                    onError(ex)
                    this.cancel()
                }
            Log.i(TAG, "assigned to object retrieved. obj: $assignedToObject")
            newTransaction = Transaction()
        }.invokeOnCompletion {
            viewModelScope.launch(Dispatchers.Default + NonCancellable) {
                Log.i(TAG, "transaction construction start.")
                try {
                    newTransaction = newTransaction!!.getUiStateValues(uiState)
                        .getTransactionRefBySaving(transactionRepository)
                        .getAccountRef(account)
                        .getAssignedToRef(assignedToObject!!)
                } catch (ex: Exception) {
                    Log.e(TAG, "error in transaction construction. Error: \n$ex")
                    this.cancel()
                }
            }.invokeOnCompletion {
                Log.i(
                    TAG,
                    "transaction constructed. processing start. transaction: \n$newTransaction"
                )
                viewModelScope.launch(Dispatchers.Default + NonCancellable) {
                    transactionRepository.saveLocalData(newTransaction!!)
                }.invokeOnCompletion { onComplete() }
            }
        }
    }

    private fun Transaction.getUiStateValues(
        uiState: MutableState<AddTransactionScreenState>
    ): Transaction {
        val amount = if(uiState.value.isSwitchGreen){
            uiState.value.amount
        }else{
            uiState.value.amount.multiply(BigDecimal("-1"))
        }
        return this.copy(
            amount = amount,
            date = uiState.value.selectedDate,
            memo = uiState.value.memoText
        )
    }

    private suspend fun Transaction.getTransactionRefBySaving(
        transRepo: Repository<Transaction>
    ): Transaction {
        Log.i(TAG,"getting transaction ref.")
        var ref = ""
        val transaction = this
        withContext(NonCancellable) {
            ref = transRepo.saveData(transaction, onError = {
                throw FileNotFoundException("failed to get transaction ref.")
            })
        }
        Log.i(TAG,"transaction ref get. ref: $ref")
        return transaction.copy(id = ref)
    }

    private fun Transaction.getAccountRef(account: Account): Transaction =
        this.copy(accountRef = account.id)

    private fun Transaction.getAssignedToRef(assignedToObject: Any): Transaction {
        return when (assignedToObject) {
            is Unassigned -> this.copy(assignedTo_Ref = assignedToObject.id)
            is BudgetItem -> this.copy(assignedTo_Ref = assignedToObject.id)
            else -> throw IllegalStateException(
                "assignedToObject is not in the correct state! object: $assignedToObject"
            )
        }
    }

    private fun getAccount(onError: (Exception) -> Unit): Account? {
        Log.i(TAG,"addTransaction getAccount start.")
        val accountList = accountRepository.getListByDate(uiState.value.selectedDate)
        val accounts = accountList.filter { it.name == uiState.value.selectedAccountName }
        if (accounts.size > 1) {
            Log.e(TAG, "onAddTransaction getAccount found > 1 account.")
            onError(java.lang.IllegalStateException("found > 1 account."))
            return null
        }
        if (accounts.isEmpty()) {
            Log.e(TAG, "onAddTransaction getAccount found no matching accounts")
            onError(java.lang.IllegalStateException("found no matching accounts."))
            return null
        }
        Log.i(TAG,"addTransaction getAccount completed. account: ${accounts.first()}")
        return accounts.first()
    }

    private suspend fun getUnassigned(onError: (Exception) -> Unit): Unassigned? {
        Log.i(TAG,"addTransaction getUnassigned start.")
        val unassignedList = unassignedRepository.getListByDate(uiState.value.selectedDate)
        if (unassignedList.size > 1) {
            Log.e(TAG, "onAddTransaction getUnassigned found > 1 unassigned")
            onError(java.lang.IllegalStateException("found > 1 unassigned. Check for error"))
            return null
        }
        if (unassignedList.isEmpty()) {
            Log.d(TAG, "onAddTransaction getUnassigned found no matching Unassigned.")
            unassignedCarryover.createAndSaveNewItem(Unassigned(date = uiState.value.selectedDate))
        }
        Log.i(TAG,"addTransaction getUnassignedCompleted. Unassigned: ${unassignedList.first()}")
        return unassignedList.first()
    }

    private suspend fun getBudgetItem(onError: (Exception) -> Unit): BudgetItem? {
        Log.i(TAG,"addTransaction getBudgetItem start.")
        val budgetItemList = budgetItemRepository.getListByDate(uiState.value.selectedDate)
        val budgetItems = budgetItemList.filter { it.name == uiState.value.selectedCategoryName }
        if (budgetItems.size > 1) {
            Log.e(TAG, "onAddTransaction found > 1 budget items. Check for error")
            onError(java.lang.IllegalStateException("found > 1 budget items. Check for error"))
            return null
        }
        if (budgetItems.isEmpty()) {
            Log.e(TAG, "onAddTransaction::getBudgetItem. budgetItems empty!")
            budgetItemCarryover.createAndSaveNewItem(BudgetItem(date = uiState.value.selectedDate))
        }
        Log.i(TAG,"addTransaction getBudgetItem completed. Item: ${budgetItems.first()}")
        return budgetItems.first()
    }

    fun onIsSwitchGreenChanged(newValue:Boolean){
        uiState.value = uiState.value.copy(isSwitchGreen = newValue)
    }

    fun onAmountChange(newValue: String, switchGreen: Boolean): String {
        //TODO Add wrong input detection (non-numbers)
        val amount =
            if (newValue.startsWith("0")) BigDecimal.ZERO
            else newValue.fromDigitString()
        if (!switchGreen) amount.multiply(BigDecimal("-1"))
        uiState.value = uiState.value.copy(
            amount = amount
        )
        return if (newValue.startsWith("0")) ""
        else newValue
    }

    fun onCategoryChange(categoryName: String) {
        uiState.value = uiState.value.copy(
            selectedCategoryName = categoryName
        )
    }

    fun onAccountChange(accountName: String) {
        uiState.value = uiState.value.copy(
            selectedAccountName = accountName
        )
    }

    fun onDateChange(newDate: LocalDate) {
        uiState.value = uiState.value.copy(selectedDate = newDate)
    }

    fun onMemoChange(newValue: String) {
        uiState.value = uiState.value.copy(memoText = newValue)
    }

    fun hideAfterDelay(onComplete: () -> Unit) {
        viewModelScope.launch {
            delay(3000L)
            onComplete()
        }
    }
}