package com.example.foodie_guardv0.adapters.restaurants.slider

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.restaurants_info.InfoRestaurant

class RestaurantSliderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val RestaurantName = view.findViewById<TextView>(R.id.sliderName)
    private val address = view.findViewById<TextView>(R.id.sliderAdress)
    private val photo = view.findViewById<ImageView>(R.id.sliderImageRestaurant)
    private val type = view.findViewById<TextView>(R.id.sliderType)


    fun render(restaurant: Restaurant) {
        RestaurantName.text = restaurant.name
        address.text = restaurant.address
        type.text = restaurant.type
        Glide.with(photo.context).load(restaurant.photo).into(photo)
        itemView.setOnLongClickListener {
            val context = itemView.context
            val intent = Intent(context, InfoRestaurant::class.java)

            intent.putExtra("id", restaurant.id)
            intent.putExtra("name", restaurant.name)
            intent.putExtra("description", restaurant.description)
            intent.putExtra("phone", restaurant.phone.toString())
            intent.putExtra("photo", restaurant.photo)
            intent.putExtra("medianprice", restaurant.medianprice.toString())
            intent.putExtra("address", restaurant.address)
            intent.putExtra("email", restaurant.email)
            intent.putExtra("type", restaurant.type)
            intent.putExtra("lat", restaurant.lat)
            intent.putExtra("long", restaurant.lon)

            context.startActivity(intent)
            true
        }
    }
}
