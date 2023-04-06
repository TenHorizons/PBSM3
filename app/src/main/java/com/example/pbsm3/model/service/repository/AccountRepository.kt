package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.model.Account
import com.example.pbsm3.model.service.dataSource.DataSource
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
                Log.d(TAG, "error at AccountRepository::loadAccounts")
                Log.d(TAG, "Exception: $ex")
                onError(ex)
            }
        }
        Log.d(TAG, "Accounts loaded. Accounts: $accounts")
    }

    override suspend fun saveData() {
        TODO("Not yet implemented")
    }

    override suspend fun updateData(item: Account, onError:(Exception)->Unit) =
        accountDataSource.update(item)

    override suspend fun saveData(item: Account, onError: (Exception) -> Unit): String =
        try {
            accountDataSource.save(item)
        }catch (ex:Exception){
            Log.d(TAG, "error at CategoryRepository::saveData:String")
            onError(ex)
            ""
        }
}