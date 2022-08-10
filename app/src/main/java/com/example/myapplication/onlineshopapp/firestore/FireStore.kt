package com.example.myapplication.onlineshopapp.firestore

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.activities.LoginActivity
import com.example.myapplication.onlineshopapp.activities.MainActivity
import com.example.myapplication.onlineshopapp.activities.SignUpActivity
import com.example.myapplication.onlineshopapp.activities.UpdateProfileActivity
import com.example.myapplication.onlineshopapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStore {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun signupUser(activity: Activity, userInfo: User) {
        mFireStore.collection(Constants.Users)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //activity.userRegistrationSuccess()

            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user"
                )
            }
    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity) {
        mFireStore.collection(Constants.Users)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)!!
                val sharedPreference = activity.getSharedPreferences(
                    Constants.NICESHOP_PREFERENCES,
                    Context.MODE_PRIVATE
                )
                val editor: SharedPreferences.Editor = sharedPreference.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when (activity) {
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->

            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.Users).document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                Intent(activity, MainActivity::class.java).also { activity.startActivity(it) }
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating",
                    e
                )
            }

    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?) {
        //used to determine image file path
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USERIMAGE + System.currentTimeMillis() + " "
                    + Constants.getFileExtension(
                activity, imageFileUri
            )
        )
        sRef.putFile(imageFileUri!!).addOnSuccessListener {
            taskSnapshot ->
            //image upload success
            Log.e("Firebase image URL",
            taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )
            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {uri ->
                Log.e("Downloadable Image URL", uri.toString())
                when(activity){
                    is UpdateProfileActivity -> {
                        activity.imageUploadSuccess(uri.toString())
                    }
                }
            }
        }
            .addOnFailureListener {
                    exception ->
                Log.e(
                activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }
}