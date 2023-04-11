package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.model.Account
import com.example.pbsm3.model.Transaction
import com.example.pbsm3.model.Unassigned
import com.example.pbsm3.model.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

private const val TAG = "AccountRepository"

@Singleton
class AccountRepository @Inject constructor(
    private val accountDataSource: DataSource<Account>,
    private val transactionRepo: Provider<Repository<Transaction>>,
    private val unaRepo: Provider<Repository<Unassigned>>,
    private val userRepo: Provider<ProvideUser>
):Repository<Account> {
    var accounts:MutableList<Account> = mutableListOf()
        private set(value) {
            field = value
            for(listener in listeners){
                listener(field)
            }
        }
    private val transactionsRepository:Repository<Transaction>
    get() = transactionRepo.get()
    private val unassignedRepository:Repository<Unassigned>
    get() = unaRepo.get()
    private val userRepository:ProvideUser
    get() = userRepo.get()

    private var listeners:MutableList<(List<Account>)->Unit> = mutableListOf()

    override suspend fun loadData(docRefs:List<String>, onError:(Exception)->Unit){
        if(docRefs.isEmpty()) {
            Log.d(TAG, "No accounts to retrieve.")
            return
        }
        for(docRef in docRefs){
            try{
                val account = accountDataSource.get(docRef)
                accounts.add(account)
            }
            catch (ex:Exception){
                Log.e(TAG, "error at AccountRepository::loadAccounts")
                Log.e(TAG, "Exception: $ex")
                onError(ex)
            }
        }
        Log.d(TAG, "Accounts loaded. Accounts: $accounts")
    }

    override suspend fun saveData() {
        TODO("Not yet implemented")
    }

    /*create a transaction to account for balance.*/
    override suspend fun saveLocalData(item: Account) {
        accounts = (accounts + item).toMutableList()
        val unassigned = unassignedRepository.getListByDate(LocalDate.now()).first()
        var newTransaction = Transaction(
            amount = item.balance,
            memo = "Starting Balance",
            accountRef = item.id,
            assignedTo_Ref = unassigned.id
        )
        val transRef = transactionsRepository.saveData(newTransaction, onError = {throw it})
        newTransaction = newTransaction.copy(id = transRef)
        transactionsRepository.saveLocalData(newTransaction)
        userRepository.addReference(newTransaction)
        val processedAccount = item.copy(
            transactionRefs = item.transactionRefs + transRef,
            position = accounts.size
        )
        updateLocalData(processedAccount)
    }

    /*No edit account allowed because illogical. only remove entire account
    * altogether. for example, discrepancies in balance
    * should be settled by checking for wrong transaction amount,
    * while account name should never change.
    * when enable delete account, transactions remain in
    * collection, but account ref will take DELETED constant.*/
    override suspend fun updateLocalData(item: Account) {
        val oldAcc = getByRef(item.id)
        Log.i(TAG,"account update start.Before update:\n$oldAcc\nupdated item:\n$item")
        val oldAccIndex = accounts.indexOf(oldAcc)
        accounts[oldAccIndex] = item
        Log.i(TAG,"account updated.\nindex: $oldAccIndex\nlist: \n$accounts")
    }

    override suspend fun updateData(item: Account, onError:(Exception)->Unit) =
        accountDataSource.update(item)

    override suspend fun saveData(item: Account, onError: (Exception) -> Unit): String =
        try {
            accountDataSource.save(item)
        }catch (ex:Exception){
            Log.e(TAG, "error at CategoryRepository::saveData:String")
            onError(ex)
            ""
        }

    override fun getListByDate(date: LocalDate): List<Account> {
        Log.d(TAG,"AccountRepository::getListByDate shouldn't be called.")
        Log.d(TAG,"Returning full account list anyway since most uses need full list.")
        Log.d(TAG,"Regretting using repository interface...")
        return accounts
    }

    override fun getByRef(ref: String): Account {
        return accounts.first { it.id == ref }
    }

    override fun getListByRef(ref: String): List<Account> {
        //not supposed to return a list by ref.
        // Just returning all since it's the common function.
        return accounts
    }

    override fun getAll(): List<Account> {
        return accounts
    }

    override fun onItemsChanged(callback: (List<Account>) -> Unit):()->Unit {
        listeners.add(callback)
        return {listeners.remove(callback)}
    }
}