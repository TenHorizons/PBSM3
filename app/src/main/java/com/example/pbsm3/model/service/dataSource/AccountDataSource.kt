package com.example.pbsm3.model.service.dataSource

import com.example.pbsm3.model.Account
import com.example.pbsm3.model.FirestoreAccount
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AccountDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) : DataSource<Account> {

    override suspend fun get(id: String): Account =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(id).get()
                    .addOnSuccessListener { document ->
                        val account: FirestoreAccount? = document.toObject(FirestoreAccount::class.java)
                        if (account == null) continuation.resumeWithException(
                            NoSuchElementException("User not found"))
                        else continuation.resume(toAccount(account))
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun save(item: Account): String =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().add(toFirestoreAccount(item))
                    .addOnSuccessListener {
                        continuation.resume(it.id)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    override suspend fun update(item: Account): Unit =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(item.id).set(toFirestoreAccount(item))
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
        firestore.collection(ACCOUNT_COLLECTION)

    private fun toFirestoreAccount(item: Account): FirestoreAccount {
        return FirestoreAccount(
            id = item.id,
            name = item.name,
            balance = item.balance.toString(),
            transactionRefs = item.transactionRefs
        )
    }

    private fun toAccount(item: FirestoreAccount): Account {
        return Account(
            id = item.id,
            name = item.name,
            balance = item.balance.toBigDecimal(),
            transactionRefs = item.transactionRefs
        )
    }
}