package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.model.Available
import com.example.pbsm3.model.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AvailableRepository"

@Singleton
class AvailableRepository @Inject constructor(
    private val availableDataSource: DataSource<Available>
):Repository<Available> {

    var available:MutableList<Available> = mutableListOf()

    override suspend fun loadData(docRefs: List<String>, onError: (Exception) -> Unit) {
        if(docRefs.isEmpty()) {
            Log.d(TAG, "No 'Available's to retrieve.")
            return
        }
        for(docRef in docRefs){
            try{
                val ava = availableDataSource.get(docRef)
                available.add(ava)
            }
            catch (ex:Exception){
                Log.d(TAG, "error at AvailableRepository::loadData")
                Log.d(TAG, "Exception: $ex")
                onError(ex)
            }
        }
        Log.d(TAG, "available loaded. available: $available")
    }

    override suspend fun saveData() {
        TODO("Not yet implemented")
    }

    override fun updateLocalData(item: Available) {
        val oldAva = getByRef(item.id)
        val oldAvaIndex = available.indexOf(oldAva)
        available[oldAvaIndex] = item
    }

    override fun getListByDate(date: LocalDate): List<Available> {
        return available.filter {
            it.date.month == date.month &&
                    it.date.year == date.year
        }
    }

    override fun getByRef(ref: String): Available {
        return available.first { it.id == ref }
    }

    override suspend fun updateData(item: Available, onError: (Exception) -> Unit) {
        try {
            availableDataSource.update(item)
        } catch (ex: Exception) {
            Log.d(TAG, "error at AvailableRepository::updateData")
            onError(ex)
        }
    }

    override suspend fun saveData(item: Available, onError: (Exception) -> Unit): String =
        try {
            availableDataSource.save(item)
        }catch (ex:Exception){
            Log.d(TAG, "error at AvailableRepository::saveData:String")
            onError(ex)
            ""
        }

}