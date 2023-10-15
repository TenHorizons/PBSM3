package com.example.pbsm3.roomModel.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity
data class UnassignedRoom(
    @PrimaryKey(autoGenerate = true)
    val id: Int = -1,
    val totalCarryover:String = "",
    val totalExpenses:String = "",
    val totalBudgeted:String = "",
    val date:String = "",
)

data class UnassignedBeforeRoom(
    val id: Int = -1,
    val totalCarryover: BigDecimal = BigDecimal("0"),
    val totalExpenses: BigDecimal = BigDecimal("0"),
    val totalBudgeted: BigDecimal = BigDecimal("0"),
    val date: LocalDate = LocalDate.now()
)

fun UnassignedBeforeRoom.toRoomFormat(): UnassignedRoom = UnassignedRoom(
    id,
    totalCarryover.toString(),
    totalExpenses.toString(),
    totalBudgeted.toString(),
    date.toString()
)

fun UnassignedRoom.toNormalFormat(): UnassignedBeforeRoom = UnassignedBeforeRoom(
    id,
    totalCarryover.toBigDecimal(),
    totalExpenses.toBigDecimal(),
    totalBudgeted.toBigDecimal(),
    LocalDate.parse(date)
)