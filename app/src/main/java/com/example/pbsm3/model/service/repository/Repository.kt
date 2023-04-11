package com.example.pbsm3.model.service.repository

import java.time.LocalDate

interface Repository<T> {
    suspend fun loadData(docRefs:List<String>, onError:(Exception)->Unit)
    suspend fun saveData()
    /**Saves a single item to Firebase. Return document reference id.*/
    suspend fun saveData(item:T, onError:(Exception)->Unit):String
    suspend fun saveLocalData(item:T)
    suspend fun updateData(item:T, onError:(Exception)->Unit)
    suspend fun updateLocalData(item:T)
    /**Returns a list of items of type T for the repository.
     * For available, should only return 1 element. Check and
     * handle exception if this is not the case.*/
    fun getListByDate(date:LocalDate):List<T>
    fun getByRef(ref:String):T

    fun onItemsChanged(callback: (List<T>)->Unit):()->Unit
}