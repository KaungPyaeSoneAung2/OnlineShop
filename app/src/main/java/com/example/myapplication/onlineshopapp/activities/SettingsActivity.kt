package com.example.myapplication.onlineshopapp.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.example.myapplication.onlineshopapp.GlideLoader
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivitySettingsBinding
import com.example.myapplication.onlineshopapp.firestore.FireStore
import com.example.myapplication.onlineshopapp.model.User
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.settingBackButton.setOnClickListener {
            onBackPressed()
        }
        binding.logoutSetting.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
        binding.editProfile.setOnClickListener {
            Intent(this,UpdateProfileActivity::class.java).also { startActivity(it) }
        }
        getUserDetails()
    }

    private fun getUserDetails() {
        FireStore().getUserDetails(this)
    }

    fun UserDetailsSuccess(user: User) {
        GlideLoader(this).loadUserPicture(user.image, binding.imageUserDashboard)
        binding.NameDashboard.text = "${user.firstName} ${user.lastName}"
        binding.NameDashboard.setOnClickListener {
            Toast.makeText(DashBoardActivity(), "Hello", Toast.LENGTH_SHORT).show()
        }
        binding.emailDashboard.text = user.email
        binding.phNumberDashboard.text = user.mobile.toString()
        binding.addressDashboard.text = user.address
    }
}