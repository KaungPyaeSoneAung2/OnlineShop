package com.example.myapplication.onlineshopapp.firestore

import com.example.myapplication.onlineshopapp.activities.SignUpActivity
import com.example.myapplication.onlineshopapp.model.User
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStore {
    private val mFireStore= FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User){
        mFireStore.collection("users")
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
            }
    }


}