package com.example.pbsm3.ui

import com.example.pbsm3.data.getFirstDayOfMonth
import com.example.pbsm3.ui.navhost.Screen
import java.time.LocalDate

data class MainState(
    val selectedDate: LocalDate = getFirstDayOfMonth(),
    val currentScreen: Screen = Screen.Login
)