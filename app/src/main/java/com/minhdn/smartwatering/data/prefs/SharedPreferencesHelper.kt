package com.minhdn.smartwatering.data.prefs

import android.content.Context
import com.google.gson.Gson
import com.minhdn.smartwatering.data.model.CurrentLocation

class SharedPreferencesHelper(context: Context, private val gson: Gson) {

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        const val PREFS_NAME = "weather_prefs"
        const val KEY_CURRENT_LOCATION = "current_location"

        @Volatile
        private var INSTANCE: SharedPreferencesHelper? = null

        fun getInstance(context: Context, gson: Gson): SharedPreferencesHelper {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPreferencesHelper(context, gson).also { INSTANCE = it }
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
}