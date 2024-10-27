package com.minhdn.smartwatering.presentation.setting

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.minhdn.smartwatering.R
import com.minhdn.smartwatering.databinding.FragmentSettingBinding

class SettingFragment : Fragment() {

    private val binding: FragmentSettingBinding by lazy {
        FragmentSettingBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

}