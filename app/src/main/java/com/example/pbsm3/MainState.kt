package com.example.pbsm3

import com.example.pbsm3.data.getFirstDayOfMonth
import java.time.LocalDate

data class MainState(
    val selectedDate: LocalDate = getFirstDayOfMonth(),
    val currentScreen: Screen = Screen.SignInScreen
)