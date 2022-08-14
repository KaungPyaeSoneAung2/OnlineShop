package com.example.myapplication.onlineshopapp.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.GlideLoader
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivityProductDetailsBinding
import com.example.myapplication.onlineshopapp.firestore.FireStore
import com.example.myapplication.onlineshopapp.model.Posts

class ProductDetailsActivity : AppCompatActivity() {
    private var mProductId: String = ""
    private lateinit var binding:ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        //This call the parent constructor
        super.onCreate(savedInstanceState)
        // This is used to align the xml view to this class
        binding=ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product Id", mProductId)
        }

        setupActionBar()

        getProductDetails()
    }

    /**
     * A function for actionBar Setup.
     */
    private fun setupActionBar() {

        setSupportActionBar(binding.productDetailsToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }

        binding.productDetailsToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    /**
     * A function to call the firestore class function that will get the product details from cloud firestore based on the product id.
     */
    private fun getProductDetails() {


        // Call the function of FirestoreClass to get the product details.
        FireStore().getProductDetails(this@ProductDetailsActivity, mProductId)
    }

    /**
     * A function to notify the success result of the product details based on the product id.
     *
     * @param product A model class with product details.
     */
    fun productDetailsSuccess(product: Posts) {


        // Populate the product details in the UI.
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            binding.productDetailImage
        )

        binding.productDetailsTitle.text = product.title
        binding.productDetailsPrice.text = "$${product.price}"
        binding.productDetailsDescription.text = product.descrption
        binding.productDetailsQuantity.text = product.quantity.toString()
    }
}