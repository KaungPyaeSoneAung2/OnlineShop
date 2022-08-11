package com.example.myapplication.onlineshopapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Posts (
    val userid: String=User().id,
    val postID:String=" ",
    val firstName: String = User().firstName,
    val postimage: String=" ",
    val postCapt:String=" ",
    val postDesc: String=" ",
    val mobile: Long=User().mobile,
    val address: String=User().address,
): Parcelable