package com.example.pbsm3.model

import com.example.pbsm3.data.defaultMonthlyBudget
import java.util.Date

data class User(
    val username:String,
    val password:String,
    val budgets: List<Budget>
)

data class Budget(
    val name:String,
    val monthlyBudgets:Map<Date,List<Category>> = defaultMonthlyBudget
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