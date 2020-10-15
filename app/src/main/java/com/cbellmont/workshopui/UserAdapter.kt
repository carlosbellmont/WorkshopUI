package com.cbellmont.workshopui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cbellmont.datamodel.User
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class UserAdapter(private var userSelectable: UserSelectable) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var users = listOf<User>()

    class UserViewHolder(var root: View, var tvName: TextView, var tvPicture: ImageView) : RecyclerView.ViewHolder(root)

    fun updateData(users : List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.adapter_user, parent, false)
        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvPicture = view.findViewById<ImageView>(R.id.ivPictureSmall)
        return UserViewHolder(view, tvName, tvPicture)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.tvName.text = users[position].getCompleteName()
        Picasso.get().load(users[position].getSmallPhoto()).fetch(object : Callback {
            override fun onSuccess() {
                holder.tvPicture.alpha = 0f;
                Picasso.get().load(users[position].getSmallPhoto()).into(holder.tvPicture);
                holder.tvPicture.animate().setDuration(300).alpha(1f).start();
            }

            override fun onError(e: Exception?) {
            }
        })

        // Picasso.get().load(users[position].getSmallPhoto()).into(holder.tvPicture)
        holder.root.setOnClickListener {
            userSelectable.onUserSelected(users[position])
        }
    }

}