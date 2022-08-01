package com.example.myapplication.onlineshopapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivityForgotPasswordBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        binding.buttonForgot.setOnClickListener { }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.topToolBar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)

        }
        binding.topToolBar.setNavigationOnClickListener {
            onBackPressed()
            finish()
        }
    }


    private fun registerUser() {
        if (validateRegisterDetails()) {
            val email: String = binding.emailForgot.text.toString().trim { it <= ' ' }
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Email has sent to your mail", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(
                            this,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        }


    }

    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.emailForgot.text.toString().trim { it <= ' ' }) -> {
                (Toast.makeText(this, "First Name Error", Toast.LENGTH_SHORT).show())
                false
            }
            else -> {
                true
            }
        }
    }

}
