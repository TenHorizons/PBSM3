package com.example.pbsm3.ui.commonScreenComponents.datepicker

import java.time.LocalDate

data class DatePickerState(
    val pickerExpanded:Boolean = false,
    val selectedDate: LocalDate = LocalDate.now(),
    val previousEnabled:Boolean = true,
    val nextEnabled:Boolean = true,
    val displayMode: PickerDisplayMode = PickerDisplayMode.CALENDAR
)