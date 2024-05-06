package com.example.foodie_guardv0.adapters.user

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.adapters.user.UserViewHolder
import com.example.foodie_guardv0.dataclass.User

class UserAdapter(private val UserList: List<User>) :
    RecyclerView.Adapter<UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return UserViewHolder(layoutInflater.inflate(R.layout.user_list, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getItemCount(): Int = UserList.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val item = UserList[position]
        holder.render(item)
    }
}
