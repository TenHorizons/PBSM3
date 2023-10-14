package com.example.pbsm3.model.service.room

import android.content.Context
import com.example.pbsm3.model.service.room.repository.CategoryRoomRepositories

interface AppContainer{
    val categoryRepository: CategoryRoomRepositories
}


class AppDataContainer(private val context: Context): AppContainer{
    override val categoryRepository: CategoryRoomRepositories by lazy{
        CategoryRoomRepositories(PBSM3Database.getDatabase(context).categoryDao())
    }
}