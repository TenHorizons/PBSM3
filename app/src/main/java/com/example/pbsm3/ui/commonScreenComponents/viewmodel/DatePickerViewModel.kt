package com.example.pbsm3.ui.commonScreenComponents.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DatePickerViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DatePickerState())
    val uiState: StateFlow<DatePickerState> = _uiState.asStateFlow()

    fun expandOrCollapsePicker() {
        _uiState.update { currentState ->
            currentState.copy(
                pickerExpanded = !currentState.pickerExpanded
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

    fun displayYearList() {
        //Check if already displayed, if true, return
        //if not displayed, display
    }

    fun displayMonthList() {
        //Check if already displayed, if true, return
        //if not displayed, display
    }

    fun moveToNextMonth() {
        //Move first, then check if can continue to move.
        //If cannot move, disable button.
    }

    fun moveToPreviousMonth() {
        //Move first, then check if can continue to move.
        //If cannot move, disable button.
    }

    private fun validateMonthRange(month: Int): Int {
        /*val validIndexes: IntRange = monthStrings.indices
        if (validIndexes.contains(month)) return month
        else {
            if (month < validIndexes.first) {
                if()
            } else {

            }
        }*/
        return -1
    }
}