package com.example.pbsm3.model.service.dataSource

import android.util.Log
import com.example.pbsm3.model.SignUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

private const val TAG = "UserDataSource"

class UserDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
//    private val auth: AccountService
) {
    suspend fun checkIfUsernameExists(username: String): Boolean =
        withContext(Dispatchers.IO){
            suspendCoroutine { continuation ->
                try {
                    getCollection().whereEqualTo("username", username).get()
                        .addOnSuccessListener { documents ->
                            val list: List<SignUser> = documents.toObjects(SignUser::class.java)
                            Log.d(TAG, "users: $list")
                            if (list.isNotEmpty()) {
                                continuation.resume(true)
                            } else {
                                continuation.resume(false)
                            }
                        }
                        .addOnFailureListener {
                            continuation.resume(false)
                        }
                } catch (ex: Exception) {
                    continuation.resume(false)
                }
            }
        }

    suspend fun signInUser(username: String, password: String): SignUser =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().whereEqualTo("username", username)
                    .whereEqualTo("password", password)
                    .get()
                    .addOnSuccessListener { documents ->
                        val list: List<SignUser> = documents.toObjects(SignUser::class.java)
                        Log.d(TAG, "users: $list")
                        if (list.isNotEmpty()) {
                            continuation.resume(list[0])
                        } else {
                            continuation.resumeWithException(
                                NoSuchElementException("User not found"))
                        }
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    suspend fun signUpUser(username: String, password: String): SignUser =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                val user = SignUser(
                    username = username,
                    password = password
                )
                getCollection().add(user)
                    .addOnSuccessListener {reference ->
                        continuation.resume(user.copy(id = reference.id))
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(
                            Exception(" error at UserDataSource::signUpUser")
                        )
                    }
            }
        }

    suspend fun updateUser(currentUser: SignUser) =
        withContext(Dispatchers.IO) {
            suspendCoroutine { continuation ->
                getCollection().document(currentUser.id).set(currentUser)
                    .addOnSuccessListener {
                        continuation.resume(Unit)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it)
                    }
            }
        }

    private fun getCollection(): CollectionReference =
        firestore.collection(USER_COLLECTION)


    //________________________________________________________

    /*fun checkIfUsernameExists(username: String): Boolean {
        var isExist = false
        getCollection().whereEqualTo("username", username).get()
            .addOnSuccessListener { isExist = true }
            .addOnFailureListener { isExist = false }
        return isExist
    }

    fun signUpUser(username: String, password: String, onError: () -> Unit): SignUser {
        val user = SignUser(
            username = username,
            password = password
        )
        var success = false
        getCollection().add(user).addOnSuccessListener { success = true }
        if (!success) onError()
        return user
    }

    suspend fun getUser(): User? {
        return getCollection().document(auth.currentUserId).get().await().toObject()
    }

    fun getCurrentUserId(): String {
        return auth.currentUserId
    }

    suspend fun save(user: User): String {
        try {
            getCollection().document(user.id).set(user).await()
        } catch (ex: Exception) {
            Log.d(TAG, "$ex")
        }
        Log.d(TAG, "user data saved? user: $user, docID: ${user.id}")
        return user.id
    }

    suspend fun update(user: User) {
        tryWithTimeout(dispatcher = Dispatchers.IO, context = "UserDataSource::update") {
            trace(UPDATE_ACCOUNT_TRACE) {
                getCollection().document(user.id)
                    .set(user).await()
            }
        }
    }

    suspend fun delete(id: String) {
        getCollection().document(id)
            .delete().await()
    }



    private suspend fun tryWithTimeout(
        dispatcher: CoroutineDispatcher,
        context: String,
        function: suspend () -> Unit
    ) = try {
        withContext(dispatcher) {
            withTimeout(5000L) {
                function
            }
        }
    } catch (tce: TimeoutCancellationException) {
        Log.d(TAG, "Timeout occurred after 5 seconds at $context")
    } catch (ce: CancellationException) {
        Log.d(TAG, "withContext cancellation occurred at $context")
    } finally {
        dispatcher.cancel()
    }*/
}