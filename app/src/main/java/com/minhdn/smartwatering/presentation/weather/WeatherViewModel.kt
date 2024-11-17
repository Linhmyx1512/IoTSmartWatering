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
import com.minhdn.smartwatering.data.repo.WeatherDataRepository
import kotlinx.coroutines.launch

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

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun provideFactory(weatherDataRepository: WeatherDataRepository): ViewModelProvider.Factory {
            return object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
                        return WeatherViewModel(weatherDataRepository) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
        }
    }
}