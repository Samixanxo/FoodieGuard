package com.example.foodie_guardv0.restaurantAdapter

import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.InfoRestaurant
import com.example.foodie_guardv0.dataclass.Dish

class DishViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val DishName = view.findViewById<TextView>(R.id.name)
    private val DishDescription = view.findViewById<TextView>(R.id.description)
    private val photo = view.findViewById<ImageView>(R.id.imageDish)


    fun render (dish: Dish){
        DishName.text=dish.name
        DishDescription.text=dish.description
        Glide.with(photo.context).load(dish.photo).into(photo)

        itemView.setOnClickListener{
            val context = itemView.context
            val intent = Intent(context, InfoRestaurant::class.java)
            intent.putExtra("name", dish.name)
            intent.putExtra("name", dish.description)
            intent.putExtra("photo", dish.photo)


            context.startActivity(intent)
        }
    }

}