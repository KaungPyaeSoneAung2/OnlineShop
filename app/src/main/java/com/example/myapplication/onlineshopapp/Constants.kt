package com.example.myapplication.onlineshopapp

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val Users: String = "users"
    const val NICESHOP_PREFERENCES: String = "NiceShopPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_userName"
    const val EXTRA_USER_DETAIL: String = "extra_user_detail"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val IMAGE_REQUEST_CODE = 1

    const val MALE: String = "Male"
    const val FEMALE: String = "Female"

    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val USERIMAGE: String = "image"
    const val ADDRESS: String = "address"
    const val PROFILE_COMPLETE: String = "profileComple"

    fun showImageChooser(activity: Activity) {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, IMAGE_REQUEST_CODE)
//        getResult.launch(Intent.ACTION_PICK)
    }

    fun getFileExtension(activity: Activity, uri: Uri?):String? {
        /*
        *MimeTypeMap: Two-way map that maps MIME-types to file extensions and vice versa.
        *
        * getSingleton(): Get the singleton instance of MimeTypeMap.
        *
        * getExtesnsionFromMimeType: Return the registered extension for the given MIME type.
        *
        * contentResolver.getType: Retrun the MIME type of the given content URL.
         */
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))
    }

}