package com.example.myapplication.onlineshopapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.graphics.green
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivityUpdateProfileBinding

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.maleUpdate.setOnClickListener {
            binding.maleUpdate.setBackgroundColor(resources.getColor(R.color.purple_500))
            binding.femaleUpdate.setBackgroundColor(resources.getColor(R.color.white))
        }
        binding.femaleUpdate.setOnClickListener {
            binding.femaleUpdate.setBackgroundColor(resources.getColor(R.color.purple_500))
            binding.maleUpdate.setBackgroundColor(resources.getColor(R.color.white))
        }
    }
}