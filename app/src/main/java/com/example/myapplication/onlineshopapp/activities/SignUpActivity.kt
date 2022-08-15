package com.example.myapplication.onlineshopapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.myapplication.onlineshopapp.R
import com.example.myapplication.onlineshopapp.databinding.ActivitySignUpBinding
import com.example.myapplication.onlineshopapp.firestore.FireStore
import com.example.myapplication.onlineshopapp.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupActionBar()

        binding.havAccountLogin.setOnClickListener{
            Intent(this,LoginActivity::class.java).also { startActivity(it) }
            finish()
        }
        binding.buttonSignUp.setOnClickListener {
            registerUser()
            Intent(this,DashBoardActivity::class.java).also { startActivity(it) }
        }

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.topToolBar)
        val actionBar=supportActionBar
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)

        }
        binding.topToolBar.setNavigationOnClickListener{
            Intent(this,LoginActivity::class.java).also { startActivity(it) }
            finish()
        }
    }

    private fun validateRegisterDetails(): Boolean{
        return when{
            TextUtils.isEmpty(binding.firstNameSignUp.text.toString().trim { it<= ' '})->{
                (Toast.makeText(this, "First Name Error", Toast.LENGTH_SHORT).show())
                false
            }
            TextUtils.isEmpty(binding.lastNameSignUp.text.toString().trim { it<= ' '})->{
                (Toast.makeText(this, "Last Name Error", Toast.LENGTH_SHORT).show())
                false
            }
            TextUtils.isEmpty(binding.emailSignUp.text.toString().trim { it<= ' '})|| !binding.emailSignUp.text.contains("mail.com")->{
                (Toast.makeText(this, "Email Error", Toast.LENGTH_SHORT).show())
                false
            }
            TextUtils.isEmpty(binding.pwSignUp.text.toString().trim { it<= ' '})->{
                (Toast.makeText(this, "Password Error", Toast.LENGTH_SHORT).show())
                false
            }
            TextUtils.isEmpty(binding.pwSignUp2.text.toString().trim { it<= ' '})->{
                (Toast.makeText(this, "Password Error", Toast.LENGTH_SHORT).show())
                false
            }
            binding.pwSignUp.text.toString() != binding.pwSignUp2.text.toString() ->{
                (Toast.makeText(this, "Password not match", Toast.LENGTH_SHORT).show())
                false
            }
            !binding.tandcSignUp.isChecked ->{
                Toast.makeText(this, "If you don't agree our policy Just Goo", Toast.LENGTH_SHORT).show()
                false
            }
            else ->{
                true
            }
        }
    }

    private fun registerUser() {
        if (validateRegisterDetails()) {
            val email: String = binding.emailSignUp.text.toString().trim { it <= ' '}
            val pw: String = binding.pwSignUp.text.toString().trim { it <= ' '}
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,pw)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->
                        //here is successful check
                        if(task.isSuccessful){
                         val firebaseUser: FirebaseUser = task.result!!.user!!
                            val user = User(
                                firebaseUser.uid,
                                binding.firstNameSignUp.text.toString().trim{it <= ' '},
                                binding.lastNameSignUp.text.toString().trim{it <= ' '},
                                binding.emailSignUp.text.toString().trim{it <= ' '},
                            )
//                            Toast.makeText(this, "Welcome. Your Id is ${firebaseUser.uid}", Toast.LENGTH_SHORT).show()

                            FireStore().signupUser(this, user)
                            //FirebaseAuth.getInstance().signOut()
                            //finish()
                        }
                        else{
                            Toast.makeText(this, task.exception!!.message.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                )

        }
    }

    fun userRegistrationSuccess(){
        Toast.makeText(this, "Welcome to our Store Mr. ${binding.firstNameSignUp.text}", Toast.LENGTH_SHORT).show()
    }
}