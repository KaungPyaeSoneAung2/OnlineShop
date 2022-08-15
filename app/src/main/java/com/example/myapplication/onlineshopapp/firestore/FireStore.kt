package com.example.myapplication.onlineshopapp.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.myapplication.onlineshopapp.Constants
import com.example.myapplication.onlineshopapp.activities.*
import com.example.myapplication.onlineshopapp.model.Product
import com.example.myapplication.onlineshopapp.model.User
import com.example.myapplication.onlineshopapp.ui.dashboard.DashboardFragment
import com.example.myapplication.onlineshopapp.ui.home.ProductsFragment
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
                    is  SettingsActivity->{
                        activity.UserDetailsSuccess(user)
                    }
                    is ProductDetailsActivity ->{
                        activity.UserDetailSuccess(user)
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
                when (activity) {
                    is UpdateProfileActivity -> {
                        // Call a function of base activity for transferring the result to it.
                        activity.userProfileUpdateSuccess()
                    }
            }}
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updating",
                    e
                )
            }

    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileUri: Uri?, imageType:String?) {
        //used to determine image file path
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.Image + System.currentTimeMillis() + " "
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

                when(activity){
                    is UpdateProfileActivity -> {
                        Log.e("Downloadable Image URL", uri.toString())
                        activity.imageUploadSuccess(uri.toString())
                    }
                    is AddProductActivity ->{
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
    fun uploadProductDetails(activity: AddProductActivity, productInfo: Product) {

        mFireStore.collection(Constants.PRODUCTS)
            .document()
            // Here the userInfo are Field and the SetOption is set to merge. It is for if we wants to merge
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {

                // Here call a function of base activity for transferring the result to it.
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e
                )
            }
    }

    fun getProductsList(fragment: Fragment) {
        // The collection name for PRODUCTS
        mFireStore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get() // Will get the documents snapshots.
            .addOnSuccessListener { document ->

                // Here we get the list of boards in the form of documents.
                Log.e("Products List", document.documents.toString())

                // Here we have created a new instance for Products ArrayList.
                val productsList: ArrayList<Product> = ArrayList()

                // A for loop as per the list of documents to convert them into Products ArrayList.
                for (i in document.documents) {

                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }

                when (fragment) {
                    is ProductsFragment -> {
                        fragment.successProductsListFromFireStore(productsList)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is ProductsFragment -> {
                        Log.e("Product fragment error","There is an error with firestore and products fragment",e)

                    }
                }
                Log.e("Get Product List", "Error while getting product list.", e)
            }
    }


    fun getDashboardItemsList(fragment: DashboardFragment) {
        mFireStore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->

                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents) {

                    val post = i.toObject(Product::class.java)!!
                     post.product_id= i.id
                    productsList.add(post)
                }

                fragment.successDashboardItemsList(productsList)
            }
            .addOnFailureListener { e ->
                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items list.", e)
            }
    }

    fun deleteProduct(fragment: ProductsFragment, productId: String) {

        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .delete()
            .addOnSuccessListener {

                fragment.productDeleteSuccess()
            }
            .addOnFailureListener { e ->


                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the product.",
                    e
                )
            }
    }

    fun getProductDetails(activity: ProductDetailsActivity, productId: String) {

        mFireStore.collection(Constants.PRODUCTS)
            .document(productId)
            .get()
            .addOnSuccessListener { document ->

                // Here we get the product details in the form of document.
                Log.e(activity.javaClass.simpleName, document.toString())

                // Convert the snapshot to the object of Product data model class.
                val product = document.toObject(Product::class.java)!!

                activity.productDetailsSuccess(product)
            }
            .addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error while getting the product details.", e)
            }
    }


    //------------------------------Line of untested codes----------------------------------------------------------------------------------

}