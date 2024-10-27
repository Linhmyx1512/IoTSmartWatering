package com.minhdn.smartwatering.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.minhdn.smartwatering.data.firebase.FirebaseHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

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
        val databaseAuto: DatabaseReference = FirebaseHelper.getDatabaseReference("is_auto")
        databaseAuto.setValue(!_isAuto.value)
        _isAuto.value = !_isAuto.value
    }

    fun togglePump() {
        val databasePush: DatabaseReference = FirebaseHelper.getDatabaseReference("is_pump_on")
        databasePush.setValue(!_isPump.value)
        _isPump.value = !_isPump.value
    }

    private fun fetchPushStatus() {
        val databasePush: DatabaseReference = FirebaseHelper.getDatabaseReference("is_pump_on")
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
        val databaseAuto: DatabaseReference = FirebaseHelper.getDatabaseReference("is_auto")
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
        val databaseSensor: DatabaseReference = FirebaseHelper.getDatabaseReference("sensor_data")
        databaseSensor.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                _humidity.value = snapshot.child("humidity").getValue(Float::class.java) ?: 0f
                _soilMoisture.value =
                    snapshot.child("soil_moisture").getValue(Float::class.java) ?: 0f
                _temperature.value = snapshot.child("temperature").getValue(Float::class.java) ?: 0f
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error if necessary
            }
        })
    }
}

