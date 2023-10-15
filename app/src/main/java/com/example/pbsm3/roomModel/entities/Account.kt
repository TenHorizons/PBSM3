package com.example.pbsm3.roomModel.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(tableName = "account")
data class AccountRoom(
    @PrimaryKey(autoGenerate = true)
    val id:Int = -1,
    val name:String = "",
    val balance:String = "",
    val transactionRefs:String = "",
    val position: Int = 0
)

data class AccountBeforeRoom(
    val id:Int = -1,
    val name:String = "",
    val balance: BigDecimal = BigDecimal("0"),
    val transactionRefs:List<Int> = listOf(),
    val position:Int = 0
)

fun AccountBeforeRoom.toRoomFormat(): AccountRoom = AccountRoom(
    id,
    name,
    balance.toString(),
    transactionRefs.joinToString(separator=","),
    position
)

fun AccountRoom.toNormalFormat(): AccountBeforeRoom = AccountBeforeRoom(
    id,
    name,
    balance.toBigDecimal(),
    transactionRefs.split(",").map{it.toInt()},
    position
)