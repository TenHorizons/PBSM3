package com.example.pbsm3.model.service.repository

interface Carryover<T> {

    suspend fun createAndSaveNewItem(item: T)

    suspend fun processModifiedItem(item:T)
}