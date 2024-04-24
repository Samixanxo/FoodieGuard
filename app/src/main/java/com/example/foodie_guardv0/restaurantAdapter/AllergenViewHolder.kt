package com.example.foodie_guardv0.restaurantAdapter

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.InfoRestaurant
import com.example.foodie_guardv0.dataclass.Dish
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.dataclass.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AllergenViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val DishName = view.findViewById<TextView>(R.id.tv_Name)
    private val DishDescription = view.findViewById<TextView>(R.id.tv_description)
    private val photo = view.findViewById<ImageView>(R.id.iv_Image)
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
    }
}