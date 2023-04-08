package com.example.pbsm3.ui.screens.addTransaction

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.pbsm3.data.UNASSIGNED
import com.example.pbsm3.data.fromDigitString
import com.example.pbsm3.model.Account
import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.Transaction
import com.example.pbsm3.model.Unassigned
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.repository.Repository
import com.example.pbsm3.model.service.repository.UserRepository
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
    private val userRepository: UserRepository,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(AddTransactionScreenState())

    fun getCategoryOptions(): List<String> {
        //category names from user repo plus unassigned.
        val toDisplay: MutableList<String> = mutableListOf()

        toDisplay.add(UNASSIGNED)
        toDisplay.addAll(userRepository.getBudgetItemNames())

        return toDisplay
    }

    fun getAccountOptions(): List<String> {
        val options = userRepository.getAccountNames()
        return options.ifEmpty {
            uiState.value = uiState.value.copy(selectedAccountName = "No accounts!")
            listOf("No accounts!")
        }
    }

    fun onAddTransaction(onError: (Exception) -> Unit, onComplete: () -> Unit) {
        if (uiState.value.selectedAccountName == "No accounts!") {
            onError(IllegalArgumentException("Please add an account before adding transactions!"))
            return
        }

        val account = getAccount(onError) ?: return

        val assignedToObject: Any =
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
                onError(ex)
                return
            }

        val newTransaction = Transaction()

        viewModelScope.launch(Dispatchers.Default + NonCancellable) {
            newTransaction.getUiStateValues(uiState)
                .getTransactionRefBySaving(transactionRepository)
                .getAccountRef(account)
                .getAssignedToRef(assignedToObject)
        }.invokeOnCompletion {
            viewModelScope.launch(Dispatchers.Default + NonCancellable) {
                try {
                    val processAccount = async(Dispatchers.Default) {
                        processAccount(newTransaction, account, onError)
                    }
                    val processAssignedTo = when (assignedToObject) {
                        is Unassigned -> async(Dispatchers.Default) {
                            processUnassigned(
                                newTransaction, assignedToObject as Unassigned, onError)
                        }
                        is BudgetItem -> async(Dispatchers.Default) {
                            processBudgetItem(
                                newTransaction, assignedToObject as BudgetItem, onError)
                        }
                        else -> throw IllegalStateException(
                            "assignedToObject incorrect state! obj: $assignedToObject"
                        )
                    }
                    awaitAll(processAccount, processAssignedTo)
                } catch (ex: Exception) {
                    onError(ex)
                }

            }.invokeOnCompletion { onComplete() }
        }
    }

    private fun Transaction.getUiStateValues(
        uiState: MutableState<AddTransactionScreenState>
    ): Transaction =
        this.copy(
            amount = uiState.value.amount,
            date = uiState.value.selectedDate,
            memo = uiState.value.memoText
        )

    private suspend fun Transaction.getTransactionRefBySaving(
        transRepo: Repository<Transaction>
    ): Transaction {
        var ref = ""
        val transaction = this
        withContext(NonCancellable) {
            ref = transRepo.saveData(transaction, onError = {
                throw FileNotFoundException("failed to get transaction ref.")
            })
        }
        return this.copy(id = ref)
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
        val accountList = accountRepository.getListByDate(uiState.value.selectedDate)
        val accounts = accountList.filter { it.name == uiState.value.selectedAccountName }
        if (accounts.size > 1) {
            Log.e(TAG, "onAddTransaction found > 1 account. Check for error")
            onError(java.lang.IllegalStateException("found > 1 account. Check for error"))
            return null
        }
        if (accounts.isEmpty()) {
            Log.e(TAG, "onAddTransaction found no matching accounts! Check for error")
            onError(java.lang.IllegalStateException("found no matching accounts! Check for error"))
            return null
        }
        return accounts.first()
    }

    private fun getUnassigned(onError: (Exception) -> Unit): Unassigned? {
        val unassignedList = unassignedRepository.getListByDate(uiState.value.selectedDate)
        if (unassignedList.size > 1) {
            Log.e(TAG, "onAddTransaction found > 1 unassigned. Check for error")
            onError(java.lang.IllegalStateException("found > 1 unassigned. Check for error"))
            return null
        }
        if (unassignedList.isEmpty()) {
            Log.d(TAG, "onAddTransaction::getUnassigned. unassignedList empty!")
            //TODO autogenerate. Remember to add ref into object.
        }
        return unassignedList.first()
    }

    private fun getBudgetItem(onError: (Exception) -> Unit): BudgetItem? {
        val budgetItemList = budgetItemRepository.getListByDate(uiState.value.selectedDate)
        val budgetItems = budgetItemList.filter { it.name == uiState.value.selectedCategoryName }
        if (budgetItems.size > 1) {
            Log.e(TAG, "onAddTransaction found > 1 budget items. Check for error")
            onError(java.lang.IllegalStateException("found > 1 budget items. Check for error"))
            return null
        }
        if (budgetItems.isEmpty()) {
            Log.e(TAG, "onAddTransaction::getBudgetItem. budgetItems empty!")
            //TODO autogenerate. Remember to add ref into object.
        }
        return budgetItems.first()
    }

    private fun processAccount(
        newTransaction: Transaction,
        account: Account,
        onError: (Exception) -> Unit
    ) {
        val updatedAccount = account.copy(
            balance = account.balance.plus(newTransaction.amount),
            transactionRefs = account.transactionRefs + newTransaction.id
        )
        viewModelScope.launch(NonCancellable + Dispatchers.Default) {
            accountRepository.updateLocalData(updatedAccount)
        }
    }

    private suspend fun processBudgetItem(
        newTransaction: Transaction,
        budgetItem: BudgetItem,
        onError: (Exception) -> Unit
    ) {
        val updatedBudgetItem = budgetItem.copy(
            totalExpenses = budgetItem.totalExpenses.plus(newTransaction.amount)
        )
        budgetItemRepository.updateLocalData(updatedBudgetItem)
    }

    private suspend fun processUnassigned(
        newTransaction: Transaction,
        unassigned: Unassigned,
        onError: (Exception) -> Unit
    ) {
        val updatedUnassigned = unassigned.copy(
            totalExpenses = unassigned.totalExpenses.plus(newTransaction.amount)
        )
        unassignedRepository.updateLocalData(updatedUnassigned)
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