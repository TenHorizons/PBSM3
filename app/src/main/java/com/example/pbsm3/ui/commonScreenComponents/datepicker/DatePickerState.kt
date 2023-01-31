package com.example.pbsm3.ui.commonScreenComponents.datepicker

import com.example.pbsm3.data.getFirstDayOfMonth
import java.time.LocalDate

data class DatePickerState(
    val pickerExpanded:Boolean = false,
    val selectedDate: LocalDate = getFirstDayOfMonth(),
    val previousEnabled:Boolean = true,
    val nextEnabled:Boolean = true,
    val displayMode: PickerDisplayMode = PickerDisplayMode.CALENDAR
)