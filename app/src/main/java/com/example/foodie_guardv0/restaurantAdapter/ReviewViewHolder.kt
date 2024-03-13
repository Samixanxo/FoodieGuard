package com.example.foodie_guardv0.restaurantAdapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Review
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient

class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    private val reviewText = view.findViewById<TextView>(R.id.reviewtext)
    private val reviewRating = view.findViewById<TextView>(R.id.rating)
    fun render(review: Review){
        reviewText.text=review.review
        reviewRating.text=review.rating.toString()+"/10"
    }
}