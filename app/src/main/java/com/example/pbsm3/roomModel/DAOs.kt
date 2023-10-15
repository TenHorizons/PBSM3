package com.example.pbsm3.roomModel

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pbsm3.roomModel.entities.AccountRoom
import com.example.pbsm3.roomModel.entities.CategoryRoom
import com.example.pbsm3.roomModel.entities.TransactionRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryRoomDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: CategoryRoom)

    @Update
    suspend fun update(category: CategoryRoom)

    @Delete
    suspend fun delete(category: CategoryRoom)

    @Query("select * from category where id = :id")
    fun getCategoryById(id: Int): Flow<CategoryRoom?>

    @Query("select * from category where date = :date")
    fun getCategoriesByDate(date: String): Flow<List<CategoryRoom>>

    @Query("select * from category where name = :name")
    fun getCategoriesByName(name: String): Flow<List<CategoryRoom>>
}

@Dao
interface AccountDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: AccountRoom)

    @Update
    suspend fun update(account: AccountRoom)

    @Delete
    suspend fun delete(account: AccountRoom)

    @Query("select * from account where id = :id")
    fun getAccountById(id: Int): Flow<AccountRoom?>

    @Query("select * from account")
    fun getAccounts(): Flow<List<AccountRoom>>
}

@Dao
interface TransactionDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(account: TransactionRoom)

    @Update
    suspend fun update(account: TransactionRoom)

    @Delete
    suspend fun delete(account: TransactionRoom)

    @Query("select * from 'transaction' where id = :id")
    fun getTransactionById(id: Int): Flow<TransactionRoom?>

    @Query("select * from 'transaction' where accountRef = :accountRef")
    fun getTransactionsByAccountRef(accountRef: Int): Flow<List<TransactionRoom>>

    @Query("select * from 'transaction'")
    fun getTransactions(): Flow<List<TransactionRoom>>
}
