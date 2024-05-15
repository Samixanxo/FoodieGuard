package com.example.foodie_guardv0.adapters.review

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Review

class ReviewAdapter(private val reviewList: List<Review>):
    RecyclerView.Adapter<ReviewViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            return ReviewViewHolder(layoutInflater.inflate(R.layout.review_list, parent, false))
        }
        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
            val item = reviewList[position]
            holder.render(item)
        }

        override fun getItemCount(): Int = reviewList.size
    }
