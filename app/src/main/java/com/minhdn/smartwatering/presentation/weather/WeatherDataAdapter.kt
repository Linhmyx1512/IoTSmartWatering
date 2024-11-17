package com.minhdn.smartwatering.presentation.weather

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.minhdn.smartwatering.data.model.CurrentLocation
import com.minhdn.smartwatering.data.model.WeatherData
import com.minhdn.smartwatering.databinding.ItemCurrentLocationBinding

class WeatherDataAdapter(
    private val onLocationClick: () -> Unit
) : RecyclerView.Adapter<WeatherDataAdapter.CurrentLocationViewHolder>() {
    private val weatherData = mutableListOf<WeatherData>()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentLocationViewHolder {
        return CurrentLocationViewHolder(
            ItemCurrentLocationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    override fun onBindViewHolder(holder: CurrentLocationViewHolder, position: Int) {
        holder.bind(weatherData[position] as CurrentLocation)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<WeatherData>) {
        weatherData.clear()
        weatherData.addAll(data)
        notifyDataSetChanged()
    }
}