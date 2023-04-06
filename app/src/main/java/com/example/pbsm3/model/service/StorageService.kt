package com.example.pbsm3.model.service

import com.example.pbsm3.model.Transaction
import kotlinx.coroutines.flow.Flow

interface StorageService {
  val transactions: Flow<List<Transaction>>

  suspend fun getTask(transactionId: String): Transaction?

  suspend fun save(transaction: Transaction): String
  suspend fun update(transaction: Transaction)
  suspend fun delete(transactionId: String)
  suspend fun deleteAllForUser(userId: String)
}
