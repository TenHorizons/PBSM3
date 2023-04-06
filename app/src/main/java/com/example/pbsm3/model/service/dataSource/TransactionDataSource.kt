package com.example.pbsm3.model.service.dataSource

import com.example.pbsm3.model.FirestoreTransaction
import com.example.pbsm3.model.Transaction
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class TransactionDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
): DataSource<Transaction> {
    override suspend fun get(id: String): Transaction =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(id).get()
                    .addOnSuccessListener { document ->
                        val transaction: FirestoreTransaction? =
                            document.toObject(FirestoreTransaction::class.java)
                        if (transaction == null) continuation.resumeWithException(
                            NoSuchElementException("Category not found")
                        )
                        else continuation.resume(toTransaction(transaction))
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun save(item: Transaction): String =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().add(toFirestoreTransaction(item))
                    .addOnSuccessListener {
                        continuation.resume(it.id)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun update(item: Transaction): Unit =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(item.id).set(toFirestoreTransaction(item))
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

    private fun toFirestoreTransaction(item: Transaction): FirestoreTransaction {
        return FirestoreTransaction(
            id = item.id,
            amount = item.amount.toString(),
            category = item.category,
            date = item.date.toString(),
            memo = item.memo,
            accountRef = item.accountRef
        )
    }

    private fun toTransaction(item: FirestoreTransaction): Transaction {
        return Transaction(
            id = item.id,
            amount = item.amount.toBigDecimal(),
            category = item.category,
            date = LocalDate.parse(item.date),
            memo = item.memo,
            accountRef = item.accountRef
        )
    }
}