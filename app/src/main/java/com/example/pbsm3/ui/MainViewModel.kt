package com.example.pbsm3.ui

import androidx.lifecycle.ViewModel
import com.example.pbsm3.ui.navhost.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class MainViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(MainState())
    val uiState: StateFlow<MainState> = _uiState.asStateFlow()

    fun setSelectedDate(newDate:LocalDate){
        _uiState.update { currentState ->
            currentState.copy(selectedDate = newDate)
        }
    }

    fun updateCurrentScreen(screen:Screen){
        _uiState.update { currentState ->
            currentState.copy(currentScreen = screen)
        }
    }
}