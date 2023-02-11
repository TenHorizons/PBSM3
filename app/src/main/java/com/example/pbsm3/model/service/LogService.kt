package com.example.pbsm3.model.service

interface LogService {
    fun logNonFatalCrash(throwable: Throwable)
}
