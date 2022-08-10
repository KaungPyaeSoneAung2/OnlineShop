package com.example.myapplication.onlineshopapp.activities

import GlideLoader
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
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
        val genderResult=genderSelectioinButton(0)
        userDetails = User()
        if (intent.hasExtra(Constants.EXTRA_USER_DETAIL)) {
            userDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAIL)!!
        }
        binding.firstNameUpdate.isEnabled = false
        binding.firstNameUpdate.setText(userDetails.firstName)

        binding.lastNameUpdate.isEnabled = false
        binding.lastNameUpdate.setText(userDetails.lastName)

        binding.emailUpdate.isEnabled=false
        binding.emailUpdate.setText(userDetails.email)

        binding.saveButtonUpdate.setOnClickListener {
            FireStore().uploadImageToCloudStorage(this,selectedImageUri)
            if(validateUserInfos()){
                val userHashMap= HashMap<String,Any>()
                val mobileNumber = binding.phNumberUpdate.text.toString().trim{ it <= ' '}
                if (mobileNumber.isNotEmpty()){
                    userHashMap[Constants.MOBILE]=mobileNumber.toLong()
                }

                val userImage=selectedImageUri.toString()
                if (userImage.isNotEmpty()){
                    userHashMap[Constants.USERIMAGE]=userImage
                }

                val gender = genderResult
                userHashMap[Constants.GENDER]=gender
                FireStore().updateUserProfileData(this,userHashMap)
            }

        }

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
                        GlideLoader(this).loadUserPicture(selectedImageUri!!,binding.imageUserUpdate)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(this, "iamge selection failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "there was no data", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "request code didn't match", Toast.LENGTH_SHORT).show()}
        }
        else{
            Toast.makeText(this, "result wasn't okay", Toast.LENGTH_SHORT).show()}
    }


    private fun validateUserInfos():Boolean{
        return when{
            TextUtils.isEmpty(binding.phNumberUpdate.text.toString().trim{ it <= ' '}) ->{
                Toast.makeText(this, "Please enter your mobile number", Toast.LENGTH_SHORT).show()
                false
            }
            else ->{
                true
            }
        }
    }
    fun imageUploadSuccess(imageURL: String){
        Toast.makeText(this, "your image is $imageURL", Toast.LENGTH_SHORT).show()
    }



    private fun genderSelectioinButton(genderRes: Int): Int {
        var genderResult = genderRes
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
}