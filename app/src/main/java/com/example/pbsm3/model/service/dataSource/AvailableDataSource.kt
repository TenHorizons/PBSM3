package com.example.pbsm3.model.service.dataSource

import com.example.pbsm3.model.Available
import com.example.pbsm3.model.FirestoreAvailable
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
) : DataSource<Available> {
    override suspend fun get(id: String): Available =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(id).get()
                    .addOnSuccessListener { document ->
                        val available: FirestoreAvailable? =
                            document.toObject(FirestoreAvailable::class.java)
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

    override suspend fun update(item: Available) =
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

    override suspend fun save(item: Available): String  =
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

    private fun toFirestoreAvailable(item: Available): FirestoreAvailable {
        return FirestoreAvailable(
            id = item.id,
            totalCarryover = item.totalCarryover.toString(),
            totalExpenses = item.totalExpenses.toString(),
            totalBudgeted = item.totalBudgeted.toString(),
            date = item.date.toString(),
        )
    }

    private fun toAvailable(item: FirestoreAvailable): Available {
        return Available(
            id = item.id,
            totalCarryover = item.totalCarryover.toBigDecimal(),
            totalExpenses = item.totalExpenses.toBigDecimal(),
            totalBudgeted = item.totalBudgeted.toBigDecimal(),
            date = LocalDate.parse(item.date),
        )
    }
}