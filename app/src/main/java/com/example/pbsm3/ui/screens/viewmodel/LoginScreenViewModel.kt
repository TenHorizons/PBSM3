package com.example.pbsm3.ui.screens.viewmodel

import androidx.lifecycle.ViewModel
import com.example.pbsm3.ui.MainState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Hil
class LoginScreenViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LoginScreenState())
    val uiState: StateFlow<LoginScreenState> = _uiState.asStateFlow()
}