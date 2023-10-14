package com.example.pbsm3.model.service.room.repository

import com.example.pbsm3.model.service.room.entities.CategoryBeforeRoom
import com.example.pbsm3.model.service.room.entities.RoomObject
import kotlinx.coroutines.flow.Flow

interface Repositories {
    fun getAllStream(): Flow<List<RoomObject>>
    fun getOneStream(id: String): Flow<RoomObject?>
    suspend fun insert(category: RoomObject)
    suspend fun delete(category: RoomObject)
    suspend fun update(category: RoomObject)
}

interface CategoryRepository{
    suspend fun insertCategory(category: CategoryBeforeRoom)
    suspend fun deleteCategory(category: CategoryBeforeRoom)
    suspend fun updateCategory(category: CategoryBeforeRoom)
    suspend fun getCategoryById(id: String): Flow<CategoryBeforeRoom?>
    suspend fun getCategoriesByDate(date: String): Flow<List<CategoryBeforeRoom>>
    suspend fun getCategoriesByName(name: String): Flow<List<CategoryBeforeRoom>>
}