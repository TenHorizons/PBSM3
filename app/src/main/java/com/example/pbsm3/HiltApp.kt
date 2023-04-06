package com.example.pbsm3

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
//https://stackoverflow.com/questions/71239101/how-to-listen-for-lifecycle-in-jetpack-compose

class HiltApp : Application(), LifecycleObserver{
    override fun onCreate() {
        super.onCreate()
        val lifecycle = ProcessLifecycleOwner.get().lifecycle
        lifecycle.addObserver(object: DefaultLifecycleObserver{
            override fun onDestroy(owner: LifecycleOwner) {
                //TODO Save any unsaved data to Firebase and dispose of resources held by repositories.
                super.onDestroy(owner)
            }
        })
    }
}