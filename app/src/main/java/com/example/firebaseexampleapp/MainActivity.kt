package com.example.firebaseexampleapp

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseexampleapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    lateinit var mainBinding: ActivityMainBinding

    val database : FirebaseDatabase = FirebaseDatabase.getInstance()
    var myReference : DatabaseReference = database.reference.child("MyUsers")

    private lateinit var dataListener: ValueEventListener

    val tripList = ArrayList<Trips>()
    lateinit var tripsAdapter: TripsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        val view = mainBinding.root
        setContentView(view)

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        if (currentUserId != null) {
            myReference = database.reference.child("MyUsers").child(currentUserId)
        }

        setSupportActionBar(mainBinding.Hometoolbar)
        supportActionBar?.title = "Home"

        mainBinding.floatingActionButton.setOnClickListener {

            val intent = Intent(this, AddTripActivity::class.java)
            startActivity(intent)
        }

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0
            , ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val id = tripsAdapter.getUserId(viewHolder.adapterPosition)

                myReference.child(id).removeValue()

                Toast.makeText(applicationContext, "The trip was deleted", Toast.LENGTH_SHORT).show()
            }

        }).attachToRecyclerView(mainBinding.recyclerView)

        retrieveDataFromDatabase()

    }

    private fun retrieveDataFromDatabase() {
        dataListener = myReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tripList.clear()
                for (eachUser in snapshot.children) {
                    val user = eachUser.getValue(Trips::class.java)
                    if (user != null) {
                        tripList.add(user)
                    }
                }
                tripsAdapter = TripsAdapter(this@MainActivity, tripList)
                mainBinding.recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                mainBinding.recyclerView.adapter = tripsAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_delete_all,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll) {
            showDialogMessage()
        } else if (item.itemId == R.id.signOut) {
            myReference.removeEventListener(dataListener)
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun showDialogMessage(){

        val dialogMessage = AlertDialog.Builder(this)
        dialogMessage.setTitle("Delete All Trips")
        dialogMessage.setMessage("İf click Yes, all trips will be deleted," +
            "If you want to delete a specific trip, you can swipe the item you want to delete right or left")
        dialogMessage.setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
            dialogInterface.cancel()
        })

        dialogMessage.setPositiveButton("Yes", DialogInterface.OnClickListener{ dialogInterface, i ->
            myReference.removeValue().addOnCompleteListener { task ->

                if (task.isSuccessful){
                    tripsAdapter.notifyDataSetChanged()

                    Toast.makeText(applicationContext, "All trips were deleted", Toast.LENGTH_SHORT).show()
                }
            }
        })
        dialogMessage.create().show()
    }
}