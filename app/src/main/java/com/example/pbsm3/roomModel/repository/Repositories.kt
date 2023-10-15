package com.example.pbsm3.roomModel.repository

import com.example.pbsm3.roomModel.entities.AccountBeforeRoom
import com.example.pbsm3.roomModel.entities.BudgetItemBeforeRoom
import com.example.pbsm3.roomModel.entities.CategoryBeforeRoom
import com.example.pbsm3.roomModel.entities.TransactionBeforeRoom
import kotlinx.coroutines.flow.Flow

interface RoomRepository<T>{
    suspend fun insert(data: T)
    suspend fun delete(data: T)
    suspend fun update(data: T)
    suspend fun getById(id: Int): Flow<T?>
}

interface CategoryRepository: RoomRepository<CategoryBeforeRoom>{
    /**Get all categories of the same month and year. Used to display budget.*/
    suspend fun getCategoriesByDate(date: String): Flow<List<CategoryBeforeRoom>>
    /**Get all categories by name. Used when deleting category, since all categories
     * need to be deleted regardless of date.*/
    suspend fun getCategoriesByName(name: String): Flow<List<CategoryBeforeRoom>>
}

interface AccountRepository: RoomRepository<AccountBeforeRoom>{
    /**Get all accounts. Used to display all accounts in Accounts screen.*/
    suspend fun getAccounts(): Flow<List<AccountBeforeRoom>>
}

interface TransactionRepository: RoomRepository<TransactionBeforeRoom>{
    /**Get all transactions with the same account reference. Used to display all transactions
     * of an account in Transaction details page.*/
    suspend fun getTransactionsByAccountRef(accountRef: Int): Flow<List<TransactionBeforeRoom>>
    /**Get all transactions. Used to display all transactions in All Transaction Details page.*/
    suspend fun getTransactions(): Flow<List<TransactionBeforeRoom>>
}

interface BudgetItemRepository: RoomRepository<BudgetItemBeforeRoom>{
    /**Get all budget items with the same category reference. Used in Budget Screen to display
     * Budget Items within a Category Card.*/
    suspend fun getBudgetItemsByCategoryRef(categoryRef: Int):Flow<List<BudgetItemBeforeRoom>>
    /**Get all budget items with the same name. Used when deleting budget item since all budget
     * items need to be deleted regardless of date.*/
    suspend fun getBudgetItemsByName(name: String): Flow<List<BudgetItemBeforeRoom>>
}