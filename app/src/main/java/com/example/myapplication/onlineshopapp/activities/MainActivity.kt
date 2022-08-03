package com.example.myapplication.onlineshopapp.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar.DISPLAY_SHOW_TITLE
import androidx.appcompat.app.ActionBar.DISPLAY_USE_LOGO
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivityMainBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setupActionBar()
        val sharedPreferences = getSharedPreferences(Constants.NICESHOP_PREFERENCES,Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME," ")!!
        binding.textviewUserName.text= "Hello $userName"
    }

//    private fun setupActionBar(){
//        setSupportActionBar(binding.topToolBar)
//        val actionBar=supportActionBar
//        if(actionBar!=null){
//            actionBar.setDisplayHomeAsUpEnabled(true)
//            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
//
//        }
//        binding.topToolBar.setNavigationOnClickListener{
//            Intent(this,LoginActivity::class.java).also { startActivity(it) }
//            finish()
//        }
//
//    }






}