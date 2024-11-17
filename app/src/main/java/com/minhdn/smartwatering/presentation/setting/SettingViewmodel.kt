package com.minhdn.smartwatering.presentation.setting

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.minhdn.smartwatering.data.firebase.FirebaseHelper
import com.minhdn.smartwatering.utils.Constants.LOWER_SOIL_MOISTURE_THRESHOLD
import com.minhdn.smartwatering.utils.Constants.LOWER_TEMPERATURE_THRESHOLD
import com.minhdn.smartwatering.utils.Constants.UPPER_SOIL_MOISTURE_THRESHOLD
import com.minhdn.smartwatering.utils.Constants.UPPER_TEMPERATURE_THRESHOLD
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SettingViewmodel : ViewModel() {
    private val _upperTemperatureThreshold = MutableStateFlow(0f)
    val upperTemperatureThreshold
        get() = _upperTemperatureThreshold.asStateFlow()

    private val _lowerTemperatureThreshold = MutableStateFlow(0f)
    val lowerTemperatureThreshold
        get() = _lowerTemperatureThreshold.asStateFlow()

    private val _upperSoilMoistureThreshold = MutableStateFlow(0f)
    val upperSoilMoistureThreshold
        get() = _upperSoilMoistureThreshold.asStateFlow()

    private val _lowerSoilMoistureThreshold = MutableStateFlow(0f)
    val lowerSoilMoistureThreshold
        get() = _lowerSoilMoistureThreshold.asStateFlow()


    init {
        fetchThreshold()
    }

    fun writeUpperTemperatureThreshold(value: Float) {
        FirebaseHelper.getDatabaseReference(UPPER_TEMPERATURE_THRESHOLD).setValue(value)
    }

    fun writeLowerTemperatureThreshold(value: Float) {
        FirebaseHelper.getDatabaseReference(LOWER_TEMPERATURE_THRESHOLD).setValue(value)
    }

    fun writeUpperSoilMoistureThreshold(value: Float) {
        FirebaseHelper.getDatabaseReference(UPPER_SOIL_MOISTURE_THRESHOLD).setValue(value)
    }

    fun writeLowerSoilMoistureThreshold(value: Float) {
        FirebaseHelper.getDatabaseReference(LOWER_SOIL_MOISTURE_THRESHOLD).setValue(value)
    }

    private fun fetchThreshold() {
        FirebaseHelper.getDatabaseReference(UPPER_TEMPERATURE_THRESHOLD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value
                    if (value is Long) {
                        _upperTemperatureThreshold.value = value.toFloat()
                    } else {
                        _upperTemperatureThreshold.value = value as Float
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        FirebaseHelper.getDatabaseReference(LOWER_TEMPERATURE_THRESHOLD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value
                    if (value is Long) {
                        _lowerTemperatureThreshold.value = value.toFloat()
                    } else {
                        _upperTemperatureThreshold.value = value as Float
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        FirebaseHelper.getDatabaseReference(UPPER_SOIL_MOISTURE_THRESHOLD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value
                    if (value is Long) {
                        _upperSoilMoistureThreshold.value = value.toFloat()
                    } else {
                        _upperTemperatureThreshold.value = value as Float
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })

        FirebaseHelper.getDatabaseReference(LOWER_SOIL_MOISTURE_THRESHOLD)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val value = snapshot.value
                    if (value is Long) {
                        _lowerSoilMoistureThreshold.value = value.toFloat()
                    } else {
                        _upperTemperatureThreshold.value = value as Float
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
    }
}