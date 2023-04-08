package com.example.pbsm3.model.service.dataSource

import com.example.pbsm3.model.FirestoreBudgetItem
import com.example.pbsm3.model.BudgetItem
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class BudgetItemDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
): DataSource<BudgetItem> {
    override suspend fun get(id: String): BudgetItem =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(id).get()
                    .addOnSuccessListener { document ->
                        val item: FirestoreBudgetItem? = document.toObject(FirestoreBudgetItem::class.java)
                        if (item == null) continuation.resumeWithException(
                            NoSuchElementException("Category not found")
                        )
                        else {
                            continuation.resume(toNewBudgetItem(item))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun save(item: BudgetItem): String =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().add(toFirestoreBudgetItem(item))
                    .addOnSuccessListener {
                        continuation.resume(it.id)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun update(item: BudgetItem): Unit =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(item.id).set(toFirestoreBudgetItem(item))
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun delete(id: String): Unit =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(id).delete()
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    private fun getCollection(): CollectionReference =
        firestore.collection(BUDGET_ITEM_COLLECTION)

    private fun toFirestoreBudgetItem(item:BudgetItem):FirestoreBudgetItem{
        return FirestoreBudgetItem(
            id = item.id,
            name = item.name,
            totalCarryover = item.totalCarryover.toString(),
            totalBudgeted = item.totalBudgeted.toString(),
            totalExpenses = item.totalExpenses.toString(),
            date = item.date.toString(),
            categoryRef = item.categoryRef
        )
    }

    private fun toNewBudgetItem(item:FirestoreBudgetItem):BudgetItem{
        return BudgetItem(
            id = item.id,
            name = item.name,
            totalCarryover = item.totalCarryover.toBigDecimal(),
            totalBudgeted = item.totalBudgeted.toBigDecimal(),
            totalExpenses = item.totalExpenses.toBigDecimal(),
            date = LocalDate.parse(item.date),
            categoryRef = item.categoryRef
        )
    }
}