package com.example.pbsm3.data

import com.example.pbsm3.model.Budget
import com.example.pbsm3.model.Category
import com.example.pbsm3.model.BudgetItem
import java.text.DateFormatSymbols
import java.util.Date
import java.util.Calendar

fun getFirstDayOfMonth():Date{
    val calendar: Calendar = Calendar.getInstance()
    calendar.set(Calendar.DAY_OF_MONTH,1)
    return calendar.time
}
//month indexes 0~11
val shortMonthStrings = DateFormatSymbols.getInstance().shortMonths.asList()
val validMonthRange:IntRange = shortMonthStrings.indices

private val validYearRange: IntRange = IntRange(
    start = Calendar.getInstance().get(Calendar.YEAR).minus(10),
    endInclusive = Calendar.getInstance().get(Calendar.YEAR).plus(11)
)

val defaultBudgetItem = BudgetItem("Groceries&Laundry")

val defaultCategories = listOf(
    Category(
        name = "Immediate Obligations",
        items = listOf(
            BudgetItem("Groceries&Laundry"),
            BudgetItem("Internet"),
            BudgetItem("Electric"),
            BudgetItem("Water"),
            BudgetItem("Rent/Mortgage"),
            BudgetItem("Monthly Software Subscriptions"),
            BudgetItem("Interest & Fees")
        )
    ),
    Category(
        name = "True Expenses",
        items = listOf(
            BudgetItem("Emergency Fund"),
            BudgetItem("Auto Maintenance"),
            BudgetItem("Home Maintenance"),
            BudgetItem("Renter's/Home Insurance"),
            BudgetItem("Medical"),
            BudgetItem("Clothing"),
            BudgetItem("Gifts"),
            BudgetItem("Computer Replacement"),
            BudgetItem("Annual Software Subscriptions"),
            BudgetItem("Stuff I Forgot to Budget For"),
        )
    ),
    Category(
        name = "Debt Payments",
        items = listOf(
            BudgetItem("Student Loan"),
            BudgetItem("Auto Loan")
        )
    ),
    Category(
        name = "Quality of Life Goals",
        items = listOf(
            BudgetItem("Investments"),
            BudgetItem("Vacation"),
            BudgetItem("Fitness"),
            BudgetItem("Education")
        )
    ),
    Category(
        name = "Just for Fun",
        items = listOf(
            BudgetItem("Dining Out"),
            BudgetItem("Gaming"),
            BudgetItem("Music"),
            BudgetItem("Fun Money")
        )
    )
)

/**Remember to check if current month exists
 * before using default monthly budget*/
val defaultMonthlyBudget = mapOf(
    getFirstDayOfMonth() to defaultCategories
)

/**Ensure budget names do not repeat in an account.*/
val defaultBudget = Budget(
    name = "Default Budget",
    monthlyBudgets = defaultMonthlyBudget
)