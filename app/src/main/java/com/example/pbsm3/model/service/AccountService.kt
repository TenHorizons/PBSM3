package com.example.pbsm3.model.service

import com.example.pbsm3.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>
//Exceptions from methods below are handled by launchCatching [CommonViewModel].
    suspend fun createAnonymousAccount()
    suspend fun authenticate(email: String, password: String)
    suspend fun linkAccount(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun deleteAccount()
    suspend fun signOut()
}
