package com.minhdn.smartwatering.data.remote

import com.minhdn.smartwatering.BuildConfig
import com.minhdn.smartwatering.data.model.RemoteWeatherData
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {
    @GET("forecast.json")
    suspend fun getWeatherData(
        @Query("key") key: String = BuildConfig.OPEN_WEATHER_API_KEY,
        @Query("q") query: String
    ) : Response<RemoteWeatherData>
}