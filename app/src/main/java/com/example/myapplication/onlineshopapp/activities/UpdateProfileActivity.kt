package com.example.myapplication.onlineshopapp.activities

import com.example.myapplication.onlineshopapp.GlideLoader
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivityUpdateProfileBinding
import com.example.myapplication.onlineshopapp.firestore.FireStore
import com.example.myapplication.onlineshopapp.model.User
import java.io.IOException

class UpdateProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateProfileBinding
    private lateinit var userDetails: User
    private var selectedImageUri: Uri? = null
    private var selectedImageURL: String = " "
//    var startForResult: ActivityResultLauncher<Intent> =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
//            ActivityResultCallback<ActivityResult> {
//                @Override
//                fun onActivityResult(result: ActivityResult) {
//                    if (result.resultCode == Activity.RESULT_OK) {
//                        if (result == Constants.IMAGE_REQUEST_CODE) {
//                            if (data != null) {
//                                try {
//                                    val selectedImageFileUri = data.data!!
//                                    binding.imageUserUpdate.setImageURI(
//                                        Uri.parse(
//                                            selectedImageFileUri.toString()
//                                        )
//                                    )
//                                } catch (e: IOException) {
//                                    e.printStackTrace()
//                                    Toast.makeText(
//                                        this,
//                                        "iamge selection failed",
//                                        Toast.LENGTH_SHORT
//                                    ).show()
//                                }
//                            } else {
//                                Toast.makeText(this, "there was no data", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    }
//                }
//            }
//        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getUserDetails()
        genderSelectioinButton()
//        binding.firstNameUpdate.isEnabled = false
//        binding.firstNameUpdate.setText(userDetails.firstName)
//
//        binding.lastNameUpdate.isEnabled = false
//        binding.lastNameUpdate.setText(userDetails.lastName)
//
//        binding.emailUpdate.isEnabled = false
//        binding.emailUpdate.setText(userDetails.email)


        binding.imageUserUpdate.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                //Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show()
                Constants.showImageChooser(this)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding.saveButtonUpdate.setOnClickListener {

            if (validateUserInfos()) {

                if (selectedImageUri != null) {
                    FireStore().uploadImageToCloudStorage(this, selectedImageUri,Constants.USER_PROFILE_IMAGE)
                }
                else{
                    updateUserProfileDetails()
                }

            }
        }

        binding.backButton.setOnClickListener{
            onBackPressed()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
                Constants.showImageChooser(this@UpdateProfileActivity)
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        selectedImageUri = data.data!!
                        //binding.imageUserUpdate.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(
                            selectedImageUri.toString(),
                            binding.imageUserUpdate
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "iamge selection failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "there was no data", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "request code didn't match", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "result wasn't okay", Toast.LENGTH_SHORT).show()
        }
    }


    private fun validateUserInfos(): Boolean {
        return when {
            TextUtils.isEmpty(binding.phNumberUpdate.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun updateUserProfileDetails() {
        val userHashMap = HashMap<String, Any>()
        val genderResult = genderSelectioinButton()
        val mobileNumber = binding.phNumberUpdate.text.toString().trim { it <= ' ' }
        if (mobileNumber.isNotEmpty()) {
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        val gender = genderResult
        userHashMap[Constants.GENDER] = gender

        val address = binding.addressUpdate.text.toString()
        if (address.isNotEmpty()) {
            userHashMap[Constants.ADDRESS] = address
        }

        userHashMap[Constants.PROFILE_COMPLETE] = 1
        FireStore().updateUserProfileData(this, userHashMap)

    }


    fun userProfileUpdateSuccess(){
        startActivity(Intent(this, DashBoardActivity::class.java))
        finish()
    }

    fun imageUploadSuccess(imageURL: String) {
        selectedImageURL = imageURL
        Log.e("Downloadable URL", imageURL.toString())
        updateUserProfileDetails()
    }


    private fun genderSelectioinButton(): Int {
        var genderResult=0
        binding.maleUpdate.setOnClickListener {
            binding.maleUpdate.setBackgroundColor(resources.getColor(R.color.purple_500))
            binding.femaleUpdate.setBackgroundColor(resources.getColor(R.color.white))
            genderResult = 0
            Toast.makeText(this, genderResult.toString(), Toast.LENGTH_SHORT).show()
        }
        binding.femaleUpdate.setOnClickListener {
            binding.femaleUpdate.setBackgroundColor(resources.getColor(R.color.purple_500))
            binding.maleUpdate.setBackgroundColor(resources.getColor(R.color.white))
            genderResult = 1
            Toast.makeText(this, genderResult.toString(), Toast.LENGTH_SHORT).show()
        }

        return genderResult
    }


    private fun getUserDetails() {
        FireStore().getUserDetails(this)
    }

    //Get submitted user details or change As you wish
    @SuppressLint("SetTextI18n")
    fun UserDetailSuccess(user: User) {
        GlideLoader(this).loadUserPicture(user.image, binding.imageUserUpdate)
        if(user.firstName!=" "){
            binding.firstNameUpdate.setText(user.firstName)
        }
        if(user.lastName!=" "){
            binding.lastNameUpdate.setText(user.lastName)
        }
        binding.emailUpdate.setText(user.email)
        binding.emailUpdate.isEnabled=false

        if(user.mobile!=0L){
            binding.phNumberUpdate.setText(user.mobile.toString())
        }
        if(user.address!=" ") {
            binding.addressUpdate.setText(user.address)
        }
    }
    }

//    private val getResult =
//        registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//        ) {
//            if (it.resultCode == Activity.RESULT_OK) {
//                if (it.resultCode == Constants.IMAGE_REQUEST_CODE) {
//                    if (it.data != null ) {
//                        try {
//                            val selectedImageFileUri = it.data!!
//                            binding.imageUserUpdate.setImageURI(Uri.parse(selectedImageFileUri.toString()))
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                            Toast.makeText(this, "iamge selection failed", Toast.LENGTH_SHORT)
//                                .show()
//                        }
//                    } else {
//                        Toast.makeText(this, "there was no data", Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//            else{
//                Toast.makeText(this, "Result wasn't OK", Toast.LENGTH_SHORT).show()
//            }
//        }
