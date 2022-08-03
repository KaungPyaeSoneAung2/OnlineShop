package com.example.myapplication.onlineshopapp.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.activities.LoginActivity
import com.example.myapplication.onlineshopapp.activities.SignUpActivity
import com.example.myapplication.onlineshopapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStore {
    private val mFireStore= FirebaseFirestore.getInstance()

    fun signupUser(activity: Activity, userInfo: User){
        mFireStore.collection(Constants.Users)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                //activity.userRegistrationSuccess()

            }
            .addOnFailureListener{  e->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while registering the user"
                )
            }
    }

    fun getCurrentUserID():String{
        val currentUser= FirebaseAuth.getInstance().currentUser
        var currentUserID=""
        if (currentUser != null){
            currentUserID=currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity){
        mFireStore.collection(Constants.Users)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName,document.toString())
                val user = document.toObject(User::class.java)!!
                val sharedPreference = activity.getSharedPreferences( Constants.NICESHOP_PREFERENCES,Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor=sharedPreference.edit()
                    editor.putString(
                        Constants.LOGGED_IN_USERNAME,
                        "${user.firstName} ${user.lastName}"
                    )
                editor.apply()

                when(activity){
                    is LoginActivity ->{
                        activity.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener{ e->

            }
    }
}