package com.cbellmont.workshopui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cbellmont.datamodel.User

class UserAdapter(private var userSelectable: UserSelectable) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users = listOf<User>()

    class UserViewHolder(var root: View, var tvName: TextView) : RecyclerView.ViewHolder(root)

    fun updateData(users : List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_user, parent, false)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        return UserViewHolder(view, tvName)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.tvName.text = users[position].getCompleteName()
        holder.root.setOnClickListener {
            userSelectable.onUserSelected(users[position])
        }
    }

}