package com.minhdn.smartwatering.presentation.weather

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.gson.Gson
import com.minhdn.smartwatering.BuildConfig
import com.minhdn.smartwatering.data.model.CurrentLocation
import com.minhdn.smartwatering.data.prefs.SharedPreferencesHelper
import com.minhdn.smartwatering.data.remote.RetrofitClient
import com.minhdn.smartwatering.data.remote.WeatherService
import com.minhdn.smartwatering.data.repo.WeatherDataRepository
import com.minhdn.smartwatering.databinding.FragmentWeatherBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class WeatherFragment : Fragment() {

    private val binding: FragmentWeatherBinding by lazy {
        FragmentWeatherBinding.inflate(layoutInflater)
    }

    private lateinit var weatherViewModel: WeatherViewModel
    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(requireContext())
    }
    private val geocoder by lazy { Geocoder(requireContext()) }

    private lateinit var sharedPrefHelper: SharedPreferencesHelper

    private val weatherDataAdapter = WeatherDataAdapter(
        onLocationClick = {
            showLocationOptions()
        }
    )

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            getCurrentLocation()
        } else {
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private var isInitialLocationSet: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weatherService = RetrofitClient.getWeatherService()
        weatherViewModel = ViewModelProvider(
            this,
            WeatherViewModel.provideFactory(weatherService)
        )[WeatherViewModel::class.java]

        sharedPrefHelper = SharedPreferencesHelper.getInstance(requireContext())

        setWeatherDataAdapter()
        setObservers()
        setListeners()

        if (!isInitialLocationSet) {
            setCurrentLocation(currentLocation = sharedPrefHelper.getCurrentLocation())
            isInitialLocationSet = true
        }
    }

    private fun setListeners() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            setCurrentLocation(sharedPrefHelper.getCurrentLocation())
        }
    }

    private fun setObservers() {
        with(weatherViewModel) {
            currentLocation.observe(viewLifecycleOwner) {
                val currentLocationDataState = it ?: return@observe
                if (currentLocationDataState.isLoading) {
                    showLoading()
                }
                currentLocationDataState.currentLocation?.let { currentLocation ->
                    hideLoading()
                    sharedPrefHelper.saveCurrentLocation(currentLocation)
                    setCurrentLocation(currentLocation)
                }
                currentLocationDataState.errorMessage?.let { errorMessage ->
                    hideLoading()
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }

            weatherData.observe(viewLifecycleOwner) {
                val weatherDataState = it ?: return@observe
                binding.swipeRefreshLayout.isRefreshing = weatherDataState.isLoading
                weatherDataState.weatherData?.let { weatherData ->
                    Log.d("WeatherFragment", "Weather: $weatherData")
                    weatherDataAdapter.setCurrentWeather(weatherData)
                }
                Log.d("WeatherFragment", "Forecast: ${weatherDataState.forecast}")
                weatherDataState.forecast?.let { forecast ->
                    Log.d("WeatherFragment", "Forecast: $forecast")
                    weatherDataAdapter.setForecastData(forecast)
                }
                weatherDataState.errorMessage?.let { errorMessage ->
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setWeatherDataAdapter() {
        binding.rvWeatherData.itemAnimator = null
        binding.rvWeatherData.adapter = weatherDataAdapter
    }

    private fun setCurrentLocation(currentLocation: CurrentLocation? = null) {
        weatherDataAdapter.setCurrentLocation(currentLocation ?: CurrentLocation())
        currentLocation?.let {
            getWeatherData(it)
        }
    }

    private fun getCurrentLocation() {
        weatherViewModel.getCurrentLocation(
            fusedLocationProviderClient = fusedLocationProviderClient,
            geocoder = geocoder
        )
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun proceedWithCurrentLocation() {
        if (isLocationPermissionGranted()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }
    }

    private fun showLocationOptions() {
        val options = arrayOf("Current location", "Search location manually")
        AlertDialog.Builder(requireContext())
            .setTitle("Choose location method")
            .setItems(options) { _, which ->
                when (which) {
                    0 -> proceedWithCurrentLocation()
                    1 -> Toast.makeText(requireContext(), "Choose location", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    private fun showLoading() {
        with(binding) {
            rvWeatherData.visibility = View.GONE
            swipeRefreshLayout.isEnabled = false
            swipeRefreshLayout.isRefreshing = true
        }
    }

    private fun hideLoading() {
        with(binding) {
            rvWeatherData.visibility = View.VISIBLE
            swipeRefreshLayout.isEnabled = true
            swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getWeatherData(currentLocation: CurrentLocation) {
        if (currentLocation.latitude != null && currentLocation.longitude != null) {
            weatherViewModel.getWeatherData(
                latitude = currentLocation.latitude,
                longitude = currentLocation.longitude
            )
        }
    }
}