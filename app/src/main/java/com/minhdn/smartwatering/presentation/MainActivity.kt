package com.minhdn.smartwatering.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.minhdn.smartwatering.R
import com.minhdn.smartwatering.databinding.ActivityMainBinding
import com.minhdn.smartwatering.data.firebase.FirebaseHelper
import com.minhdn.smartwatering.presentation.history.HistoryFragment
import com.minhdn.smartwatering.presentation.home.HomeFragment
import com.minhdn.smartwatering.presentation.setting.SettingFragment
import com.minhdn.smartwatering.presentation.weather.WeatherFragment

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
    }   

    private fun updateUI() {
        binding.bottomNav.setOnNavigationItemSelectedListener {
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
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.containerMain, fragment)
            .commit()
    }

    companion object {
        private const val TAG = "SmartWatering"
    }
}