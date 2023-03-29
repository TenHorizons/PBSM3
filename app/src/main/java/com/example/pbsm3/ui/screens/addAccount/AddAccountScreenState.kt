package com.example.pbsm3.ui.screens.addAccount

import java.math.BigDecimal

data class AddAccountScreenState(
    val accountName:String="",
    val accountBalance:BigDecimal = BigDecimal("0.0000")
)