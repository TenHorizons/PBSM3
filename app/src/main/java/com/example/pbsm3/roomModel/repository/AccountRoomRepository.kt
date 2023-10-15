package com.example.pbsm3.roomModel.repository

import com.example.pbsm3.roomModel.AccountDao
import com.example.pbsm3.roomModel.entities.AccountBeforeRoom
import com.example.pbsm3.roomModel.entities.toNormalFormat
import com.example.pbsm3.roomModel.entities.toRoomFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AccountRoomRepository @Inject constructor(
    private val accountDao: AccountDao
) : AccountRepository {

    /**Account will be added with empty list of transactions.*/
    override suspend fun insert(data: AccountBeforeRoom) =
        accountDao.insert(data.toRoomFormat())

    /**Different from budget item, instead of reassigning account transactions,
     * transactions are deleted outright. Transactions should be accounted for
     * within budget items, but user will not be able to see account
     * transaction history.*/
    override suspend fun delete(data: AccountBeforeRoom) {
        //TODO delete all transactions within 'account.transactionRefs' before deleting account
        accountDao.delete(data.toRoomFormat())
    }

    override suspend fun update(data: AccountBeforeRoom) =
        accountDao.update(data.toRoomFormat())

    override suspend fun getById(id: Int): Flow<AccountBeforeRoom?> =
        accountDao.getAccountById(id).map { accountRoom ->
            accountRoom?.toNormalFormat() ?: throw NullPointerException(
                "cannot convert to normal account format! data: $accountRoom")
        }


    override suspend fun getAccounts(): Flow<List<AccountBeforeRoom>> =
        accountDao.getAccounts().map { accountRoomList ->
            accountRoomList.map { accountRoom ->
                accountRoom.toNormalFormat()
            }
        }

}