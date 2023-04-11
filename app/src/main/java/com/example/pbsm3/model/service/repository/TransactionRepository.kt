package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.model.Account
import com.example.pbsm3.model.BudgetItem
import com.example.pbsm3.model.Transaction
import com.example.pbsm3.model.Unassigned
import com.example.pbsm3.model.service.dataSource.DataSource
import kotlinx.coroutines.*
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

private const val TAG = "TransactionRepository"

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDataSource: DataSource<Transaction>,
    private val accountRepo: Provider<Repository<Account>>,
    private val budgetItemRepo: Provider<Repository<BudgetItem>>,
    private val unassignedRepo: Provider<Repository<Unassigned>>,
    private val unaCarryover: Provider<Carryover<Unassigned>>,
    private val itemCarryover: Provider<Carryover<BudgetItem>>,
) : Repository<Transaction> {
    var transactions: MutableList<Transaction> = mutableListOf()
        private set(value) {
            field = value
            for (listener in listeners) {
                listener(field)
            }
        }

    private val accountRepository: Repository<Account>
        get() = accountRepo.get()
    private val budgetItemRepository: Repository<BudgetItem>
        get() = budgetItemRepo.get()
    private val unassignedRepository: Repository<Unassigned>
        get() = unassignedRepo.get()
    private val unassignedCarryover: Carryover<Unassigned>
        get() = unaCarryover.get()
    private val budgetItemCarryover: Carryover<BudgetItem>
        get() = itemCarryover.get()

    private var listeners: MutableList<(List<Transaction>) -> Unit> = mutableListOf()


    override suspend fun loadData(docRefs: List<String>, onError: (Exception) -> Unit) {
        if (docRefs.isEmpty()) {
            Log.d(TAG, "No transactions to retrieve.")
            return
        }
        for (docRef in docRefs) {
            try {
                val transaction = transactionDataSource.get(docRef)
                transactions.add(transaction)
            } catch (ex: Exception) {
                Log.d(TAG, "error at TransactionRepository::loadTransactions")
                Log.d(TAG, "Exception: $ex")
                onError(ex)
            }
        }
        Log.d(TAG, "Transactions loaded. Transactions: $transactions")
    }

    override suspend fun saveData() {
        TODO("Not yet implemented")
    }

    override suspend fun saveLocalData(item: Transaction) {
        withContext(Dispatchers.Default + NonCancellable) {
            try {
                transactions = (transactions + item).toMutableList()
                val account = accountRepository.getByRef(item.accountRef)
                val assignedToObject =
                    try {
                        unassignedRepository.getByRef(item.assignedTo_Ref)
                    } catch (ex: Exception) {
                        budgetItemRepository.getByRef(item.assignedTo_Ref)
                    }
                val processAccount = async(Dispatchers.Default) {
                    processAccount(item, account, onError = {throw it})
                }
                val processAssignedTo = when (assignedToObject) {
                    is Unassigned -> async(Dispatchers.Default) {
                        processUnassigned(
                            item, assignedToObject, onError = {throw it})
                    }
                    is BudgetItem -> async(Dispatchers.Default) {
                        processBudgetItem(
                            item, assignedToObject, onError = {throw it})
                    }
                    else -> throw IllegalStateException(
                        "assignedToObject incorrect state! obj: $assignedToObject"
                    )
                }
                awaitAll(processAccount, processAssignedTo)
            } catch (ex: Exception) {
                Log.e(
                    TAG,
                    "error processing transaction. error:\n$ex")
                throw ex
            }
        }
    }

    override suspend fun updateLocalData(item: Transaction) {
        val oldTrans = getByRef(item.id)
        val oldTransIndex = transactions.indexOf(oldTrans)
        transactions[oldTransIndex] = item
        //TODO add a whole bunch of algorithm
    }

    override suspend fun updateData(item: Transaction, onError: (Exception) -> Unit) =
        transactionDataSource.update(item)

    override suspend fun saveData(item: Transaction, onError: (Exception) -> Unit): String =
        try {
            transactionDataSource.save(item)
        } catch (ex: Exception) {
            Log.d(TAG, "error at CategoryRepository::saveData:String")
            onError(ex)
            ""
        }

    override fun getListByDate(date: LocalDate): List<Transaction> {
        return transactions.filter { it.date.month == date.month && it.date.year == date.year }
    }

    override fun getByRef(ref: String): Transaction {
        return transactions.first { it.id == ref }
    }

    override fun getListByRef(ref: String): List<Transaction> {
        return transactions.filter { it.accountRef == ref }
    }

    override fun getAll(): List<Transaction> {
        return transactions
    }

    override fun onItemsChanged(callback: (List<Transaction>) -> Unit): () -> Unit {
        listeners.add(callback)
        return { listeners.remove(callback) }
    }

    private suspend fun processAccount(
        newTransaction: Transaction,
        account: Account,
        onError: (Exception) -> Unit
    ) {
        Log.i(TAG, "addTransaction processAccount start.")
        val updatedAccount = account.copy(
            balance = account.balance.plus(newTransaction.amount),
            transactionRefs = account.transactionRefs + newTransaction.id
        )
        accountRepository.updateLocalData(updatedAccount)
        Log.i(TAG, "addTransaction processAccount completed. updated account:\n$updatedAccount")
    }

    private suspend fun processBudgetItem(
        newTransaction: Transaction,
        budgetItem: BudgetItem,
        onError: (Exception) -> Unit
    ) {
        Log.i(TAG, "addTransaction processBudgetItem start.")
        val updatedBudgetItem = budgetItem.copy(
            totalExpenses = budgetItem.totalExpenses.plus(newTransaction.amount)
        )
        budgetItemRepository.updateLocalData(updatedBudgetItem)
        Log.i(
            TAG,
            "addTransaction processBudgetItem completed. \n" +
                    "updatedBudgetItem: $updatedBudgetItem"
        )
    }

    private suspend fun processUnassigned(
        newTransaction: Transaction,
        unassigned: Unassigned,
        onError: (Exception) -> Unit
    ) {
        Log.i(TAG, "addTransaction processUnassigned start.")
        val updatedUnassigned = unassigned.copy(
            totalExpenses = unassigned.totalExpenses.plus(newTransaction.amount)
        )
        unassignedRepository.updateLocalData(updatedUnassigned)
        Log.i(
            TAG,
            "addTransaction processUnassigned completed. \n" +
                    "updatedUnassigned: $updatedUnassigned"
        )
    }
}