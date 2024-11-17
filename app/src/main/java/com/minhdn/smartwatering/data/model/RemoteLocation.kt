package com.minhdn.smartwatering.data.model

data class RemoteLocation (
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double,
)