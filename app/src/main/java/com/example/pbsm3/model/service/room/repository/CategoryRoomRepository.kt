package com.example.pbsm3.model.service.room.repository

import com.example.pbsm3.model.service.room.CategoryRoomDao
import com.example.pbsm3.model.service.room.entities.CategoryBeforeRoom
import com.example.pbsm3.model.service.room.entities.toNormalFormat
import com.example.pbsm3.model.service.room.entities.toRoomFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CategoryRoomRepository(private val categoryDao: CategoryRoomDao) : CategoryRepository {
    override suspend fun insertCategory(category: CategoryBeforeRoom) =
        categoryDao.insert(category.toRoomFormat())

    override suspend fun deleteCategory(category: CategoryBeforeRoom) =
        categoryDao.delete(category.toRoomFormat())

    override suspend fun updateCategory(category: CategoryBeforeRoom) =
        categoryDao.update(category.toRoomFormat())

    override suspend fun getCategoryById(id: String): Flow<CategoryBeforeRoom?> =
        categoryDao.getCategoryById(id).map { categoryRoom ->
            categoryRoom.toNormalFormat()
        }

    override suspend fun getCategoriesByDate(date: String): Flow<List<CategoryBeforeRoom>> =
        categoryDao.getCategoriesByDate(date).map{categoryRoomList ->
            categoryRoomList.map { categoryRoom ->
                categoryRoom.toNormalFormat()
            }
        }

    override suspend fun getCategoriesByName(name: String): Flow<List<CategoryBeforeRoom>> =
        categoryDao.getCategoriesByName(name).map{categoryRoomList ->
            categoryRoomList.map { categoryRoom ->
                categoryRoom.toNormalFormat()
            }
        }
}