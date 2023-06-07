package com.example.firebaseexampleapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaseexampleapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

class LoginActivity : AppCompatActivity() {

    lateinit var loginBinding: ActivityLoginBinding

    val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        val view = loginBinding.root

        setContentView(view)

        setSupportActionBar(loginBinding.signinToolbar)
        supportActionBar?.title = "SIGN IN"

        loginBinding.buttonSignin.setOnClickListener {

            val userEmail = loginBinding.editTextEmailSignin.text.toString()
            val userPassword = loginBinding.editTextPasswordSignin.text.toString()

            signinWithFirebase(userEmail, userPassword)
        }

        loginBinding.buttonSignup.setOnClickListener {

            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        loginBinding.buttonForgot.setOnClickListener {

            val intent = Intent(this, ForgetActivity::class.java)
            startActivity(intent)
        }
    }

    fun signinWithFirebase(userEmail : String, userPassWord : String) {

        if(userEmail.isEmpty() || userPassWord.isEmpty()){
            Toast.makeText(applicationContext, "Email and Password must not be empty", Toast.LENGTH_SHORT).show()
            return
        }

        auth.signInWithEmailAndPassword(userEmail, userPassWord)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Toast.makeText(applicationContext, "Login is successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    try {
                        throw task.exception!!
                    } catch(e: FirebaseAuthInvalidCredentialsException) {
                        Toast.makeText(applicationContext, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    } catch(e: Exception) {
                        Toast.makeText(applicationContext, "An error occurred: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    override fun onStart() {
        super.onStart()

        val user = auth.currentUser

        if (user != null){
            val intent = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}