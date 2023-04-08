package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.model.Account
import com.example.pbsm3.model.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AccountRepository"

@Singleton
class AccountRepository @Inject constructor(
    private val accountDataSource: DataSource<Account>
):Repository<Account> {
    var accounts:MutableList<Account> = mutableListOf()

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

    override suspend fun saveLocalData(item: Account) {
        accounts.add(item)
    }

    override suspend fun updateLocalData(item: Account) {
        val oldAcc = getByRef(item.id)
        val oldAccIndex = accounts.indexOf(oldAcc)
        accounts[oldAccIndex] = item
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
}