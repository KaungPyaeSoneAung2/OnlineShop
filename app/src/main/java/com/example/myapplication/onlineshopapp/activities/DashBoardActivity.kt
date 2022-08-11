package com.example.myapplication.onlineshopapp.activities

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.onlineshopapp.BackPressedTwice
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivityDashBoardBinding

class DashBoardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashBoardBinding
    private val backPressedTwice:BackPressedTwice = BackPressedTwice()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashBoardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_dash_board)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_products, R.id.navigation_dashboard,  R.id.navigation_orders
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        backPressedTwice.doubleBackToExit()
    }

}