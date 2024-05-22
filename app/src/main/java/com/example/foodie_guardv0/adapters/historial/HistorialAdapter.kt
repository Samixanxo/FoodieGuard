package com.example.foodie_guardv0.adapters.historial

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.adapters.dish.DishViewHolder
import com.example.foodie_guardv0.dataclass.Dish
import com.example.foodie_guardv0.dataclass.Reservation

class HistorialAdapter(private val dishList: List<Reservation>):
    RecyclerView.Adapter<HistorialViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HistorialViewHolder(layoutInflater.inflate(R.layout.reservation_list, parent, false))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val item = dishList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = dishList.size
}