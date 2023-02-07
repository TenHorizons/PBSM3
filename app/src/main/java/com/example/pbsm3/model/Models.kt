package com.example.pbsm3.model

import com.example.pbsm3.data.defaultMonthlyBudget
import java.time.LocalDate

data class User(
    val username:String,
    val password:String,
    val budgets: List<Budget>
)

data class Budget(
    val name:String,
    val monthlyBudgets:Map<LocalDate,List<Category>> = defaultMonthlyBudget
)

data class Category(
    val name:String,
    val items:List<BudgetItem>,
    val totalBudgeted:Double = 0.0,
    val totalAvailable:Double = 0.0
)

data class BudgetItem(
    //TODO: edit to include "leftOver, budgeted, spent" after Transaction
    // completed, then include logic for "available"
    val name:String,
    val budgeted:Double = 0.0,
    val available:Double = 0.0,
    val notes:String = ""
)

data class Transaction(
    //TODO: [optional] add payee, repeat, cleared, and flag
    val amount:Double,
    val category:String,
    val account: Account,
    val date:LocalDate,
    val memo:String =""
)
data class Account(
    val name:String,
    val balance:Double = 0.0
)