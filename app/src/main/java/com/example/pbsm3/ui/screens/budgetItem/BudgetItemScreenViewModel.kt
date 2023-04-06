package com.example.pbsm3.ui.screens.budgetItem

import androidx.compose.runtime.mutableStateOf
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.StorageService
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import java.math.BigDecimal
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class BudgetItemScreenViewModel @Inject constructor(
    private val dataSource: StorageService,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(BudgetItemScreenState(

    ))

    fun setupState(
        budgetItemName:String,
        totalCarryover: BigDecimal,
        totalExpenses:BigDecimal,
        totalBudgeted:BigDecimal,
        date: LocalDate
    ) {
        uiState.value =  uiState.value.copy(
            budgetItemName = budgetItemName,
            totalCarryover =  totalCarryover,
            totalExpenses =  totalExpenses,
            totalBudgeted =  totalBudgeted,
            date =  date
        )
    }

    fun getAvailable(): BigDecimal {
        val state = uiState.value
        val carryover = state.totalCarryover
        val budgeted = state.totalBudgeted
        val spent = state.totalExpenses
        return carryover.plus(budgeted).minus(spent)
    }

}