package com.minhdn.smartwatering.presentation.home

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.minhdn.smartwatering.data.firebase.FirebaseHelper
import com.minhdn.smartwatering.utils.Constants.HUMIDITY
import com.minhdn.smartwatering.utils.Constants.IS_AUTO
import com.minhdn.smartwatering.utils.Constants.IS_PUMP_ON
import com.minhdn.smartwatering.utils.Constants.SENSOR_DATA
import com.minhdn.smartwatering.utils.Constants.SOIL_MOISTURE
import com.minhdn.smartwatering.utils.Constants.TEMPERATURE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel : ViewModel() {

    private val _humidity = MutableStateFlow(0f)
    val humidity: StateFlow<Float> = _humidity

    private val _soilMoisture = MutableStateFlow(0f)
    val soilMoisture: StateFlow<Float> = _soilMoisture

    private val _temperature = MutableStateFlow(0f)
    val temperature: StateFlow<Float> = _temperature

    private val _isAuto = MutableStateFlow(false)
    val isAuto: StateFlow<Boolean> = _isAuto

    private val _isPump = MutableStateFlow(false)
    val isPump: StateFlow<Boolean> = _isPump


    init {
        fetchSensorData()
        fetchAutoStatus()
        fetchPushStatus()
    }

    fun toggleAuto() {
        val databaseAuto: DatabaseReference = FirebaseHelper.getDatabaseReference(IS_AUTO)
        databaseAuto.setValue(!_isAuto.value)
        _isAuto.value = !_isAuto.value

        if (!_isAuto.value && _isPump.value) {
            togglePump()
        }
    }

    fun togglePump() {
        val databasePush: DatabaseReference = FirebaseHelper.getDatabaseReference(IS_PUMP_ON)
        databasePush.setValue(!_isPump.value)
        _isPump.value = !_isPump.value
    }

    private fun fetchPushStatus() {
        val databasePush: DatabaseReference = FirebaseHelper.getDatabaseReference(IS_PUMP_ON)
        databasePush.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _isPump.value = snapshot.getValue(Boolean::class.java) ?: false
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if necessary
            }
        })
    }

    private fun fetchAutoStatus() {
        val databaseAuto: DatabaseReference = FirebaseHelper.getDatabaseReference(IS_AUTO)
        databaseAuto.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _isAuto.value = snapshot.getValue(Boolean::class.java) ?: false
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if necessary
            }
        })
    }

    private fun fetchSensorData() {
        val databaseSensor: DatabaseReference = FirebaseHelper.getDatabaseReference(SENSOR_DATA)
        databaseSensor.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _humidity.value = snapshot.child(HUMIDITY).getValue(Float::class.java) ?: 0f
                _soilMoisture.value =
                    snapshot.child(SOIL_MOISTURE).getValue(Float::class.java) ?: 0f
                _temperature.value = snapshot.child(TEMPERATURE).getValue(Float::class.java) ?: 0f
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if necessary
            }
        })
    }
}

