package com.example.firebaseexampleapp

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.firebaseexampleapp.databinding.UsersItemBinding

class UsersAdapter(var context : Context,
                   var userlist : ArrayList<Users>) : RecyclerView.Adapter<UsersAdapter.UsersViewHolder>() {

    inner class UsersViewHolder(val adapterBinding : UsersItemBinding)
        : RecyclerView.ViewHolder(adapterBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {

        val binding = UsersItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {

        holder.adapterBinding.textViewName.text = userlist[position].userName
        holder.adapterBinding.textViewAge.text = userlist[position].userAge.toString()
        holder.adapterBinding.textViewEmail.text = userlist[position].userEmail

        holder.adapterBinding.linearLayout.setOnClickListener {

            val intent = Intent(context, UpdateUserActivity::class.java)
            intent.putExtra("id", userlist[position].userId)
            intent.putExtra("name", userlist[position].userName)
            intent.putExtra("age", userlist[position].userAge)
            intent.putExtra("email", userlist[position].userEmail)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return   userlist.size
    }

    fun getUserId(position: Int) : String{
        return userlist[position].userId
    }
}