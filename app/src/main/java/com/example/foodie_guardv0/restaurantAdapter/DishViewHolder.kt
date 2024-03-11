package com.example.foodie_guardv0.restaurantAdapter

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.InfoRestaurant
import com.example.foodie_guardv0.dataclass.Dish

class DishViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val DishName = view.findViewById<TextView>(R.id.name)
    private val DishDescription = view.findViewById<TextView>(R.id.description)
    private val photo = view.findViewById<ImageView>(R.id.imageDish)
    private val allergens = view.findViewById<GridLayout>(R.id.gridLayout)

    fun render (dish: Dish){
        DishName.text=dish.name
        DishDescription.text=dish.description
        Glide.with(photo.context).load(dish.photo).into(photo)

        val listAllergens = dish.allergens.toList()
        Log.e("resultado", listAllergens.toString())
        for (x in 0 until listAllergens.size){
            if (listAllergens[x] == '0'){
                Log.e("resultado", listAllergens[x].toString())
                allergens.getChildAt(x).visibility = View.GONE
            }
        }

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