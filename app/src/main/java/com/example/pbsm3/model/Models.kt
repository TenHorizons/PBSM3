package com.example.pbsm3.model

import com.example.pbsm3.data.defaultMonthlyBudget
import com.google.firebase.firestore.DocumentId
import java.math.BigDecimal
import java.time.LocalDate

data class SignUser(
    @DocumentId val id: String = "",
    val username:String = "",
    val password:String = "",
    val categoryRefs:List<String> = listOf(),
    val accountRefs:List<String> = listOf(),
    val budgetItemRefs:List<String> = listOf(),
    val transactionRefs:List<String> = listOf(),
    val availableRefs:List<String> = listOf(),
    val accountNames:List<String> = listOf(),
    val categoryNames:List<String> = listOf(),
    val budgetItemNames:List<String> = listOf()
)

/**assignedTo_Ref should be a budget item or unassigned.*/
data class Transaction(
    @DocumentId val id:String = "",
    val amount: BigDecimal = BigDecimal("0"),
    val date:LocalDate = LocalDate.now(),
    val memo:String = "",
    val accountRef:String = "",
    val assignedTo_Ref:String = ""
)
data class Account(
    @DocumentId val id:String = "",
    val name:String = "",
    val balance:BigDecimal = BigDecimal("0"),
    val transactionRefs:List<String> = listOf()
)

/**Previous name was Available. If found any change it.*/
data class Unassigned(
    @DocumentId val id: String = "",
    val totalCarryover:BigDecimal = BigDecimal("0"),
    val totalExpenses:BigDecimal = BigDecimal("0"),
    val totalBudgeted:BigDecimal = BigDecimal("0"),
    val date:LocalDate = LocalDate.now()
)

data class NewCategory(
    @DocumentId val id: String = "",
    val name:String ="",
    val totalCarryover:BigDecimal = BigDecimal("0"),
    val totalExpenses:BigDecimal = BigDecimal("0"),
    val totalBudgeted:BigDecimal = BigDecimal("0"),
    val date:LocalDate = LocalDate.now(),
    val budgetItemsRef:List<String> = listOf()
)

data class NewBudgetItem(
    @DocumentId val id: String = "",
    val name:String ="",
    val totalCarryover:BigDecimal = BigDecimal("0"),
    val totalExpenses:BigDecimal = BigDecimal("0"),
    val totalBudgeted:BigDecimal = BigDecimal("0"),
    var date:LocalDate = LocalDate.now(),
    val categoryRef: String = ""
)

data class FirestoreTransaction(
    @DocumentId val id:String = "",
    val amount: String = "",
    val date:String = LocalDate.now().toString(),
    val memo:String = "",
    val accountRef:String = "",
    val assignedTo_Ref:String = ""
)

data class FirestoreUnassigned(
    @DocumentId val id: String = "",
    val totalCarryover:String = "",
    val totalExpenses:String = "",
    val totalBudgeted:String = "",
    val date:String = LocalDate.now().toString(),
)

data class FirestoreCategory(
    @DocumentId val id: String = "",
    val name:String = "",
    val totalCarryover:String = "",
    val totalExpenses:String = "",
    val totalBudgeted:String = "",
    val date:String = LocalDate.now().toString(),
    val budgetItemsRef:List<String> = listOf()
)

data class FirestoreAccount(
    @DocumentId val id:String = "",
    val name:String = "",
    val balance:String = "",
    val transactionRefs:List<String> = listOf()
)

data class FirestoreBudgetItem(
    @DocumentId val id: String = "",
    val name:String = "",
    val totalCarryover:String = "",
    val totalExpenses:String = "",
    val totalBudgeted:String = "",
    val date:String = LocalDate.now().toString(),
    val categoryRef: String = ""
)
//_____________________________________________________

data class User(
    val id: String = "",
    val isAnonymous: Boolean = true,
    val categoryRefs:List<String> = listOf(),
    val accountRefs:List<String> = listOf(),
    val budgetItemRefs:List<String> = listOf(),
    val transactionRefs:List<String> = listOf()
)

data class Budget(
    val name:String,
    val monthlyBudgets:Map<LocalDate,List<Category>> = defaultMonthlyBudget
)

data class Category(
    val name:String,
    val items:List<BudgetItem>,
    val totalBudgeted:Double = 0.0,
    val totalAvailable:Double = 0.0
)

