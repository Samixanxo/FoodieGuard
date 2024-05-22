package com.example.foodie_guardv0.adapters.historial

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Reservation
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient

class HistorialViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val service = RetrofitClient.retrofit.create(ApiService::class.java)

    private val DishName = view.findViewById<TextView>(R.id.name)
    private val DishDescription = view.findViewById<TextView>(R.id.description)
    private val photo = view.findViewById<ImageView>(R.id.imageDish)


    fun render (reservation: Reservation){
        DishName.text=reservation.date.toString()

    }}