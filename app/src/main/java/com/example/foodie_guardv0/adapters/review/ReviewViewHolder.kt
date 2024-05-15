package com.example.foodie_guardv0.adapters.review

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Review

class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {


    private val reviewText = view.findViewById<TextView>(R.id.reviewtext)
    private val reviewRating = view.findViewById<TextView>(R.id.rating)
    fun render(review: Review){
        reviewText.text=review.review
        reviewRating.text=review.rating.toString()+"/10"
    }
}