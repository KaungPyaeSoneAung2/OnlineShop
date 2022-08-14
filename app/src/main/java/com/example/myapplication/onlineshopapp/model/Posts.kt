package com.example.myapplication.onlineshopapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Posts(
    val userid: String = " ",
    val userName: String = " ",
    var postID: String = " ",
    val image:String =" ",
    val title: String = " ",
    val price: Int = 0,
    val descrption: String = " ",
    val quantity: Int = 0,
) : Parcelable