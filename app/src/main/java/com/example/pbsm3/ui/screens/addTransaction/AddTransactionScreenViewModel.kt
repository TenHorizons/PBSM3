package com.example.pbsm3.ui.screens.addTransaction

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.pbsm3.data.defaultAccounts
import com.example.pbsm3.data.defaultCategories
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.StorageService
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import javax.inject.Inject

private const val TAG = "AddTransactionViewModel"

@HiltViewModel
class AddTransactionScreenViewModel @Inject constructor(
    private val transactionRepository: StorageService,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(AddTransactionScreenState())
    fun onAmountChange(newValue: String, switchGreen: Boolean): String {
        //TODO Add wrong input detection (non-numbers)
        val amount =
            if (newValue.startsWith("0")) 0.0
            else newValue.toDouble() / 100
        if (!switchGreen) amount * (-1)
        uiState.value = uiState.value.copy(
            amount = amount
        )
        return if (newValue.startsWith("0")) ""
        else newValue
    }

    fun onCategoryChange(categoryName: String) {
        uiState.value = uiState.value.copy(
            //TODO: convert category name to selectedCategory
            selectedCategory = categoryName
        )
    }

    fun onAccountChange(accountName: String) {
        uiState.value = uiState.value.copy(
            //TODO: convert account name to selectedAccount
            selectedAccount = accountName
        )
    }

    fun onDateChange(newDate: LocalDate) {
        uiState.value = uiState.value.copy(selectedDate = newDate)
    }

    fun onMemoChange(newValue: String) {
        uiState.value = uiState.value.copy(memoText = newValue)
    }

    fun getCategoryOptions(): List<String> {
        //TODO: get options from Firebase map eventually
        return defaultCategories.map { category -> category.name }.toList()
    }

    fun getAccountOptions(): List<String> {
        //TODO: get options from Firebase map eventually
        return defaultAccounts.map { account -> account.name }.toList()
    }

    fun onAddTransaction() {
        //TODO: check if all required info is present, then save to Firebase.
        // Also, update account balance with new transaction.
        Log.d(
            TAG, "onAdd Transaction called. State:\n" +
                    "amount: ${uiState.value.amount}\n" +
                    "selectedCategory: ${uiState.value.selectedCategory}\n" +
                    "selectedAccount: ${uiState.value.selectedAccount}\n" +
                    "selectedDate: ${uiState.value.selectedDate}\n" +
                    "memoText: ${uiState.value.memoText}\n")
    }
}