package com.example.firebaseexampleapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.firebaseexampleapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

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

        auth.signInWithEmailAndPassword(userEmail, userPassWord)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    Toast.makeText(applicationContext, "Login is successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, task.exception?.toString(), Toast.LENGTH_SHORT).show()
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