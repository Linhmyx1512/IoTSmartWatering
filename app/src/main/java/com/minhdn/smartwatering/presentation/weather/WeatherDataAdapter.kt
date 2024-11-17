package com.minhdn.smartwatering.presentation.weather

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.minhdn.smartwatering.data.model.CurrentLocation
import com.minhdn.smartwatering.data.model.CurrentWeather
import com.minhdn.smartwatering.data.model.Forecast
import com.minhdn.smartwatering.data.model.WeatherData
import com.minhdn.smartwatering.databinding.ItemContainerCurrentWeatherBinding
import com.minhdn.smartwatering.databinding.ItemContainerForecastBinding
import com.minhdn.smartwatering.databinding.ItemCurrentLocationBinding

class WeatherDataAdapter(
    private val onLocationClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private companion object {
        const val VIEW_TYPE_CURRENT_LOCATION = 0
        const val VIEW_TYPE_CURRENT_WEATHER = 1
        const val VIEW_TYPE_FORECAST = 2
    }

    private val weatherData = mutableListOf<WeatherData>()

    fun setCurrentLocation(currentLocation: CurrentLocation) {
        if (weatherData.isEmpty()) {
            weatherData.add(VIEW_TYPE_CURRENT_LOCATION, currentLocation)
            notifyItemInserted(VIEW_TYPE_CURRENT_LOCATION)
        } else {
            weatherData[VIEW_TYPE_CURRENT_LOCATION] = currentLocation
            notifyItemChanged(VIEW_TYPE_CURRENT_LOCATION)
        }
    }

    fun setCurrentWeather(currentWeather: CurrentWeather) {
        if (weatherData.getOrNull(VIEW_TYPE_CURRENT_WEATHER) != null) {
            weatherData[VIEW_TYPE_CURRENT_WEATHER] = currentWeather
            notifyItemChanged(VIEW_TYPE_CURRENT_WEATHER)
        } else {
            weatherData.add(VIEW_TYPE_CURRENT_WEATHER, currentWeather)
            notifyItemInserted(VIEW_TYPE_CURRENT_WEATHER)
        }
    }

    fun setForecastData(forecast: List<Forecast>) {
        weatherData.removeAll { it is Forecast }
        notifyItemRangeRemoved(VIEW_TYPE_FORECAST, weatherData.size)
        weatherData.addAll(VIEW_TYPE_FORECAST, forecast)
        notifyItemRangeChanged(VIEW_TYPE_FORECAST, weatherData.size)
    }

    inner class CurrentLocationViewHolder(private val binding: ItemCurrentLocationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentLocation: CurrentLocation) {
           with(binding) {
               tvCurrentDate.text = currentLocation.date
               tvCurrentLocation.text = currentLocation.location
               ivCurrentLocation.setOnClickListener {
                   onLocationClick()
               }
               tvCurrentLocation.setOnClickListener {
                   onLocationClick()
               }
           }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_CURRENT_LOCATION -> CurrentLocationViewHolder(
                ItemCurrentLocationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            VIEW_TYPE_FORECAST -> ForecastViewHolder(
                ItemContainerForecastBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )

            else -> CurrentWeatherViewHolder(
                ItemContainerCurrentWeatherBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CurrentLocationViewHolder -> holder.bind(weatherData[position] as CurrentLocation)
            is CurrentWeatherViewHolder -> holder.bind(weatherData[position] as CurrentWeather)
            is ForecastViewHolder -> holder.bind(weatherData[position] as Forecast)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (weatherData[position]) {
            is CurrentLocation -> VIEW_TYPE_CURRENT_LOCATION
            is CurrentWeather -> VIEW_TYPE_CURRENT_WEATHER
            is Forecast -> VIEW_TYPE_FORECAST
        }
    }

    inner class CurrentWeatherViewHolder(
        private val binding: ItemContainerCurrentWeatherBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(currentWeather: CurrentWeather) {
            with(binding) {
                ivIcon.load("https:${currentWeather.icon}"){crossfade(true)}
                tvTemperature.text = String.format("%s\u00B0C", currentWeather.temperature)
                tvWind.text = String.format("%s km/h", currentWeather.wind)
                tvHumidity.text = String.format("%s%%", currentWeather.humidity)
                tvChanceOfRain.text = String.format("%s%%", currentWeather.chanceOfRain)
            }
        }
    }

    inner class ForecastViewHolder(
        private val binding: ItemContainerForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(forecast: Forecast) {
            with(binding) {
                tvTime.text = forecast.time
                tvTemperature.text = String.format("%s\u00B0C", forecast.temperature)
                tvFeelsLikeTemperature.text = String.format("%s\u00B0C", forecast.feelsLikeTemperature)
                tvChanceOfRain.text = String.format("%s%%", forecast.chanceOfRain)
                ivIcon.load("https:${forecast.icon}"){crossfade(true)}
            }
        }
    }
}