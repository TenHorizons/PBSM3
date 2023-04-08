package com.example.pbsm3.model.service.repository

import android.util.Log
import com.example.pbsm3.data.getCarryover
import com.example.pbsm3.model.Unassigned
import com.example.pbsm3.model.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "AvailableRepository"

@Singleton
class UnassignedRepository @Inject constructor(
    private val unassignedDataSource: DataSource<Unassigned>
):Repository<Unassigned>,Carryover<Unassigned> {

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

    override suspend fun saveLocalData(item: Unassigned) {
        TODO("Not yet implemented")
    }

    override suspend fun updateLocalData(item: Unassigned) {
        val oldAva = getByRef(item.id)
        val oldAvaIndex = unassigned.indexOf(oldAva)
        unassigned[oldAvaIndex] = item
        //TODO add a whole bunch of algorithm
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

    override suspend fun createNewItem(item: Unassigned) {
        val list: MutableList<Unassigned> = unassigned.toMutableList()
        if (list.any { it.date == item.date })
            throw IllegalStateException("Unassigned already exists!")
        list.sortBy { it.date }
        val lastDate = list.last().date
        if (lastDate > item.date)
            throw IllegalStateException(
                "latest Unassigned has newer date!"
            )
        val updatedUnassigned = item.linkItems(lastDate)
        saveData(updatedUnassigned, onError = {
            throw java.lang.RuntimeException(
                "could not save updated Unassigned to database."
            )
        })
    }

    override suspend fun processModifiedItem(item: Unassigned) {
        TODO("Not yet implemented")
    }

    private suspend fun Unassigned.linkItems(
        lastDate: LocalDate
    ):Unassigned {
        var lastD = lastDate
        lastD = lastD.plusMonths(1)
        while (lastD < this.date) {
            val unassigned = Unassigned(date = lastD).calculateCarryover()
            saveData(unassigned, onError = {
                throw RuntimeException(
                    "failed to save unassigned to firestore."
                )
            })
            lastD.plusMonths(1)
        }
        return this.getRefBySaving().calculateCarryover()
    }

    private suspend fun Unassigned.getRefBySaving():Unassigned{
        val unassignedRef = saveData(this, onError = {
            throw RuntimeException(
                "failed to save unassigned to firestore."
            )
        })
        return this.copy(id = unassignedRef)
    }

    private suspend fun Unassigned.calculateCarryover():Unassigned{
        val list = unassigned
        list.sortBy { it.date }
        val oldCarryover = this.totalCarryover
        val lastCarryover = list.last().getCarryover()
        val updatedCarryover = oldCarryover.plus(lastCarryover)
        return this.copy(totalCarryover = updatedCarryover)
    }
}