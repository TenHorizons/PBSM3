package com.example.pbsm3.model.service.dataSource

interface DataSource<T> {
    suspend fun get(id: String): T
    suspend fun save(item: T): String
    suspend fun update(item: T)
    suspend fun delete(id: String)
}
