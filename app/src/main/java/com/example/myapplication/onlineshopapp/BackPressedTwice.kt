package com.example.myapplication.onlineshopapp

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.os.Handler

class BackPressedTwice:AppCompatActivity() {
    private var doubleBackToExitPressedOnce = false
    fun doubleBackToExit(){
        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }
        this.doubleBackToExitPressedOnce=true
        Toast.makeText(this,"Press back once more to exit",Toast.LENGTH_SHORT).show()
        @Suppress("DEPRECATION")
        Handler().postDelayed({doubleBackToExitPressedOnce=false},2000)

    }
}