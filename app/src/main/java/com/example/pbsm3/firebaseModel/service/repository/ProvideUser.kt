package com.example.pbsm3.firebaseModel.service.repository

import com.example.pbsm3.firebaseModel.PBSObject

interface ProvideUser {
    fun addReference(item: PBSObject)
    fun removeReference(item: PBSObject)
}