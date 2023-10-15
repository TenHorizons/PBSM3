package com.example.pbsm3.roomModel.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity
data class BudgetItemRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    val name:String = "",
    val totalCarryover:String = "",
    val totalExpenses:String = "",
    val totalBudgeted:String = "",
    val date:String = "",
    val categoryRef: Int = -1,
    val position: Int = 0
)

data class BudgetItemBeforeRoom(
    val id: Int = -1,
    val name:String ="",
    val totalCarryover: BigDecimal = BigDecimal("0"),
    val totalExpenses: BigDecimal = BigDecimal("0"),
    val totalBudgeted: BigDecimal = BigDecimal("0"),
    var date: LocalDate = LocalDate.now(),
    val categoryRef: Int = -1,
    val position: Int = 0
)

fun BudgetItemBeforeRoom.toRoomFormat():BudgetItemRoom = BudgetItemRoom(
    id,
    name,
    totalCarryover.toString(),
    totalExpenses.toString(),
    totalBudgeted.toString(),
    date.toString(),
    categoryRef,
    position
)

fun BudgetItemRoom.toNormalFormat():BudgetItemBeforeRoom = BudgetItemBeforeRoom(
    id,
    name,
    totalCarryover.toBigDecimal(),
    totalExpenses.toBigDecimal(),
    totalBudgeted.toBigDecimal(),
    LocalDate.parse(date),
    categoryRef,
    position
)