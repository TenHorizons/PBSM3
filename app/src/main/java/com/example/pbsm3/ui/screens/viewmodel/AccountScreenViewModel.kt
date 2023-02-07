package com.example.pbsm3.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AccountScreenViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AccountScreenState())
    val uiState: StateFlow<AccountScreenState> = _uiState.asStateFlow()
}