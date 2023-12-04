package com.example.foodie_guardv0.restaurantAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guardv0.dataclass.Address
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guard0.R

class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val RestaurantName = view.findViewById<TextView>(R.id.name)
    private val phoneNumber = view.findViewById<TextView>(R.id.number)
    private val address = view.findViewById<TextView>(R.id.adress)
    private val photo = view.findViewById<ImageView>(R.id.imageRestaurant)


    fun render(restaurant: Restaurant) {
        RestaurantName.text = restaurant.name
        phoneNumber.text = restaurant.phone.toString()
        address.text = restaurant.address
        Glide.with(photo.context).load(restaurant.photo).into(photo)

    }

    private fun formatAddress(address: Address): String {
        val city = address.city
        val street = address.street
        val number = address.number
        val postalCode = address.postalCode
        // Formatea la dirección como desees
        return "$number $street, $city, $postalCode"
    }
}
