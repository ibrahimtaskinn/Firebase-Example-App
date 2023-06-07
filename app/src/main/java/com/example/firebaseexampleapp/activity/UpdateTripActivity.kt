package com.example.firebaseexampleapp.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseexampleapp.databinding.ActivityUpdateTripBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UpdateTripActivity : AppCompatActivity() {

    private lateinit var updateTripBinding: ActivityUpdateTripBinding
    private lateinit var myReference: DatabaseReference
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateTripBinding = ActivityUpdateTripBinding.inflate(layoutInflater)
        val view = updateTripBinding.root
        setContentView(view)

        setSupportActionBar(updateTripBinding.updatetriptoolbar)
        supportActionBar?.title = "Update Trip"

        getAndSetData()

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            myReference = database.reference.child("MyUsers").child(currentUserId)
        }

        updateTripBinding.buttonUpdateTrip.setOnClickListener {
            updateData()
        }
    }

    fun getAndSetData(){

        val title = intent.getStringExtra("title")
        val city  = intent.getStringExtra("city")
        val notes = intent.getStringExtra("notes")

        updateTripBinding.editTextUpdateTitle.setText(title)
        updateTripBinding.editTextUpdateCity.setText(city)
        updateTripBinding.editTextUpdateNotes.setText(notes)
    }

    private fun updateData() {
        val updatedTitle = updateTripBinding.editTextUpdateTitle.text.toString()
        val updatedCity = updateTripBinding.editTextUpdateCity.text.toString()
        val updatedNotes = updateTripBinding.editTextUpdateNotes.text.toString()
        val tripId = intent.getStringExtra("id").toString()

        val userMap = mutableMapOf<String, Any>()
        userMap["tripId"] = tripId
        userMap["title"] = updatedTitle
        userMap["city"] = updatedCity
        userMap["notes"] = updatedNotes

        myReference.child(tripId).updateChildren(userMap).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(applicationContext, "The trip has been updated", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}