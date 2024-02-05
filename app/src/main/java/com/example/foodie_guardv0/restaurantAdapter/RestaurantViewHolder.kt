package com.example.foodie_guardv0.restaurantAdapter

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guardv0.dataclass.Address
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.HomeActivity
import com.example.foodie_guardv0.Activity.InfoRestaurant

class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val RestaurantName = view.findViewById<TextView>(R.id.name)
    private val phoneNumber = view.findViewById<TextView>(R.id.number)
    private val address = view.findViewById<TextView>(R.id.adress)
    private val photo = view.findViewById<ImageView>(R.id.imageRestaurant)


    fun render(restaurant: Restaurant){
        RestaurantName.text = restaurant.name
        phoneNumber.text = "Telefono: " + restaurant.phone.toString()
        address.text = "Direccion: \n" + restaurant.address
        Glide.with(photo.context).load(restaurant.photo).into(photo)

        itemView.setOnClickListener () {
            val context = itemView.context
            val intent = Intent(context, InfoRestaurant::class.java)
            intent.putExtra("name", restaurant.name)
            intent.putExtra("description", restaurant.description)
            intent.putExtra("phone", restaurant.phone)
            intent.putExtra("photo", restaurant.photo)
            intent.putExtra("address", restaurant.address)
            intent.putExtra("email", restaurant.email)
            context.startActivity(intent)

        }
    }




}
