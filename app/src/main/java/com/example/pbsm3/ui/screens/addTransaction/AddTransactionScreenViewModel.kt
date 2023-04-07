package com.example.pbsm3.ui.screens.addTransaction

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.example.pbsm3.data.fromDigitString
import com.example.pbsm3.model.*
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.repository.Repository
import com.example.pbsm3.model.service.repository.UserRepository
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "AddTransactionViewModel"

@HiltViewModel
class AddTransactionScreenViewModel @Inject constructor(
    private val transactionRepository: Repository<Transaction>,
    private val accountRepository: Repository<Account>,
    private val categoryRepository: Repository<NewCategory>,
    private val budgetItemRepository: Repository<NewBudgetItem>,
    private val unassignedRepository: Repository<Unassigned>,
    private val userRepository: UserRepository,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(AddTransactionScreenState())
    private val _unassigned = "Unassigned"

    fun getCategoryOptions(): List<String> {
        //category names from user repo plus unassigned.
        val toDisplay: MutableList<String> = mutableListOf()

        toDisplay.add(_unassigned)
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
        //convert names to objects.
        if(uiState.value.selectedAccountName == "No accounts!") {
            onError(IllegalArgumentException("Please add an account before adding transactions!"))
            onComplete()
            return
        }

        val categoryList = categoryRepository.getListByDate(uiState.value.selectedDate)

        if (categoryList.isEmpty()) {
            viewModelScope.launch(NonCancellable) {
                userRepository.generateDefaultDataFromMap(
                    date = uiState.value.selectedDate,
                    onError = onError
                )
            }.invokeOnCompletion {
                onAddTransaction(onError,onComplete)
                return@invokeOnCompletion
            }
        }else{

        }
        val accountList = accountRepository.getListByDate(uiState.value.selectedDate)
        val selectedCategoryRef =
            if (uiState.value.selectedCategoryName == _unassigned)
                unassignedRepository.getListByDate(uiState.value.selectedDate).first().id
            else
                categoryList.first { it.name == uiState.value.selectedCategoryName }.id
        val selectedAccountRef =
            accountList.first { it.name == uiState.value.selectedAccountName }.id
        val newTransaction = Transaction(
            amount = uiState.value.amount,
            date = uiState.value.selectedDate,
            memo = uiState.value.memoText,
            accountRef = selectedAccountRef,
            assignedTo_Ref = selectedCategoryRef
        )
        Log.d(TAG, "onAdd Transaction called. newTransaction: $newTransaction")
        transactionRepository.updateLocalData(newTransaction)
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