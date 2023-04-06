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


val defaultAccount = Account(name = "Maybank")

val defaultAccounts = listOf(
    defaultAccount,
    Account(name = "CIMB Bank"),
    Account(name = "Public Bank")
)
val defaultNewCategory = NewCategory(name = "Immediate Obligations")
val defaultNewCategories = listOf(
    defaultNewCategory,
    NewCategory(name = "True Expenses", ),
    NewCategory(name = "Debt Payments"),
    NewCategory(name = "Quality of Life Goals"),
    NewCategory(name = "Just for Fun")
)

val defaultNewBudgetItem = NewBudgetItem(name = "Groceries&Laundry")

val defaultNewBudgetItems = listOf(
    defaultNewBudgetItem,
    NewBudgetItem(name = "Internet"),
    NewBudgetItem(name = "Electric"),
    NewBudgetItem(name = "Water"),
    NewBudgetItem(name = "Rent/Mortgage"),
    NewBudgetItem(name = "Monthly Software Subscriptions"),
    NewBudgetItem(name = "Interest & Fees"),
    NewBudgetItem(name = "Emergency Fund"),
    NewBudgetItem(name = "Auto Maintenance"),
    NewBudgetItem(name = "Home Maintenance"),
    NewBudgetItem(name = "Renter's/Home Insurance"),
    NewBudgetItem(name = "Medical"),
    NewBudgetItem(name = "Clothing"),
    NewBudgetItem(name = "Gifts"),
    NewBudgetItem(name = "Computer Replacement"),
    NewBudgetItem(name = "Annual Software Subscriptions"),
    NewBudgetItem(name = "Stuff I Forgot to Budget For"),
    NewBudgetItem(name = "Student Loan"),
    NewBudgetItem(name = "Auto Loan"),
    NewBudgetItem(name = "Investments"),
    NewBudgetItem(name = "Vacation"),
    NewBudgetItem(name = "Fitness"),
    NewBudgetItem(name = "Education"),
    NewBudgetItem(name = "Dining Out"),
    NewBudgetItem(name = "Gaming"),
    NewBudgetItem(name = "Music"),
    NewBudgetItem(name = "Fun Money")
)

val defaultTransactions: List<Transaction> =
    defaultAccounts.map {
        listOf(
            Transaction(
                category = "Groceries&Laundry",
                date = LocalDate.now()
            ),
            Transaction(
                category = "Rent/Mortgage",
                date = LocalDate.now().minusDays(1)
            )
        )
    }.flatten()


//There shouldn't be a default map, because user
// shouldn't have default accounts nor transactions.
//val defaultAccountsToTransactionsMap = mapOf(
//    defaultAccounts[0] to listOf(defaultTransactions[0],defaultTransactions[1]),
//    defaultAccounts[1] to listOf(defaultTransactions[2],defaultTransactions[3]),
//    defaultAccounts[2] to listOf(defaultTransactions[4],defaultTransactions[5])
//)

val defaultCategoryToBudgetItemsMap:Map<NewCategory,List<NewBudgetItem>> = mapOf(
    defaultNewCategories[0] to listOf(
        defaultNewBudgetItems[0],
        defaultNewBudgetItems[1],
        defaultNewBudgetItems[2],
        defaultNewBudgetItems[3],
        defaultNewBudgetItems[4],
        defaultNewBudgetItems[5],
        defaultNewBudgetItems[6]
    ),
    defaultNewCategories[1] to listOf(
        defaultNewBudgetItems[7],
        defaultNewBudgetItems[8],
        defaultNewBudgetItems[9],
        defaultNewBudgetItems[10],
        defaultNewBudgetItems[11],
        defaultNewBudgetItems[12],
        defaultNewBudgetItems[13],
        defaultNewBudgetItems[14],
        defaultNewBudgetItems[15],
        defaultNewBudgetItems[16]
    ),
    defaultNewCategories[2] to listOf(
        defaultNewBudgetItems[17],
        defaultNewBudgetItems[18]
    ),
    defaultNewCategories[3] to listOf(
        defaultNewBudgetItems[19],
        defaultNewBudgetItems[20],
        defaultNewBudgetItems[21],
        defaultNewBudgetItems[22]
    ),
    defaultNewCategories[4] to listOf(
        defaultNewBudgetItems[23],
        defaultNewBudgetItems[24],
        defaultNewBudgetItems[25],
        defaultNewBudgetItems[26]
    ),
)

//_______________________________________________________________________

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


const val ALL_ACCOUNTS = "All Account"