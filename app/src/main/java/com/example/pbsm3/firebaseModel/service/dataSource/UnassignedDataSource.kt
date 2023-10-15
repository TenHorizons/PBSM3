package com.example.pbsm3.firebaseModel.service.dataSource

import com.example.pbsm3.firebaseModel.FirestoreUnassigned
import com.example.pbsm3.firebaseModel.Unassigned
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UnassignedDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : DataSource<Unassigned> {
    override suspend fun get(id: String): Unassigned =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(id).get()
                    .addOnSuccessListener { document ->
                        val unassigned: FirestoreUnassigned? =
                            document.toObject(FirestoreUnassigned::class.java)
                        if (unassigned == null) continuation.resumeWithException(
                            NoSuchElementException("Available not found")
                        )
                        else continuation.resume(toUnassigned(unassigned))
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
                getCollection().document(item.id).set(toFirestoreUnassigned(item))
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
                getCollection().add(toFirestoreUnassigned(item))
                    .addOnSuccessListener {
                        continuation.resume(it.id)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    private fun getCollection(): CollectionReference =
        firestore.collection(UNASSIGNED_COLLECTION)

    private fun toFirestoreUnassigned(item: Unassigned): FirestoreUnassigned {
        return FirestoreUnassigned(
            id = item.id,
            totalCarryover = item.totalCarryover.toString(),
            totalExpenses = item.totalExpenses.toString(),
            totalBudgeted = item.totalBudgeted.toString(),
            date = item.date.toString(),
        )
    }

    private fun toUnassigned(item: FirestoreUnassigned): Unassigned {
        return Unassigned(
            id = item.id,
            totalCarryover = item.totalCarryover.toBigDecimal(),
            totalExpenses = item.totalExpenses.toBigDecimal(),
            totalBudgeted = item.totalBudgeted.toBigDecimal(),
            date = LocalDate.parse(item.date),
        )
    }
}