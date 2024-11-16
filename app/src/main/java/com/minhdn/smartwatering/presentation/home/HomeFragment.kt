package com.minhdn.smartwatering.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.minhdn.smartwatering.R
import com.minhdn.smartwatering.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private val binding: FragmentHomeBinding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.humidity.collect { humidity ->
                binding.tvHumidity.text = humidity.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.soilMoisture.collect { soilMoisture ->
                binding.tvSoilMoisture.text = soilMoisture.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.temperature.collect { temperature ->
                binding.tvTemperature.text = temperature.toString()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isAuto.collect { isAuto ->
                updateSwitch(isAuto)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isPump.collect { isPump ->
                updatePump(isPump)
            }
        }
    }

    private fun updateSwitch(isOn: Boolean) {
        binding.swAutoWatering.apply {
            trackTintList = if (isOn) {
                ContextCompat.getColorStateList(requireContext(), R.color.main_color)
            } else {
                ContextCompat.getColorStateList(requireContext(), R.color.gray)
            }
        }
    }

    private fun updatePump(isPump: Boolean) {
        binding.tvTurnOnOff.apply {
            text = if (isPump) {
                "ON"
            } else {
                "OFF"
            }
            backgroundTintList = if (isPump) {
                ContextCompat.getColorStateList(requireContext(), R.color.main_color)
            } else {
                ContextCompat.getColorStateList(requireContext(), R.color.gray)
            }
        }
    }


    private fun setOnClickListeners() {
        binding.swAutoWatering.setOnCheckedChangeListener { _, _ ->
            viewModel.toggleAuto()
        }

        binding.tvTurnOnOff.setOnClickListener {
            viewModel.togglePump()
        }
    }
}