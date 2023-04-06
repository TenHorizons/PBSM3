package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.data.defaultCategoryToBudgetItemsMap
import com.example.pbsm3.model.*
import com.example.pbsm3.model.service.dataSource.UserDataSource
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "UserRepository"

@Singleton
class UserRepository @Inject constructor(
    private val userDataSource: UserDataSource,
    private val categoryRepository: Repository<NewCategory>,
    private val accountRepository: Repository<Account>,
    private val transactionRepository: Repository<Transaction>,
    private val budgetItemRepository: Repository<NewBudgetItem>
) {
    var currentUser: SignUser? = null

    suspend fun isUsernameExists(username: String): Boolean {
        return userDataSource.checkIfUsernameExists(username)
    }

    suspend fun signIn(username: String, password: String, onError: () -> Unit): Boolean {
        try {
            currentUser =
                userDataSource.signInUser(username, password)
            Log.d(TAG, "signed in. user: $currentUser")
            if (currentUser != null) return true
        } catch (ex: Exception) {
            Log.d(TAG, "error at UserRepository::signIn")
            Log.d(TAG, "error: $ex")
            onError()
        }
        return false
    }

    suspend fun signUp(username: String, password: String, onError: () -> Unit) {
        //TODO something wrong with signUp, error updating when auto-generating data. check.
        try {
            currentUser = userDataSource.signUpUser(username, password)
            Log.d(TAG, "signed up. user: $currentUser")
        } catch (ex: Exception) {
            Log.d(TAG, "error at UserRepository::signUp")
            onError()
        }
    }

    fun hasUser(): Boolean {
        return currentUser != null
    }

    suspend fun loadUserData(onError: (Exception) -> Unit) {
        try {
            //Apparently used on concurrent programming,and helps coordinate access
            //to shared resources. Could try using semaphore
            //val semaphore = Semaphore(0)
            accountRepository.loadData(currentUser!!.accountRefs, onError = onError)
            transactionRepository.loadData(currentUser!!.transactionRefs, onError = onError)

            //Currently, overwrites category and budget item refs if any one is empty.
            if ((currentUser!!.categoryRefs).isEmpty() || (currentUser!!.budgetItemRefs).isEmpty()) {
                Log.d(TAG, "categoryRefs or budgetItemRefs are empty. Generating default data.")
                generateDefaultDataFromMap(onError = onError)
            } else {
                categoryRepository.loadData(currentUser!!.categoryRefs, onError = onError)
                budgetItemRepository.loadData(currentUser!!.budgetItemRefs, onError = onError)
            }
        } catch (ex: Exception) {
            onError(ex)
        }
    }

    /**Generates default data. Should only be called with user's category and
     * budget item references are empty.*/
    private suspend fun generateDefaultDataFromMap(onError: (Exception) -> Unit) = coroutineScope {
        val categoryReferences: MutableList<String> = mutableListOf()
        val totalItemReferences: MutableList<String> = mutableListOf()

        val categoryNames:MutableList<String> = mutableListOf()
        val budgetItemNames:MutableList<String> = mutableListOf()

        withContext(NonCancellable) {
            defaultCategoryToBudgetItemsMap.map { (category: NewCategory, itemList: List<NewBudgetItem>) ->
                async {
                    val categoryReference = categoryRepository.saveData(category, onError)
                    categoryReferences.add(categoryReference)
                    categoryNames.add(category.name)
                    //TODO category ref is empty in firestore. fix.
                    val budgetItems = itemList.map { newBudgetItem ->
                        newBudgetItem.copy(
                            id = budgetItemRepository.saveData(
                                item = newBudgetItem.copy(
                                    categoryRef = categoryReference
                                ),
                                onError = onError
                            ),
                            categoryRef = categoryReference
                        )
                    }
                    val budgetItemRefs = budgetItems.map { it.id }
                    totalItemReferences.addAll(budgetItemRefs)
                    budgetItemNames.addAll(budgetItems.map{ it.name })

                    categoryRepository.updateData(
                        item = category.copy(
                            id = categoryReference,
                            budgetItemsRef = budgetItemRefs
                        ),
                        onError = onError
                    )
                }
            }.awaitAll()
        }

        //save a copy of auto-generated references to local, then to Firestore for safekeeping
        currentUser = currentUser!!.copy(
            categoryRefs = categoryReferences,
            budgetItemRefs = totalItemReferences,
            categoryNames = categoryNames,
            budgetItemNames = budgetItemNames
        )
        try{
            userDataSource.updateUser(currentUser!!)
        }catch (ex: Exception){
            Log.d(TAG, "error at UserRepository::userDataSource.updateUser()")
            onError(ex)
        }

        //saves a copy to repositories
        //using references to not expose local copy in repository
        categoryRepository.loadData(categoryReferences, onError = onError)
        budgetItemRepository.loadData(totalItemReferences, onError = onError)

        Log.d(TAG, "default categories and budget items generated.")
        Log.d(TAG, "categories: $categoryReferences")
        Log.d(TAG, "budget items: $totalItemReferences")
        Log.d(TAG, "User: $currentUser")
    }

//__________________________________________________________________    


    //var currentUser:User? = null
    /*fun signUp(username: String, password: String) {
        newCurrentUser = userDataSource.signUpUser(username,password, onError = {
            //TODO do something on error.
            Log.d(TAG, "error at UserRepository::signUp")
        })
    }
    suspend fun loadUserData(): User {
        if (currentUser == null) {
            try{
                withContext(Dispatchers.IO) {
                    withTimeout(10000L) {//reset timer
                        currentUser = userDataSource.getUser()
                    }
                    if(currentUser==null)throw NullPointerException()
                }
            }catch(tce: TimeoutCancellationException){
                handleNullUser()
            }catch(ex:NullPointerException){
                handleNullUser()
            }catch (ex:Exception){
                handleNullUser()
            }
        }
        Log.d(TAG,"user data retrieved. User: $currentUser")
        return currentUser!!
    }
    private suspend fun handleNullUser(){
        currentUser = User(
            id = userDataSource.getCurrentUserId()
        )
        userDataSource.save(currentUser!!)
    }
    private suspend fun tryWithTimeout(
        dispatcher: CoroutineDispatcher,
        context: String,
        function: suspend () -> Unit
    ) = try {
        withContext(dispatcher) {
            withTimeout(5000L) {
                function
            }
        }
    } catch (tce: TimeoutCancellationException) {
        Log.d(TAG, "Timeout occurred after 5 seconds at $context")
    } catch (ce: CancellationException) {
        Log.d(TAG, "withContext cancellation occurred at $context")
    } finally {
        dispatcher.cancel()
    }*/
}