package com.example.pbsm3.firebaseModel.service.repository

interface Carryover<T> {

    suspend fun createAndSaveNewItem(item: T)

    suspend fun processModifiedItem(item:T)
}