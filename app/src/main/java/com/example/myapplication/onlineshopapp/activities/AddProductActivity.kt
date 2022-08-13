package com.example.myapplication.onlineshopapp.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.GlideLoader
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivityAddProductBinding
import com.example.myapplication.onlineshopapp.firestore.FireStore
import com.example.myapplication.onlineshopapp.model.Posts
import java.io.IOException


class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private var mSelectedImageFileUri: Uri? = null

    // A global variable for uploaded product image URL.
    private var mProductImageURL: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.imageAddProduct.setOnClickListener{
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this@AddProductActivity)
            } else {
                /*Requests permissions to be granted to this application. These permissions
                 must be requested in your manifest, they should not be granted to your app,
                 and they should have protection level*/
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Constants.showImageChooser(this@AddProductActivity)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    "Request was denied",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK
            && requestCode == Constants.IMAGE_REQUEST_CODE
            && data!!.data != null
        ) {

            // Replace the add icon with edit icon once the image is selected.
            binding.imageAddSmall.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_baseline_edit_24
                )
            )

            // The uri of selection image from phone storage.
            mSelectedImageFileUri = data.data!!

            try {
                // Load the product image in the ImageView.
                GlideLoader(this@AddProductActivity).loadProductPicture(
                    mSelectedImageFileUri!!,
                    binding.imageAddProduct
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.addProductToolBar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        binding.addProductToolBar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to validate the product details.
     */
    private fun validateProductDetails(): Boolean {
        return when {

            mSelectedImageFileUri == null -> {
                Toast.makeText(this, "Please select product image", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(binding.captionProduct.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter product title", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(binding.descriptionProduct.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter product description", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(binding.priceProduct.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter product price", Toast.LENGTH_SHORT).show()
                false
            }

            TextUtils.isEmpty(binding.qtyProduct.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter product quantity", Toast.LENGTH_SHORT).show()

                false
            }
            else -> {
                true
            }
        }
    }

    /**
     * A function to upload the selected product image to firebase cloud storage.
     */
    private fun uploadProductImage() {


        FireStore().uploadImageToCloudStorage(
            this@AddProductActivity,
            mSelectedImageFileUri,
            Constants.PRODUCT_IMAGE
        )
    }

    /**
     * A function to get the successful result of product image upload.
     */
    fun imageUploadSuccess(imageURL: String) {

        // Initialize the global image url variable.
        mProductImageURL = imageURL

        uploadProductDetails()
    }

    private fun uploadProductDetails() {

        // Get the logged in username from the SharedPreferences that we have stored at a time of login.
        val username =
            this.getSharedPreferences(Constants.NICESHOP_PREFERENCES, Context.MODE_PRIVATE)
                .getString(Constants.LOGGED_IN_USERNAME, "")!!

        // Here we get the text from editText and trim the space
        val product = Posts(
            FireStore().getCurrentUserID(),
            username,
            binding.captionProduct.text.toString().trim { it <= ' ' },
            binding.priceProduct.text.toString().trim { it <= ' ' },
            binding.descriptionProduct.text.toString().trim { it <= ' ' },
            binding.qtyProduct.text.toString().trim { it <= ' ' }.toInt(),
            mProductImageURL
        )

        FireStore().uploadProductDetails(this@AddProductActivity, product)
    }

    /**
     * A function to return the successful result of Product upload.
     */
    fun productUploadSuccess() {
        Toast.makeText(
            this@AddProductActivity,
            "Product uploaded successfully",
            Toast.LENGTH_SHORT
        ).show()

        finish()
    }
}