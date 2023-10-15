package com.example.pbsm3.firebaseModel.service.repository

import android.util.Log
import com.example.pbsm3.data.getCarryover
import com.example.pbsm3.firebaseModel.Unassigned
import com.example.pbsm3.firebaseModel.service.dataSource.DataSource
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

private const val TAG = "AvailableRepository"

@Singleton
class UnassignedRepository @Inject constructor(
    private val unassignedDataSource: DataSource<Unassigned>,
    private val userRepo: Provider<ProvideUser>
):Repository<Unassigned>,Carryover<Unassigned> {

    var unassigned:MutableList<Unassigned> = mutableListOf()
        private set(value) {
            field = value
            for(listener in listeners){
                listener(field)
            }
        }
    private var listeners:MutableList<(List<Unassigned>)->Unit> = mutableListOf()


    private val userRepository:ProvideUser
    get() = userRepo.get()

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
        Log.i(TAG,"unassigned added.\nindex: $oldAvaIndex\nlist: $unassigned")
        processModifiedItem(item)
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

    override fun getListByRef(ref: String): List<Unassigned> {
        return unassigned.filter { it.id == ref }
    }

    override fun getAll(): List<Unassigned> {
        return unassigned
    }

    override fun onItemsChanged(callback: (List<Unassigned>) -> Unit):()->Unit {
        listeners.add(callback)
        return {listeners.remove(callback)}
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

    //TODO to test when implementing multiple month budget.
    override suspend fun createAndSaveNewItem(item: Unassigned) {
        if (unassigned.any { it.date == item.date })
            throw IllegalStateException("Unassigned already exists!")
        unassigned.sortBy { it.date }
        val lastDate = unassigned.last().date
        if (lastDate > item.date)
            throw IllegalStateException(
                "latest Unassigned has newer date!"
            )
        item.linkItems(lastDate)
    }

    override suspend fun processModifiedItem(item: Unassigned) {
        Log.i(TAG,"processModifiedItem called. Start.")
        val beforeModify = unassigned.first { it.date == item.date }
        if(beforeModify.totalCarryover != item.totalCarryover)
            throw IllegalStateException(
                "carryover was modified outside repository"
            )
        val list = unassigned.toMutableList()
        list.sortBy { it.date }
        val indexOfModified = list.indexOf(beforeModify)
        Log.i(TAG,
            "processModifiedItem before modify:\n" +
                    "${list[indexOfModified]}\n" +
                    "item: $item"
        )
        list[indexOfModified] = item
        Log.i(TAG,"Item modified. updated:\n${list[indexOfModified]}")
        var indexToRecalculate = indexOfModified+1
        while(indexToRecalculate < list.size){
            Log.i(TAG,"recalculating. before recalculate: ${list[indexToRecalculate]}")
            val recalculated = list[indexToRecalculate]
                .calculateCarryover()
            list[indexToRecalculate] = recalculated
            Log.i(TAG,"after recalculate: ${list[indexToRecalculate]}")
            indexToRecalculate++
        }
        unassigned = list
    }

    private suspend fun Unassigned.linkItems(
        lastDate: LocalDate
    ):Unassigned {
        Log.i(TAG,"linking items start.")
        var lastD = lastDate
        lastD = lastD.plusMonths(1)
        while (lastD < this.date) {
            Log.i(TAG,"create new unassigned of date $lastD")
            Unassigned(date = lastD)
                .calculateCarryover()
                .saveLocalAndUpdateWithRef()
            lastD.plusMonths(1)
        }
        Log.i(TAG,"linking items end.")
        return this.calculateCarryover().saveLocalAndUpdateWithRef()
    }

    private suspend fun Unassigned.saveLocalAndUpdateWithRef():Unassigned{
        Log.i(TAG,"saving unassigned start.")
        val unassignedRef = saveData(this, onError = {
            throw RuntimeException(
                "failed to save unassigned to firestore."
            )
        })
        Log.i(TAG,"saving unassigned complete. adding ref to user")
        val updatedUnassigned = this.copy(id = unassignedRef)
        userRepository.addReference(updatedUnassigned)
        Log.i(TAG,"ref update complete.")
        return updatedUnassigned
    }

    private fun Unassigned.calculateCarryover():Unassigned{
        val lastMonth = this.getPreviousMonthUnassigned()
        val currentCarryover = this.totalCarryover
        val lastMonthCarryover = lastMonth.getCarryover()
        val updatedCarryover = currentCarryover.plus(lastMonthCarryover)
        return this.copy(totalCarryover = updatedCarryover)
    }

    private fun Unassigned.getPreviousMonthUnassigned():Unassigned{
        return unassigned.first {
            it.date == this.date.minusMonths(1)
        }
    }
}