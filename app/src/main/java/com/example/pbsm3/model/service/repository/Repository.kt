package com.example.pbsm3.model.service.repository

interface Repository<T> {
    suspend fun loadData(docRefs:List<String>, onError:(Exception)->Unit)
    suspend fun saveData()
    /**Saves a single item to Firebase. Return document reference id.*/
    suspend fun saveData(item:T, onError:(Exception)->Unit):String
    suspend fun updateData(item:T, onError:(Exception)->Unit)
}