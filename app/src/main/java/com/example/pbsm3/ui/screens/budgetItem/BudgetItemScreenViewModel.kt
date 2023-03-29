package com.example.pbsm3.ui.screens.budgetItem

import androidx.compose.runtime.mutableStateOf
import com.example.pbsm3.model.service.LogService
import com.example.pbsm3.model.service.module.StorageService
import com.example.pbsm3.ui.screens.CommonViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BudgetItemScreenViewModel @Inject constructor(
    private val storageService: StorageService,
    logService: LogService
) : CommonViewModel(logService) {
    var uiState = mutableStateOf(BudgetItemScreenState())

}