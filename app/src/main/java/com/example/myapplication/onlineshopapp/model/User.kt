package com.example.myapplication.onlineshopapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class User (
    val id: String="",
    val firstName: String = " ",
    val lastName:String =" ",
    val email: String=" ",
    val image: String=" ",
    val mobile: Long=0,
    val gender: Int=0,
    val address: String=" ",
    val profileComple: Int = 0,
    ): Parcelable