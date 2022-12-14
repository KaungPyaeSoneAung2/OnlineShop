package com.example.myapplication.onlineshopapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val user_id: String = " ",
    val user_name: String = " ",
    val title: String = " ",
    val price: String = " ",
    val description: String = " ",
    val stock_quantity: String = " ",
    val image: String = " ",
    var product_id: String = " ",
    var phNumber: String =" ",
    var address: String=" ",
    var email:String=" "
) : Parcelable