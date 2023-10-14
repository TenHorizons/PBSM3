package com.example.pbsm3.model.service.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pbsm3.model.service.room.entities.CategoryRoom
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryRoomDao{
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(category: CategoryRoom)

    @Update
    suspend fun update(category: CategoryRoom)

    @Delete
    suspend fun delete(category: CategoryRoom)

    @Query("select * from category where id = :id")
    suspend fun getCategoryById(id: String): Flow<CategoryRoom>

    @Query("select * from category where date = :date")
    suspend fun getCategoriesByDate(date: String): Flow<List<CategoryRoom>>

    @Query("select * from category where name = :name")
    suspend fun getCategoriesByName(name: String): Flow<List<CategoryRoom>>
}

@Dao
interface AccountDao{

}

accountRepository