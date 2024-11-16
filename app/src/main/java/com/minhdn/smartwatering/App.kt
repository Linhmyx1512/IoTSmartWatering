package com.minhdn.smartwatering

import android.app.Application
import com.minhdn.smartwatering.data.local.AppDatabase

class App : Application() {
    companion object {
        lateinit var instance: App
            private set
    }

    val database by lazy {
        AppDatabase.getInstance()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}