package com.example.pbsm3.data

import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.Category
import com.example.pbsm3.model.Unassigned
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

fun BigDecimal.displayTwoDecimal(): BigDecimal =
    this.setScale(2, RoundingMode.HALF_UP)
fun Unassigned.getCarryover(): BigDecimal =
    getCarryover(this.totalCarryover,this.totalExpenses,this.totalBudgeted)
fun Category.getCarryover(): BigDecimal =
    getCarryover(this.totalCarryover,this.totalExpenses,this.totalBudgeted)
fun BudgetItem.getCarryover(): BigDecimal =
    getCarryover(this.totalCarryover,this.totalExpenses,this.totalBudgeted)
private fun getCarryover(
    carryover: BigDecimal,
    expenses: BigDecimal,
    budgeted: BigDecimal
): BigDecimal = carryover.plus(expenses).plus(budgeted)

fun BigDecimal.isZero():Boolean =
    this.compareTo(BigDecimal.ZERO)==0
fun BigDecimal.isLessThanZero(): Boolean =
    this.compareTo(BigDecimal.ZERO)==-1
fun BigDecimal.toDigitString():String =
    this.multiply(BigDecimal("100")).toInt().toString()
fun String.fromDigitString(): BigDecimal =
    BigDecimal(this).divide(BigDecimal("100"))


//to remove___________________________________________________________

fun getFirstDayOfMonth(): LocalDate =
    LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
fun getFirstDayOfMonth(date: LocalDate): LocalDate =
    date.with(TemporalAdjusters.firstDayOfMonth())