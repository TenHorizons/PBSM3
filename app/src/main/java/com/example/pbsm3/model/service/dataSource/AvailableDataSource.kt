package com.example.pbsm3.model.service.dataSource

import com.example.pbsm3.model.Unassigned
import com.example.pbsm3.model.FirestoreUnassigned
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AvailableDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : DataSource<Unassigned> {
    override suspend fun get(id: String): Unassigned =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(id).get()
                    .addOnSuccessListener { document ->
                        val available: FirestoreUnassigned? =
                            document.toObject(FirestoreUnassigned::class.java)
                        if (available == null) continuation.resumeWithException(
                            NoSuchElementException("Available not found")
                        )
                        else continuation.resume(toAvailable(available))
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun delete(id: String) =
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

    override suspend fun update(item: Unassigned) =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(item.id).set(toFirestoreAvailable(item))
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun save(item: Unassigned): String  =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().add(toFirestoreAvailable(item))
                    .addOnSuccessListener {
                        continuation.resume(it.id)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    private fun getCollection(): CollectionReference =
        firestore.collection(AVAILABLE_COLLECTION)

    private fun toFirestoreAvailable(item: Unassigned): FirestoreUnassigned {
        return FirestoreUnassigned(
            id = item.id,
            totalCarryover = item.totalCarryover.toString(),
            totalExpenses = item.totalExpenses.toString(),
            totalBudgeted = item.totalBudgeted.toString(),
            date = item.date.toString(),
        )
    }

    private fun toAvailable(item: FirestoreUnassigned): Unassigned {
        return Unassigned(
            id = item.id,
            totalCarryover = item.totalCarryover.toBigDecimal(),
            totalExpenses = item.totalExpenses.toBigDecimal(),
            totalBudgeted = item.totalBudgeted.toBigDecimal(),
            date = LocalDate.parse(item.date),
        )
    }
}