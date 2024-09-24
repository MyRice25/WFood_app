package com.example.wavesoffood.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.wavesoffood.Fragment.NotificationFragment
import com.example.wavesoffood.R
import com.example.wavesoffood.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var NavController = findNavController(R.id.fgContainerView)
        var bottomNav = findViewById<BottomNavigationView>(R.id.bottomNavView)
        bottomNav.setupWithNavController(NavController)
        binding.icNotification.setOnClickListener{
            val bottomSheetDialog = NotificationFragment()
            bottomSheetDialog.show(supportFragmentManager,"Test")
        }
    }
}