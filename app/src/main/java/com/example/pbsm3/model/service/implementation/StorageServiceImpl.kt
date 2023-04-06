package com.example.pbsm3.model.service.implementation

import com.example.pbsm3.model.Transaction
import com.example.pbsm3.model.service.AccountService
import com.example.pbsm3.model.service.StorageService
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.perf.ktx.trace
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

//TODO Currently only supports Transactions. Expand functionality, perhaps in other implementations.
class StorageServiceImpl @Inject constructor(
  private val firestore: FirebaseFirestore,
  private val auth: AccountService
  ) : StorageService {

  @OptIn(ExperimentalCoroutinesApi::class)
  override val transactions: Flow<List<Transaction>>
    get() =
      auth.currentUser.flatMapLatest { user ->
        currentCollection(user.id).snapshots().map { snapshot -> snapshot.toObjects() }
      }

  override suspend fun getTask(transactionId: String): Transaction? =
    currentCollection(auth.currentUserId).document(transactionId).get().await().toObject()

  override suspend fun save(transaction: Transaction): String =
    trace(SAVE_TASK_TRACE) { currentCollection(auth.currentUserId).add(transaction).await().id }

  override suspend fun update(transaction: Transaction): Unit =
    trace(UPDATE_TASK_TRACE) {
      currentCollection(auth.currentUserId).document(transaction.id).set(transaction).await()
    }

  override suspend fun delete(transactionId: String) {
    currentCollection(auth.currentUserId).document(transactionId).delete().await()
  }

   /*Deleting a user doesn't remove its sub-collections. Need to do so manually.
   Also, deleting a collection requires coordinating an unbounded
   number of individual delete requests. If you need to
   delete entire collections, do so only from a
   trusted server environment. While it is possible to
   delete a collection from a mobile/web client, doing so has
   negative security and performance implications.
   https://firebase.google.com/docs/firestore/manage-data/delete-data#kotlin+ktx_2*/
  override suspend fun deleteAllForUser(userId: String) {
    val matchingTasks = currentCollection(userId).get().await()
    matchingTasks.map { it.reference.delete().asDeferred() }.awaitAll()
  }

  private fun currentCollection(uid: String): CollectionReference =
    firestore.collection(USER_COLLECTION).document(uid).collection(TRANSACTION_COLLECTION)

  companion object {
    private const val USER_COLLECTION = "users"
    private const val TRANSACTION_COLLECTION = "transactions"
    private const val SAVE_TASK_TRACE = "saveTask"
    private const val UPDATE_TASK_TRACE = "updateTask"
  }
}
