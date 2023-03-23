package com.example.pbsm3.model.service.implementation

import com.example.pbsm3.model.User
import com.example.pbsm3.model.service.AccountService
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth
) : AccountService {
    //Access user info
    /*val user = Firebase.auth.currentUser
    user?.let {
        // Name, email address, and profile photo Url
        val name = it.displayName
        val email = it.email
        val photoUrl = it.photoUrl

        // Check if user's email is verified
        val emailVerified = it.isEmailVerified

        // The user's ID, unique to the Firebase project. Do NOT use this value to
        // authenticate with your backend server, if you have one. Use
        // FirebaseUser.getIdToken() instead.
        val uid = it.uid
    }*/

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()
    override val hasUser: Boolean
        get() = auth.currentUser != null
    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(it.uid, it.isAnonymous) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()

    }

    //TODO: overload authenticate to provide other sign in methods
    //create user with email and password implementation
    /*auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = auth.currentUser
//                    updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
//                    updateUI(null)
            }
        }*/
    //sign in user with email and password implementation
    /*auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success")
                val user = auth.currentUser
//                    updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
//                    updateUI(null)
            }
        }*/
    //convert anon account to permanent account
    /*https://firebase.google.com/docs/auth/android/anonymous-auth#convert-an-anonymous
    -account-to-a-permanent-account*/
    /*val credential = GoogleAuthProvider.getCredential(googleIdToken, null)
    //above is for google sign-in. refer link for getting credential for other sign-in methods.
    auth.currentUser!!.linkWithCredential(credential)
        .addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "linkWithCredential:success")
                val user = task.result?.user
//                    updateUI(user)
            } else {
                Log.w(TAG, "linkWithCredential:failure", task.exception)
                Toast.makeText(context, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
//                    updateUI(null)
            }
        }*/

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun linkAccount(email: String, password: String) {
        //removed trace. add in later after properly figuring out what it does.
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).await()
    }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
        }
        auth.signOut()
        // Sign the user back in anonymously.
        createAnonymousAccount()
    }

    //TODO removed trace. add in later after properly figuring out what it does.
}