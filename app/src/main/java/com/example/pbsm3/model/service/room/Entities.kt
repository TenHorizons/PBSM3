package com.example.pbsm3.model.service.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: String = "",
    val name:String ="",
    val totalCarryover: BigDecimal = BigDecimal("0"),//TODO need to convert to primitive
    val totalExpenses:BigDecimal = BigDecimal("0"),
    val totalBudgeted:BigDecimal = BigDecimal("0"),
    val date: LocalDate = LocalDate.now(),
    val budgetItemsRef:List<String> = listOf(),
    val position: Int = 0
)