package com.minhdn.smartwatering.data.prefs

import android.content.Context
import com.google.gson.Gson
import com.minhdn.smartwatering.data.model.CurrentLocation
import com.minhdn.smartwatering.utils.Constants.KEY_IS_ALARM

class SharedPreferencesHelper(context: Context) {

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    companion object {
        const val PREFS_NAME = "smart_watering_prefs"
        const val KEY_CURRENT_LOCATION = "current_location"

        @Volatile
        private var INSTANCE: SharedPreferencesHelper? = null

        fun getInstance(context: Context): SharedPreferencesHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPreferencesHelper(context).also { INSTANCE = it }
            }
        }
    }

    fun saveCurrentLocation(currentLocation: CurrentLocation) {
        sharedPreferences.edit().putString(KEY_CURRENT_LOCATION, gson.toJson(currentLocation)).apply()
    }

    fun getCurrentLocation(): CurrentLocation? {
        val currentLocationJson = sharedPreferences.getString(KEY_CURRENT_LOCATION, null)
        return gson.fromJson(currentLocationJson, CurrentLocation::class.java)
    }

    fun setIsAlarm(isAlarm: Boolean) {
        sharedPreferences.edit().putBoolean(KEY_IS_ALARM, isAlarm).apply()
    }

    fun getIsAlarm(): Boolean {
        return sharedPreferences.getBoolean(KEY_IS_ALARM, false)
    }
}