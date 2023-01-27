package com.example.pbsm3.model

import java.util.Date

data class Transaction(
    //TODO: [optional] add payee, repeat, cleared, and flag
    val amount:Double,
    val category:String,
    val account: Account,
    val date:Date,
    val memo:String =""
)
data class Account(
    val name:String,
    val balance:Double = 0.0
)