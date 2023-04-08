package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.model.Transaction
import com.example.pbsm3.model.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "TransactionRepository"

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDataSource: DataSource<Transaction>
):Repository<Transaction> {
    var transactions:MutableList<Transaction> = mutableListOf()
    private set

    override suspend fun loadData(docRefs:List<String>, onError:(Exception)->Unit){
        if(docRefs.isEmpty())
        {
            Log.d(TAG, "No transactions to retrieve.")
            return
        }
        for(docRef in docRefs)
        {
            try
            {
                val transaction = transactionDataSource.get(docRef)
                transactions.add(transaction)
            }
            catch (ex:Exception)
            {
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
        transactions.add(item)
        //TODO add a whole bunch of algorithm
    }

    override suspend fun updateLocalData(item: Transaction) {
        val oldTrans = getByRef(item.id)
        val oldTransIndex = transactions.indexOf(oldTrans)
        transactions[oldTransIndex] = item
        //TODO add a whole bunch of algorithm
    }

    override suspend fun updateData(item: Transaction, onError:(Exception)->Unit) =
        transactionDataSource.update(item)

    override suspend fun saveData(item: Transaction, onError: (Exception) -> Unit): String =
        try {
            transactionDataSource.save(item)
        }catch (ex:Exception){
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
}