data class BudgetItem(
    //TODO: edit to include "leftOver, budgeted, spent" after Transaction
    // completed, then include logic for "available"
    val name:String,
    val budgeted:Double = 0.0,
    val available:Double = 0.0,
    val memo:String = ""
)

/*import com.google.firebase.firestore.DocumentId
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

data class User(
    val id: String = "",
    val isAnonymous: Boolean = true,
)

val budgetName:String = "Default Budget"

data class Category(
    @DocumentId val id: String = "",
    val name:String,
    val totalCarryover:BigDecimal = BigDecimal("0"),
    val totalExpenses:BigDecimal = BigDecimal("0"),
    val totalBudgeted:BigDecimal = BigDecimal("0"),
    val date:LocalDate = LocalDate.now(),
    val budgetItemsRef:List<String> = listOf()
)
data class BudgetItem(
    @DocumentId val id: String = "",
    val name:String,
    val totalCarryover:BigDecimal = BigDecimal("0"),
    val totalExpenses:BigDecimal = BigDecimal("0"),
    val totalBudgeted:BigDecimal = BigDecimal("0"),
    val date:LocalDate = LocalDate.now(),
    val categoryRef: String = ""
)
data class Account(
    @DocumentId val id:String = "",
    val name:String,
    val balance:BigDecimal = BigDecimal("0")
    val transactionsRef:List<String> = listOf()
)
data class Transaction(
    @DocumentId val id:String = "",
    val amount:BigDecimal = BigDecimal("0"),
    val category:String,
    val date:LocalDate = LocalDate.now(),
    val memo:String = "",
    val accountRef: String = ""
)

val unassignedBalance:BigDecimal = BigDecimal("0")*/



/*/**Functions to get defaults and process date below*/

fun getFirstDayOfMonth(): LocalDate {
    return LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
}
fun getFirstDayOfMonth(date: LocalDate): LocalDate {
    return date.with(TemporalAdjusters.firstDayOfMonth())
}


fun Category.getDefaultCategories():List<Category> = listOf(
    Category(name = "Immediate Obligations"),
    Category(name = "True Expenses"),
    Category(name = "Debt Payments"),
    Category(name = "Quality of Life Goals"),
    Category(name = "Just for Fun")
)
fun Category.getEmptyCategory():Category =
    Category(name = "Category Name")


fun BudgetItem.getImmediateObligationsDefaultItems():List<BudgetItem>{
    return listOf(
        BudgetItem(name = "Groceries&Laundry"),
        BudgetItem(name = "Internet"),
        BudgetItem(name = "Electric"),
        BudgetItem(name = "Water"),
        BudgetItem(name = "Rent/Mortgage"),
        BudgetItem(name = "Monthly Software Subscriptions"),
        BudgetItem(name = "Interest & Fees")
    )
}
fun BudgetItem.getTrueExpensesDefaultItems():List<BudgetItem>{
    return listOf(
        BudgetItem(name = "Emergency Fund"),
        BudgetItem(name = "Auto Maintenance"),
        BudgetItem(name = "Home Maintenance"),
        BudgetItem(name = "Renter's/Home Insurance"),
        BudgetItem(name = "Medical"),
        BudgetItem(name = "Clothing"),
        BudgetItem(name = "Gifts"),
        BudgetItem(name = "Computer Replacement"),
        BudgetItem(name = "Annual Software Subscriptions"),
        BudgetItem(name = "Stuff I Forgot to Budget For")
    )
}
fun BudgetItem.getDebtPaymentsDefaultItems():List<BudgetItem>{
    return listOf(
        BudgetItem(name = "Student Loan"),
        BudgetItem(name = "Auto Loan")
    )
}
fun BudgetItem.getQualityOfLifeGoalsDefaultItems():List<BudgetItem>{
    return listOf(
        BudgetItem(name = "Investments"),
        BudgetItem(name = "Vacation"),
        BudgetItem(name = "Fitness"),
        BudgetItem(name = "Education")
    )
}
fun BudgetItem.getJustForFunDefaultItems():List<BudgetItem>{
    return listOf(
        BudgetItem(name = "Dining Out"),
        BudgetItem(name = "Gaming"),
        BudgetItem(name = "Music"),
        BudgetItem(name = "Fun Money")
    )
}
fun BudgetItem.getEmptyItem():BudgetItem =
    BudgetItem(name = "Budget Item Name")
*/