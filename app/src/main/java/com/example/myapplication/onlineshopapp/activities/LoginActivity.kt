package com.example.myapplication.onlineshopapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private companion object{
        private const val TAG= "LoginActivity"
        private const val RC_GOOGLE_SIGN_IN=4926
    }
    private lateinit var auth:FirebaseAuth

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth= Firebase.auth




        binding.noAccountSignUp.setOnClickListener{
            Intent(this,SignUpActivity::class.java).also { startActivity(it) }
        }

        binding.buttonLogin.setOnClickListener {
            logInRegisteredUser()

        }


        ///////////////////////////////////////////////////////////////////////////

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()

        val client = GoogleSignIn.getClient(this,gso)
        binding.googleLogin.setOnClickListener{
            val signInIntent= client.signInIntent
            startActivityForResult(signInIntent,RC_GOOGLE_SIGN_IN)
        }

    }

    private fun validateLoginDetails():Boolean {
    return when{
        TextUtils.isEmpty(binding.emailLogin.text.toString().trim { it<= ' '})|| !binding.emailLogin.text.contains("mail.com")->{
            Toast.makeText(this, "First Name Error", Toast.LENGTH_SHORT).show()
            false
        }
        TextUtils.isEmpty(binding.pwLogin.text.toString().trim { it<= ' '})->{
            Toast.makeText(this, "Password Error", Toast.LENGTH_SHORT).show()
            false
        }
        else ->{
            true
        }
    }
    }

    private fun logInRegisteredUser(){
        if (validateLoginDetails()){
            val email = binding.emailLogin.text.toString().trim{it <= ' '}
            val pw = binding.pwLogin.text.toString().trim{it <= ' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,pw)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Welcome Sir", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "You wish, Try Again MF", Toast.LENGTH_SHORT).show()

                    }
                }
        }
    }


    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        //this go to main activtiy
        if(user==null){
            Log.w(TAG,"User is null, not going to navigate")
            return
        }
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode== RC_GOOGLE_SIGN_IN){
            val task=GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle"+account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            }catch (e: ApiException){
                Log.w(TAG, "Google sign in failed",e)
            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){ task->
                if(task.isSuccessful){
                    Log.d(TAG,"signInWithCredential:success")
                    val user= auth.currentUser
                    updateUI(user)
                }
                else{
                    Log.w(TAG,"signInWithCredential:failure",task.exception)
                    Toast.makeText(this,"Authentication failed",Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }
    }

}