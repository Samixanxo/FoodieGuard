package com.example.foodie_guardv0.restaurantAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.FoodType

class FoodTypeAdapter (private val foodTypeList: List<FoodType>) :
    RecyclerView.Adapter<FoodTypeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodTypeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FoodTypeViewHolder(layoutInflater.inflate(R.layout.foodtype_recycler_view, parent, false))
    }

    override fun onBindViewHolder(holder: FoodTypeViewHolder, position: Int) {
        val item = foodTypeList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = foodTypeList.size
    }