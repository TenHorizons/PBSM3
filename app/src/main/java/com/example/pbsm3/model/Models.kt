package com.example.pbsm3.model

import com.google.firebase.firestore.DocumentId
import java.math.BigDecimal
import java.time.LocalDate

data class User(
    @DocumentId val id: String = "",
    val username:String = "",
    val password:String = "",
    val categoryRefs:List<String> = listOf(),
    val categoryNames:List<String> = listOf(),
    val accountRefs:List<String> = listOf(),
    val accountNames:List<String> = listOf(),
    val budgetItemRefs:List<String> = listOf(),
    val budgetItemNames:List<String> = listOf(),
    val transactionRefs:List<String> = listOf(),
    val availableRefs:List<String> = listOf()
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

data class Category(
    @DocumentId val id: String = "",
    val name:String ="",
    val totalCarryover:BigDecimal = BigDecimal("0"),
    val totalExpenses:BigDecimal = BigDecimal("0"),
    val totalBudgeted:BigDecimal = BigDecimal("0"),
    val date:LocalDate = LocalDate.now(),
    val budgetItemsRef:List<String> = listOf()
)

data class BudgetItem(
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