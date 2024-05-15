package com.example.foodie_guardv0.adapters.dish

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Dish

class DishAdapter(private val dishList: List<Dish>):
        RecyclerView.Adapter<DishViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DishViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return DishViewHolder(layoutInflater.inflate(R.layout.dish_list, parent, false))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: DishViewHolder, position: Int) {
        val item = dishList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = dishList.size
}