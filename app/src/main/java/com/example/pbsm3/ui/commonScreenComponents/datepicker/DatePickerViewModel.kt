package com.example.pbsm3.ui.commonScreenComponents.datepicker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class DatePickerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DatePickerState())
    val uiState: StateFlow<DatePickerState> = _uiState.asStateFlow()

    fun expandPicker() {
        _uiState.update { currentState ->
            currentState.copy(
                pickerExpanded = true
            )
        }
    }

    fun collapsePicker() {
        _uiState.update { currentState ->
            currentState.copy(
                pickerExpanded = false
            )
        }
    }

    fun setSelectedDate(date: LocalDate) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedDate = date
            )
        }
    }
}