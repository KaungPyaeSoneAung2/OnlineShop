package com.example.myapplication.onlineshopapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivityLoginBinding
import com.example.myapplication.onlineshopapp.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.havAccountLogin.setOnClickListener{
            Intent(this,LoginActivity::class.java).also { startActivity(it) }
        }

    }

    private fun setupActionBar(){
        setSupportActionBar()
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.back)

        }
        .setNavigationOnClickListener{onBackPressed()}
    }
}