package com.example.pbsm3.roomModel.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate

@Entity(tableName = "transaction")
data class TransactionRoom(
    @PrimaryKey(autoGenerate = true)
    val id:Int = -1,
    val amount: String = "",
    val date:String = "",
    val memo:String = "",
    val accountRef:Int = -1,
    val assignedToBudgetItemRef:Int = -1
)

data class TransactionBeforeRoom(
    val id:Int = -1,
    val amount: BigDecimal = BigDecimal("0"),
    val date: LocalDate = LocalDate.now(),
    val memo:String = "",
    val accountRef:Int = -1,
    val assignedToBudgetItemRef:Int = -1
)

fun TransactionBeforeRoom.toRoomFormat(): TransactionRoom = TransactionRoom(
    id,
    amount.toString(),
    date.toString(),
    memo,
    accountRef,
    assignedToBudgetItemRef
)

fun TransactionRoom.toNormalFormat(): TransactionBeforeRoom = TransactionBeforeRoom(
    id,
    amount.toBigDecimal(),
    LocalDate.parse(date),
    memo,
    accountRef,
    assignedToBudgetItemRef
)