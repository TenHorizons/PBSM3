package com.example.pbsm3.model.service.dataSource

import com.example.pbsm3.model.FirestoreCategory
import com.example.pbsm3.model.NewCategory
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CategoryDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : DataSource<NewCategory> {
    override suspend fun get(id: String): NewCategory =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(id).get()
                    .addOnSuccessListener { document ->
                        val category: FirestoreCategory? = document.toObject(FirestoreCategory::class.java)
                        if (category == null) continuation.resumeWithException(
                            NoSuchElementException("Category not found")
                        )
                        else continuation.resume(toNewCategory(category))
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun save(item: NewCategory): String =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().add(toFirestoreCategory(item))
                    .addOnSuccessListener {
                        continuation.resume(it.id)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun update(item: NewCategory): Unit =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(item.id).set(toFirestoreCategory(item))
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
        firestore.collection(CATEGORY_COLLECTION)

    private fun toFirestoreCategory(item: NewCategory): FirestoreCategory {
        return FirestoreCategory(
            id = item.id,
            name = item.name,
            totalCarryover = item.totalCarryover.toString(),
            totalExpenses = item.totalExpenses.toString(),
            totalBudgeted = item.totalBudgeted.toString(),
            date = item.date.toString(),
            budgetItemsRef = item.budgetItemsRef
        )
    }

    private fun toNewCategory(item: FirestoreCategory): NewCategory {
        return NewCategory(
            id = item.id,
            name = item.name,
            totalCarryover = item.totalCarryover.toBigDecimal(),
            totalExpenses = item.totalExpenses.toBigDecimal(),
            totalBudgeted = item.totalBudgeted.toBigDecimal(),
            date = LocalDate.parse(item.date),
            budgetItemsRef = item.budgetItemsRef
        )
    }
}