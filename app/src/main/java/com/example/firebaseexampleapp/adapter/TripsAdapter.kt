package com.example.firebaseexampleapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseexampleapp.model.Trips
import com.example.firebaseexampleapp.activity.UpdateTripActivity
import com.example.firebaseexampleapp.databinding.TripsItemBinding

class TripsAdapter(var context : Context,
                   var userlist : ArrayList<Trips>) : RecyclerView.Adapter<TripsAdapter.UsersViewHolder>() {

    inner class UsersViewHolder(val adapterBinding : TripsItemBinding)
        : RecyclerView.ViewHolder(adapterBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {

        val binding = TripsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {

        holder.adapterBinding.textViewTitle.text = userlist[position].title
        holder.adapterBinding.textViewCity.text = userlist[position].city.toString()
        holder.adapterBinding.textViewNotes.text = userlist[position].notes

        holder.adapterBinding.linearLayout.setOnClickListener {

            val intent = Intent(context, UpdateTripActivity::class.java)
            intent.putExtra("id", userlist[position].tripId)
            intent.putExtra("title", userlist[position].title)
            intent.putExtra("city", userlist[position].city)
            intent.putExtra("notes", userlist[position].notes)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return   userlist.size
    }

    fun getUserId(position: Int) : String{
        return userlist[position].tripId
    }
}