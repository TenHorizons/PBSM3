package com.example.pbsm3.model.service.repository

import com.example.pbsm3.model.PBSObject

interface ProvideUser {
    fun addReference(item: PBSObject)
    fun removeReference(item: PBSObject)
}