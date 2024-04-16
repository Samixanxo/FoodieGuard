package com.example.foodie_guardv0.restaurantAdapter

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guardv0.dataclass.Address
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.InfoRestaurant

class RestaurantSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val RestaurantName = view.findViewById<TextView>(R.id.sliderName)
    private val address = view.findViewById<TextView>(R.id.sliderAdress)
    private val photo = view.findViewById<ImageView>(R.id.sliderImageRestaurant)
    private val type = view.findViewById<TextView>(R.id.sliderType)


    fun render(restaurant: Restaurant) {
        RestaurantName.text = restaurant.name
        address.text = restaurant.address
        Glide.with(photo.context).load(restaurant.photo).into(photo)
        type.text = restaurant.type
    }





}
