package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.model.Unassigned
import com.example.pbsm3.model.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AvailableRepository"

@Singleton
class UnassignedRepository @Inject constructor(
    private val unassignedDataSource: DataSource<Unassigned>
):Repository<Unassigned> {

    var unassigned:MutableList<Unassigned> = mutableListOf()

    override suspend fun loadData(docRefs: List<String>, onError: (Exception) -> Unit) {
        if(docRefs.isEmpty()) {
            Log.d(TAG, "No 'Available's to retrieve.")
            return
        }
        for(docRef in docRefs){
            try{
                val ava = unassignedDataSource.get(docRef)
                unassigned.add(ava)
            }
            catch (ex:Exception){
                Log.d(TAG, "error at AvailableRepository::loadData")
                Log.d(TAG, "Exception: $ex")
                onError(ex)
            }
        }
        Log.d(TAG, "available loaded. available: $unassigned")
    }

    override suspend fun saveData() {
        TODO("Not yet implemented")
    }

    override fun updateLocalData(item: Unassigned) {
        val oldAva = getByRef(item.id)
        val oldAvaIndex = unassigned.indexOf(oldAva)
        unassigned[oldAvaIndex] = item
    }

    override fun getListByDate(date: LocalDate): List<Unassigned> {
        return unassigned.filter {
            it.date.month == date.month &&
                    it.date.year == date.year
        }
    }

    override fun getByRef(ref: String): Unassigned {
        return unassigned.first { it.id == ref }
    }

    override suspend fun updateData(item: Unassigned, onError: (Exception) -> Unit) {
        try {
            unassignedDataSource.update(item)
        } catch (ex: Exception) {
            Log.d(TAG, "error at AvailableRepository::updateData")
            onError(ex)
        }
    }

    override suspend fun saveData(item: Unassigned, onError: (Exception) -> Unit): String =
        try {
            unassignedDataSource.save(item)
        }catch (ex:Exception){
            Log.d(TAG, "error at AvailableRepository::saveData:String")
            onError(ex)
            ""
        }

}