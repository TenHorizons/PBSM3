package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.model.NewCategory
import com.example.pbsm3.model.service.dataSource.DataSource
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "CategoryRepository"

@Singleton
class CategoryRepository @Inject constructor(
    private val categoryDataSource: DataSource<NewCategory>
):Repository<NewCategory> {

    //TODO confirm that all data source calls are surrounded with try catch.
    var categories:MutableList<NewCategory> = mutableListOf()

    override suspend fun loadData(docRefs:List<String>, onError:(Exception)->Unit){
        if(docRefs.isEmpty()) {
            Log.d(TAG, "No categories to retrieve.")
            return
        }
        for(docRef in docRefs){
            try{
                val category = categoryDataSource.get(docRef)
                categories.add(category)
            }
            catch (ex:Exception){
                Log.d(TAG, "error at CategoryRepository::loadCategories")
                Log.d(TAG, "Exception: $ex")
                onError(ex)
            }
        }
        Log.d(TAG, "Categories loaded. Categories: $categories")
    }

    override suspend fun saveData() {
        TODO("Not yet implemented")
    }

    override suspend fun updateData(item: NewCategory, onError:(Exception)->Unit) {
        try {
            categoryDataSource.update(item)
        } catch (ex: Exception) {
            Log.d(TAG, "error at CategoryRepository::saveData:String")
            onError(ex)
        }
    }

    override suspend fun saveData(item: NewCategory, onError:(Exception)->Unit): String =
        try {
            categoryDataSource.save(item)
        }catch (ex:Exception){
            Log.d(TAG, "error at CategoryRepository::saveData:String")
            onError(ex)
            ""
        }
}