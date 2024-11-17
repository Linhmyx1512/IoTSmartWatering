package com.minhdn.smartwatering.presentation.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.minhdn.smartwatering.R
import com.minhdn.smartwatering.ReminderFactory
import com.minhdn.smartwatering.data.prefs.SharedPreferencesHelper
import com.minhdn.smartwatering.databinding.FragmentSettingBinding
import kotlinx.coroutines.launch

class SettingFragment : Fragment() {

    private val binding: FragmentSettingBinding by lazy {
        FragmentSettingBinding.inflate(layoutInflater)
    }

    private val viewmodel: SettingViewmodel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        fetchThreshold()
        initListeners()
    }

    private fun initUI() {
        binding.npHour.apply {
            minValue = 0
            maxValue = 23
            wrapSelectorWheel = true
        }

        binding.npMinute.apply {
            minValue = 0
            maxValue = 59
            wrapSelectorWheel = true
        }

        binding.sbAlarm.apply {
            setOnCheckedChangeListener { _, isChecked ->
                trackTintList = if (isChecked) {
                    ContextCompat.getColorStateList(requireContext(), R.color.main_color)
                } else {
                    ContextCompat.getColorStateList(requireContext(), R.color.gray)
                }

                if (isChecked) {
                    ReminderFactory(requireContext()).pushReminder(
                        binding.sbEveryday.isChecked,
                        binding.npHour.value,
                        binding.npMinute.value
                    )
                } else {
                    ReminderFactory(requireContext()).cancelReminder(binding.sbEveryday.isChecked)
                }
            }
        }

        binding.sbEveryday.apply {
            setOnCheckedChangeListener { _, isChecked ->
                trackTintList = if (isChecked) {
                    ContextCompat.getColorStateList(requireContext(), R.color.main_color)
                } else {
                    ContextCompat.getColorStateList(requireContext(), R.color.gray)
                }
                if (binding.sbAlarm.isChecked) {
                    ReminderFactory(requireContext()).updateReminder(
                        isChecked,
                        binding.npHour.value,
                        binding.npMinute.value
                    )
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener {
            val upperTemperature = binding.edtTemperatureUpper.text.toString().toFloat()
            val lowerTemperature = binding.edtTemperatureLower.text.toString().toFloat()
            val upperSoilMoisture = binding.edtSoilMoistureUpper.text.toString().toFloat()
            val lowerSoilMoisture = binding.edtSoilMoistureLower.text.toString().toFloat()

            viewmodel.writeUpperTemperatureThreshold(upperTemperature)
            viewmodel.writeLowerTemperatureThreshold(lowerTemperature)
            viewmodel.writeUpperSoilMoistureThreshold(upperSoilMoisture)
            viewmodel.writeLowerSoilMoistureThreshold(lowerSoilMoisture)

            Toast.makeText(requireContext(), "Saved successfully", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchThreshold() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.upperTemperatureThreshold.collect { upperTemperatureThreshold ->
                binding.edtTemperatureUpper.setText(upperTemperatureThreshold.toString())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.lowerTemperatureThreshold.collect { lowerTemperatureThreshold ->
                binding.edtTemperatureLower.setText(lowerTemperatureThreshold.toString())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.upperSoilMoistureThreshold.collect { upperSoilMoistureThreshold ->
                binding.edtSoilMoistureUpper.setText(upperSoilMoistureThreshold.toString())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewmodel.lowerSoilMoistureThreshold.collect { lowerSoilMoistureThreshold ->
                binding.edtSoilMoistureLower.setText(lowerSoilMoistureThreshold.toString())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        fetchThreshold()
    }

}