package com.example.myapplication.onlineshopapp

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore

object Constants {
    const val Users: String = "users"
    const val NICESHOP_PREFERENCES: String = "NiceShopPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_userName"
    const val EXTRA_USER_DETAIL: String ="extra_user_detail"
    const val READ_STORAGE_PERMISSION_CODE=2
    const val IMAGE_REQUEST_CODE=1

    const val MALE:String="Male"
    const val FEMALE:String="Female"

    const val MOBILE:String="mobile"
    const val GENDER:String="gender"

    fun showImageChooser(activity: Activity){
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE)
//        getResult.launch(Intent.ACTION_PICK)
    }

}