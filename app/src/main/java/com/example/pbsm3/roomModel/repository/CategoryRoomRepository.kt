package com.example.pbsm3.roomModel.repository

import com.example.pbsm3.roomModel.CategoryRoomDao
import com.example.pbsm3.roomModel.entities.CategoryBeforeRoom
import com.example.pbsm3.roomModel.entities.toNormalFormat
import com.example.pbsm3.roomModel.entities.toRoomFormat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryRoomRepository @Inject constructor(
    private val categoryDao: CategoryRoomDao
) : CategoryRepository {
    override suspend fun insert(data: CategoryBeforeRoom) =
        categoryDao.insert(data.toRoomFormat())

    override suspend fun delete(data: CategoryBeforeRoom) =
        categoryDao.delete(data.toRoomFormat())

    override suspend fun update(data: CategoryBeforeRoom) =
        categoryDao.update(data.toRoomFormat())

    override suspend fun getById(id: Int): Flow<CategoryBeforeRoom?> =
        categoryDao.getCategoryById(id).map { categoryRoom ->
            categoryRoom?.toNormalFormat() ?: throw NullPointerException(
                "cannot convert to normal category format! data: $categoryRoom")
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