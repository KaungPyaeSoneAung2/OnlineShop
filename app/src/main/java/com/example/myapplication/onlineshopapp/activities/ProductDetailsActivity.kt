package com.example.myapplication.onlineshopapp.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.GlideLoader
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivityProductDetailsBinding
import com.example.myapplication.onlineshopapp.firestore.FireStore
import com.example.myapplication.onlineshopapp.model.Product
import com.example.myapplication.onlineshopapp.model.User

class ProductDetailsActivity : AppCompatActivity() {
    private var mProductId: String = ""
    private lateinit var binding:ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
            Log.i("Product Id", mProductId)
        }

        setupActionBar()

        getProductDetails()
        getUserDetails()
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.productDetailsToolbar)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        }
        binding.productDetailsToolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getProductDetails() {


        FireStore().getProductDetails(this@ProductDetailsActivity, mProductId)
    }

    @SuppressLint("SetTextI18n")
    fun productDetailsSuccess(product: Product) {
        GlideLoader(this@ProductDetailsActivity).loadProductPicture(
            product.image,
            binding.productDetailImage
        )

        binding.productDetailsTitle.text = product.title
        binding.productDetailsPrice.text = "${product.price}MMk"
        binding.productDetailsDescription.text = product.description
        binding.productDetailsQuantity.text = "Stock :${product.stock_quantity}"
    }

    private fun getUserDetails() {
        FireStore().getUserDetails(this)
    }
    @SuppressLint("SetTextI18n")
    fun UserDetailSuccess(user: User) {
        if(user.mobile!=0L){
        binding.productContactNumber.text= "Contact Number: ${user.mobile}"}
        else{
            binding.productContactNumber.text="Contact Email : ${user.email}"
        }
        if(user.address!=" "){
            binding.addressDetails.text= "Address: ${user.address}"}
        else{
            binding.addressDetails.text="Address : Not submitted}"
        }
    }
}