package com.example.pbsm3.data

import com.example.pbsm3.model.*
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

fun getFirstDayOfMonth(): LocalDate {
    return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
}

fun getFirstDayOfMonth(date: LocalDate): LocalDate {
    return date.with(TemporalAdjusters.firstDayOfMonth())
}

fun setDate(oldDate: LocalDate, newDate: LocalDate): LocalDate {
    return oldDate
        .withYear(newDate.year)
        .withMonth(newDate.monthValue)
        .withDayOfMonth(newDate.dayOfMonth)
}

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

val defaultAccount = Account(name = "Default Account")

val defaultAccounts = listOf(
    defaultAccount,
    Account("Default Account 2"),
    Account("Default Account 3")
)


val defaultTransactions: List<Transaction> =
    defaultCategories.map { category ->
        defaultAccounts.map { account ->
            listOf(
                Transaction(
                    category = category.name,
                    account = account,
                    date = LocalDate.now()
                ),
                Transaction(
                    category = category.name,
                    account = account,
                    date = LocalDate.now().minusDays(1)
                )
            )
        }.flatten()
    }.flatten()

const val ALL_ACCOUNTS = "All Account"