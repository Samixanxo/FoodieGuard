package com.example.foodie_guardv0.adapters.user
import android.widget.ImageView


import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.User

class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val userName = view.findViewById<TextView>(R.id.name)
    private val userLastname = view.findViewById<TextView>(R.id.lastname)
    private val userSurname = view.findViewById<TextView>(R.id.adress)
    val userProfilePic = view.findViewById<ImageView>(R.id.image)
    private val userEmail = view.findViewById<TextView>(R.id.email)
    private val userBirthdate = view.findViewById<TextView>(R.id.birthdate)

    fun render(user: User) {
        userName.text = user.name
        userSurname.text = user.surname
        userEmail.text = user.email

    }
}
