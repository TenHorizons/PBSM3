package com.example.pbsm3.firebaseModel.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
