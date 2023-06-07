package com.example.firebaseexampleapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseexampleapp.model.Trips
import com.example.firebaseexampleapp.databinding.ActivityAddTripBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddTripActivity : AppCompatActivity() {
    private lateinit var addTripBinding: ActivityAddTripBinding
    private lateinit var myReference: DatabaseReference
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addTripBinding = ActivityAddTripBinding.inflate(layoutInflater)
        val view = addTripBinding.root
        setContentView(view)

        setSupportActionBar(addTripBinding.addusertoolbar)
        supportActionBar?.title = "Add Trip"

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            myReference = database.reference.child("MyUsers").child(currentUserId)
        }

        addTripBinding.buttonAddUser.setOnClickListener {
            addUserToDatabase()
        }
    }

    private fun addUserToDatabase() {
        val title: String = addTripBinding.editTextTitle.text.toString()
        val city: String = addTripBinding.editTextCity.text.toString()
        val notes: String = addTripBinding.editTextNotes.text.toString()

        val id: String = myReference.push().key.toString()

        val trip = Trips(id, title, city, notes)

        myReference.child(id).setValue(trip).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    applicationContext,
                    "The new user has been added to the database",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            } else {
                Toast.makeText(
                    applicationContext,
                    task.exception.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}