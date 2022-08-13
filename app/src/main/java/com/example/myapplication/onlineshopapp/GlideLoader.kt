package com.example.myapplication.onlineshopapp

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.myapplication.onlineshopapp.R
import java.io.IOException

class GlideLoader(val context:Context) {
    fun loadUserPicture(imageUri: String, imageView: ImageView){
        try {
            Glide.with(context)
                .load(imageUri)
                .centerCrop()
                .placeholder(R.drawable.ic_baseline_account_box_24)
                .into(imageView)
        }catch (
            e: IOException
        ){
            e.printStackTrace()
        }
    }
    fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            // Load the user image in the ImageView.
            Glide
                .with(context)
                .load(image) // Uri or URL of the image
                .centerCrop() // Scale type of the image.
                .into(imageView) // the view in which the image will be loaded.
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}