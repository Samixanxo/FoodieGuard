package com.example.foodie_guardv0.adapters.allergen

import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Dish

class AllergenViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val DishName = view.findViewById<TextView>(R.id.tv_Name)
    private val DishDescription = view.findViewById<TextView>(R.id.tv_description)
    private val photo = view.findViewById<ImageView>(R.id.iv_Image)
    private val allergens = view.findViewById<GridLayout>(R.id.gridLayout)
    private val Price = view.findViewById<TextView>(R.id.tv_price)

    fun render (dish: Dish){
        DishName.text=dish.name
        DishDescription.text=dish.description
        Price.text = "Precio: " + dish.price.toString() + "€"
        Glide.with(photo.context).load(dish.photo).into(photo)


        val listAllergens = dish.allergens.toList()
        Log.e("resultado", listAllergens.toString())
        for (x in 0 until listAllergens.size){
            if (listAllergens[x] == '0'){
                Log.e("resultado", listAllergens[x].toString())
                allergens.getChildAt(x).visibility = View.GONE
            }
        }
    }
}