package com.example.pbsm3.data

import com.example.pbsm3.model.Account
import com.example.pbsm3.model.Transaction
import java.time.LocalDate

val defaultCategoryNames = listOf(
    "Immediate Obligations",
    "True Expenses",
    "Debt Payments",
    "Quality of Life Goals",
    "Just for Fun"
)

val defaultBudgetItemNames = listOf(
    "Groceries&Laundry", "Internet",
    "Electric", "Water",
    "Rent/Mortgage", "Monthly Software Subscriptions", "Interest & Fees",

    "Emergency Fund", "Auto Maintenance",
    "Home Maintenance", "Renter's/Home Insurance",
    "Medical", "Clothing",
    "Gifts", "Computer Replacement",
    "Annual Software Subscriptions",
    "Stuff I Forgot to Budget For",

    "Student Loan", "Auto Loan",

    "Investments", "Vacation",
    "Fitness", "Education",

    "Dining Out", "Gaming",
    "Music", "Fun Money"
)

/**Double check if correct*/
val defaultCategoryToItemNamesMap = mapOf(
    defaultCategoryNames[0] to listOf(
        defaultBudgetItemNames[0],
        defaultBudgetItemNames[1],
        defaultBudgetItemNames[2],
        defaultBudgetItemNames[3],
        defaultBudgetItemNames[4],
        defaultBudgetItemNames[5],
        defaultBudgetItemNames[6]
    ),
    defaultCategoryNames[1] to listOf(
        defaultBudgetItemNames[7],
        defaultBudgetItemNames[8],
        defaultBudgetItemNames[9],
        defaultBudgetItemNames[10],
        defaultBudgetItemNames[11],
        defaultBudgetItemNames[12],
        defaultBudgetItemNames[13],
        defaultBudgetItemNames[14],
        defaultBudgetItemNames[15],
        defaultBudgetItemNames[16]
    ),
    defaultCategoryNames[2] to listOf(
        defaultBudgetItemNames[17],
        defaultBudgetItemNames[18]
    ),
    defaultCategoryNames[3] to listOf(
        defaultBudgetItemNames[19],
        defaultBudgetItemNames[20],
        defaultBudgetItemNames[21],
        defaultBudgetItemNames[22]
    ),
    defaultCategoryNames[4] to listOf(
        defaultBudgetItemNames[23],
        defaultBudgetItemNames[24],
        defaultBudgetItemNames[25],
        defaultBudgetItemNames[26]
    )
)

const val ALL_ACCOUNTS = "All Account"

const val UNASSIGNED = "Unassigned"

//to remove___________________________________________

val defaultAccount = Account(name = "Maybank")

val defaultAccounts = listOf(
    defaultAccount,
    Account(name = "CIMB Bank"),
    Account(name = "Public Bank")
)


val defaultTransactions: List<Transaction> =
    defaultAccounts.map {
        listOf(
            Transaction(
                date = LocalDate.now()
            ),
            Transaction(
                date = LocalDate.now().minusDays(1)
            )
        )
    }.flatten()


//val defaultCategory = Category(name = "Immediate Obligations")
//val defaultCategories = listOf(
//    defaultCategory,
//    Category(name = "True Expenses"),
//    Category(name = "Debt Payments"),
//    Category(name = "Quality of Life Goals"),
//    Category(name = "Just for Fun")
//)
//val defaultBudgetItem = BudgetItem(name = "Groceries&Laundry")
//val defaultBudgetItems = listOf(
//    defaultBudgetItem,
//    BudgetItem(name = "Internet"),
//    BudgetItem(name = "Electric"),
//    BudgetItem(name = "Water"),
//    BudgetItem(name = "Rent/Mortgage"),
//    BudgetItem(name = "Monthly Software Subscriptions"),
//    BudgetItem(name = "Interest & Fees"),
//    BudgetItem(name = "Emergency Fund"),
//    BudgetItem(name = "Auto Maintenance"),
//    BudgetItem(name = "Home Maintenance"),
//    BudgetItem(name = "Renter's/Home Insurance"),
//    BudgetItem(name = "Medical"),
//    BudgetItem(name = "Clothing"),
//    BudgetItem(name = "Gifts"),
//    BudgetItem(name = "Computer Replacement"),
//    BudgetItem(name = "Annual Software Subscriptions"),
//    BudgetItem(name = "Stuff I Forgot to Budget For"),
//    BudgetItem(name = "Student Loan"),
//    BudgetItem(name = "Auto Loan"),
//    BudgetItem(name = "Investments"),
//    BudgetItem(name = "Vacation"),
//    BudgetItem(name = "Fitness"),
//    BudgetItem(name = "Education"),
//    BudgetItem(name = "Dining Out"),
//    BudgetItem(name = "Gaming"),
//    BudgetItem(name = "Music"),
//    BudgetItem(name = "Fun Money")
//)
//
//val defaultCategoryToBudgetItemsMap:Map<Category,List<BudgetItem>> = mapOf(
//    defaultCategories[0] to listOf(
//        defaultBudgetItems[0],
//        defaultBudgetItems[1],
//        defaultBudgetItems[2],
//        defaultBudgetItems[3],
//        defaultBudgetItems[4],
//        defaultBudgetItems[5],
//        defaultBudgetItems[6]
//    ),
//    defaultCategories[1] to listOf(
//        defaultBudgetItems[7],
//        defaultBudgetItems[8],
//        defaultBudgetItems[9],
//        defaultBudgetItems[10],
//        defaultBudgetItems[11],
//        defaultBudgetItems[12],
//        defaultBudgetItems[13],
//        defaultBudgetItems[14],
//        defaultBudgetItems[15],
//        defaultBudgetItems[16]
//    ),
//    defaultCategories[2] to listOf(
//        defaultBudgetItems[17],
//        defaultBudgetItems[18]
//    ),
//    defaultCategories[3] to listOf(
//        defaultBudgetItems[19],
//        defaultBudgetItems[20],
//        defaultBudgetItems[21],
//        defaultBudgetItems[22]
//    ),
//    defaultCategories[4] to listOf(
//        defaultBudgetItems[23],
//        defaultBudgetItems[24],
//        defaultBudgetItems[25],
//        defaultBudgetItems[26]
//    ),
//)
