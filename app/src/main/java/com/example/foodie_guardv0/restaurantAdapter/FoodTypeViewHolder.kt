package com.example.foodie_guardv0.restaurantAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.FoodType

class FoodTypeViewHolder (view : View)  : RecyclerView.ViewHolder(view){

    val FoodType = view.findViewById<TextView>(R.id.typeImage)
    val image = view.findViewById<ImageView>(R.id.typeName)

    fun render (foodType: FoodType){
        FoodType.text = foodType.name
        Glide.with(image.context).load(foodType.image).into(image)
    }
}