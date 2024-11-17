package com.minhdn.smartwatering.presentation

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.minhdn.smartwatering.R
import com.minhdn.smartwatering.databinding.ActivityMainBinding
import com.minhdn.smartwatering.presentation.history.HistoryFragment
import com.minhdn.smartwatering.presentation.home.HomeFragment
import com.minhdn.smartwatering.presentation.setting.SettingFragment
import com.minhdn.smartwatering.presentation.weather.WeatherFragment
import com.minhdn.smartwatering.service.WateringService

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val listFragment = listOf(
        HomeFragment(),
        SettingFragment(),
        HistoryFragment(),
        WeatherFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        updateUI()
        replaceFragment(listFragment[0])
        requestNotificationPermissionIfNeed()
    }

    private fun updateUI() {
        binding.bottomNav.apply {
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.homeFragment -> {
                        replaceFragment(listFragment[0])
                    }

                    R.id.weatherFragment -> {
                        replaceFragment(listFragment[1])
                    }

                    R.id.historyFragment -> {
                        replaceFragment(listFragment[2])
                    }

                    R.id.settingFragment -> {
                        replaceFragment(listFragment[3])
                    }
                }
                true
            }
            // bg transparent
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerMain, fragment)
            .commit()
    }

    private fun requestNotificationPermissionIfNeed() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) return
            else {
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (isGranted) {
                        startServiceIfNeed()
                    }
                }.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        } else {
            startServiceIfNeed()
        }
    }

    private fun startServiceIfNeed() {
        WateringService.start(this)
    }

    companion object {
        private const val TAG = "SmartWatering"
    }
}