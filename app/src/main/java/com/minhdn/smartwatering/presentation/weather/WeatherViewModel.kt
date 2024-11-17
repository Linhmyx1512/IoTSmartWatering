package com.minhdn.smartwatering.presentation.weather

import android.location.Geocoder
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.google.android.gms.location.FusedLocationProviderClient
import com.minhdn.smartwatering.data.model.CurrentLocation
import com.minhdn.smartwatering.data.model.CurrentWeather
import com.minhdn.smartwatering.data.model.Forecast
import com.minhdn.smartwatering.data.remote.WeatherService
import com.minhdn.smartwatering.data.repo.WeatherDataRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherViewModel(private val weatherDataRepository: WeatherDataRepository) : ViewModel() {

    private val _currentLocation = MutableLiveData<CurrentLocationDataState>()
    val currentLocation: LiveData<CurrentLocationDataState> = _currentLocation

    fun getCurrentLocation(
        fusedLocationProviderClient: FusedLocationProviderClient,
        geocoder: Geocoder
    ) {
        viewModelScope.launch {
            emitCurrentLocationUIState(isLoading = true)
            weatherDataRepository.getCurrentLocation(
                fusedLocationProviderClient,
                onSuccess = { currentLocation ->
                    updateAddressText(currentLocation, geocoder)
                },
                onFailure = {
                    emitCurrentLocationUIState(errorMessage = "Failed to get location")
                }
            )
        }
    }
    private fun updateAddressText(currentLocation: CurrentLocation, geocoder: Geocoder) {
        viewModelScope.launch {
            val location = weatherDataRepository.updateAddressText(currentLocation, geocoder)
            emitCurrentLocationUIState(currentLocation = location)
        }
    }
    private fun emitCurrentLocationUIState(
        isLoading: Boolean = false,
        currentLocation: CurrentLocation? = null,
        errorMessage: String? = null
    ) {
        val currentLocationDataState = CurrentLocationDataState(
            isLoading = isLoading,
            currentLocation = currentLocation,
            errorMessage = errorMessage
        )
        _currentLocation.value = currentLocationDataState
    }

    data class CurrentLocationDataState(
        val isLoading: Boolean,
        val currentLocation: CurrentLocation?,
        val errorMessage: String?
    )

    private val _weatherData = MutableLiveData<WeatherDataState>()
    val weatherData: LiveData<WeatherDataState> = _weatherData

    fun getWeatherData(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            emitWeatherDataUIState(isLoading = true)
            weatherDataRepository.getWeatherData(latitude, longitude)?.let { remoteWeatherData ->
                val currentWeather = CurrentWeather(
                    icon = remoteWeatherData.current.condition.icon,
                    temperature = remoteWeatherData.current.temperature,
                    wind = remoteWeatherData.current.wind,
                    humidity = remoteWeatherData.current.humidity,
                    chanceOfRain = remoteWeatherData.forecast.forecastDay.first().day.chanceOfRain
                )

                val forecast = remoteWeatherData.forecast.forecastDay.first().hour.map {
                    Forecast(
                        time = getForecastTime(it.time),
                        temperature = it.temperature,
                        feelsLikeTemperature = it.feelsLikeTemperature,
                        chanceOfRain = it.chanceOfRain,
                        icon = it.condition.icon
                    )
                }
                emitWeatherDataUIState(weatherData = currentWeather, forecast = forecast)
            } ?: emitWeatherDataUIState(errorMessage = "Failed to get weather data")
        }
    }

    private fun emitWeatherDataUIState(
        isLoading: Boolean = false,
        weatherData: CurrentWeather? = null,
        forecast: List<Forecast>? = null,
        errorMessage: String? = null
    ) {
        val weatherDataState = WeatherDataState(
            isLoading = isLoading,
            weatherData = weatherData,
            forecast = forecast,
            errorMessage = errorMessage
        )
        _weatherData.value = weatherDataState
    }

    data class WeatherDataState(
        val isLoading: Boolean,
        val weatherData: CurrentWeather?,
        val forecast: List<Forecast>?,
        val errorMessage: String?
    )

    private fun getForecastTime(dateTime: String): String {
        val pattern = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        val date = pattern.parse(dateTime) ?: return dateTime
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(date)
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(weatherService: WeatherService): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                        val weatherDataRepository = WeatherDataRepository(weatherService)
                        return WeatherViewModel(weatherDataRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}