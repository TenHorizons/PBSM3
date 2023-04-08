package com.example.pbsm3.model.service.repository

import com.example.pbsm3.data.getCarryover
import com.example.pbsm3.model.Unassigned
import java.time.LocalDate

suspend fun Unassigned.processNewItem(repo: UnassignedRepository) {
    val list: MutableList<Unassigned> =
        repo.unassigned.toMutableList()
    if (list.any { it.date == this.date })
        throw IllegalStateException("Unassigned already exists!")
    list.sortBy { it.date }
    val lastDate = list.last().date
    if (lastDate > date)
        throw IllegalStateException(
            "latest Unassigned has newer date!"
        )
    this.linkItems(lastDate, repo)
}

suspend fun Unassigned.linkItems(
    lastDate: LocalDate,
    repo: UnassignedRepository
) {
    var lastD = lastDate
    lastD = lastD.plusMonths(1)
    while (lastD < this.date) {
        val unassigned = Unassigned(date = lastD).calculateCarryover(repo)
        repo.saveData(unassigned, onError = {
            throw RuntimeException(
                "failed to save unassigned to firestore."
            )
        })
        lastD.plusMonths(1)
    }
    val updatedUnassigned =
        this.getRefBySaving(repo).calculateCarryover(repo)
}

suspend fun Unassigned.getRefBySaving(repo:UnassignedRepository):Unassigned{
    val unassignedRef = repo.saveData(this, onError = {
        throw RuntimeException(
            "failed to save unassigned to firestore."
        )
    })
    return this.copy(id = unassignedRef)
}

suspend fun Unassigned.calculateCarryover(repo:UnassignedRepository):Unassigned{
    val list = repo.unassigned
        list.sortBy { it.date }
    val oldCarryover = this.totalCarryover
    val lastCarryover = list.last().getCarryover()
    val updatedCarryover = oldCarryover.plus(lastCarryover)
    return this.copy(totalCarryover = updatedCarryover)
}