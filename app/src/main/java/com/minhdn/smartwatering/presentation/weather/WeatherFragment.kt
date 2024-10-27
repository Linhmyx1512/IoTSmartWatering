package com.minhdn.smartwatering.presentation.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.minhdn.smartwatering.R
import com.minhdn.smartwatering.databinding.FragmentWeatherBinding

class WeatherFragment : Fragment() {

    private val binding: FragmentWeatherBinding by lazy {
        FragmentWeatherBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

}