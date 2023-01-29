package com.example.pbsm3.ui.commonScreenComponents.viewmodel

import com.example.pbsm3.ui.commonScreenComponents.utils.CalendarDisplayMode
import java.util.*

data class DatePickerState(
    val pickerExpanded:Boolean = false,
    val selectedDate: Date = Calendar.getInstance().time,
    val displayedMonthIndex:Int = Calendar.getInstance().get(Calendar.MONTH),
    val displayedYear:Int = Calendar.getInstance().get(Calendar.YEAR),
    val navigationEnabled:Boolean = true,
    val previousEnabled:Boolean = true,
    val nextEnabled:Boolean = true,
    val displayMode:CalendarDisplayMode = CalendarDisplayMode.NONE
)