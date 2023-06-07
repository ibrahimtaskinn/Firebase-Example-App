package com.example.firebaseexampleapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaseexampleapp.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class SignupActivity : AppCompatActivity() {

    lateinit var signupBinding: ActivitySignupBinding

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signupBinding = ActivitySignupBinding.inflate(layoutInflater)
        val view = signupBinding.root
        setContentView(view)

        setSupportActionBar(signupBinding.signupToolbar)
        supportActionBar?.title = "SIGN UP"

        signupBinding.buttonSignupUser.setOnClickListener {

            val userEmail = signupBinding.editTextEmailSignup.text.toString()
            val userPassword = signupBinding.editTextPasswordSignup.text.toString()

            signupWithFirebase(userEmail, userPassword)
        }
    }

    fun signupWithFirebase(userEmail : String, userPassword : String){

        if(userEmail.isEmpty() || userPassword.isEmpty()){
            Toast.makeText(applicationContext, "Email and Password must not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        if(userPassword.length < 6){
            Toast.makeText(applicationContext, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
            Toast.makeText(applicationContext, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener { task ->

            if (task.isSuccessful){
                Toast.makeText(applicationContext, "Your account has been created", Toast.LENGTH_SHORT).show()

                FirebaseAuth.getInstance().signOut()

                val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                try {
                    throw task.exception!!
                } catch(e: FirebaseAuthUserCollisionException) {
                    Toast.makeText(applicationContext, "This email is already registered", Toast.LENGTH_SHORT).show()
                } catch(e: Exception) {
                    Toast.makeText(applicationContext, "An error occurred: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}