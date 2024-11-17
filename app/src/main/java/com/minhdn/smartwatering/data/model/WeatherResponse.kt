package com.minhdn.smartwatering.data.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current") val currentWeather: CurrentWeather,
    @SerializedName("daily") val dailyWeather: List<DailyWeather>
)

data class CurrentWeather(
    @SerializedName("temp") val temp: Double,
    @SerializedName("humidity") val humidity: Double,
    @SerializedName("win_speed") val weather: Double
)

data class DailyWeather(
    @SerializedName("temp") val temp: Temp,
    @SerializedName("pop") val pop: Double
)

data class Temp(
    @SerializedName("temp_day") val tempDay: Double
)