package com.example.pbsm3.roomModel.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity(tableName = "category")
data class CategoryRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    val name:String ="",
    val totalCarryover: String = "",
    val totalExpenses: String = "",
    val totalBudgeted: String = "",
    val date: String = "",
    val budgetItemsIds:String = "",
    val position: Int = 0
)

data class CategoryBeforeRoom(
    val id: Int = -1,
    val name:String ="",
    val totalCarryover: BigDecimal = BigDecimal("0"),
    val totalExpenses:BigDecimal = BigDecimal("0"),
    val totalBudgeted:BigDecimal = BigDecimal("0"),
    val date: LocalDate = LocalDate.now(),
    val budgetItemsIds:List<Int> = listOf(),
    val position: Int = 0
)

fun CategoryBeforeRoom.toRoomFormat(): CategoryRoom = CategoryRoom(
    id,
    name,
    totalCarryover.toString(),
    totalExpenses.toString(),
    totalBudgeted.toString(),
    date.toString(),
    budgetItemsIds.joinToString(separator=","),
    position
)

fun CategoryRoom.toNormalFormat(): CategoryBeforeRoom = CategoryBeforeRoom(
    id,
    name,
    totalBudgeted.toBigDecimal(),
    totalExpenses.toBigDecimal(),
    totalCarryover.toBigDecimal(),
    LocalDate.parse(date),
    budgetItemsIds.split(",").map{it.toInt()},
    position
)