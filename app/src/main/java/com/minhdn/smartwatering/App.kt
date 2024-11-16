package com.minhdn.smartwatering

import android.app.Application
import com.minhdn.smartwatering.data.local.AppDatabase

class App : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
}