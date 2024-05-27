package com.example.foodie_guardv0.adapters.restaurants

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.restaurants_info.InfoRestaurant
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences

class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val userSharedPreferences = UserSharedPreferences(view.context)
    private val RestaurantName = view.findViewById<TextView>(R.id.name)
    private val address = view.findViewById<TextView>(R.id.adress)
    private val photo = view.findViewById<ImageView>(R.id.imageRestaurant)
    private val type = view.findViewById<TextView>(R.id.type)
    private val median = view.findViewById<TextView>(R.id.median)
    private val favButton = view.findViewById<ImageView>(R.id.favButton)


    fun render(restaurant: Restaurant) {
        RestaurantName.text = restaurant.name
        address.text = restaurant.address
        type.text = restaurant.type
        median.text = "Precio aproximado: " + restaurant.medianprice.toString() + "€"
        Glide.with(photo.context).load(restaurant.photo).into(photo)

        if (restaurant.fav){
            favButton.setImageResource(R.drawable.fav_heart)
        }

        favButton.setOnClickListener {
            val currentDrawable = favButton.drawable
            val context = favButton.context
            val favHeartDrawable = ContextCompat.getDrawable(context, R.drawable.fav_heart)
            val favVoidHeartDrawable = ContextCompat.getDrawable(context, R.drawable.fav_void_heart)

            if (currentDrawable.constantState == favVoidHeartDrawable?.constantState) {
                favButton.setImageResource(R.drawable.fav_heart)
                userSharedPreferences.saveFav(restaurant)
            } else if (currentDrawable.constantState == favHeartDrawable?.constantState) {
                favButton.setImageResource(R.drawable.fav_void_heart)
                userSharedPreferences.deleteFav(restaurant)
            }
        }




        itemView.setOnClickListener {
            val context = itemView.context
            val intent = Intent(context, InfoRestaurant::class.java)

            intent.putExtra("id", restaurant.id)
            intent.putExtra("name", restaurant.name)
            intent.putExtra("description", restaurant.description)
            intent.putExtra("phone", restaurant.phone.toString())
            intent.putExtra("photo", restaurant.photo)
            intent.putExtra("medianprice",restaurant.medianprice.toString())
            intent.putExtra("address", restaurant.address)
            intent.putExtra("email", restaurant.email)
            intent.putExtra("type", restaurant.type)
            intent.putExtra("lat", restaurant.lat)
            intent.putExtra("long", restaurant.lon)
            intent.putExtra("web", restaurant.website)

            context.startActivity(intent)

        }

    }


}
