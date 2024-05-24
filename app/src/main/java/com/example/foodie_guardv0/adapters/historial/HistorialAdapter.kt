package com.example.foodie_guardv0.adapters.historial

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.foodie_guardv0.adapters.dish.DishViewHolder
import com.example.foodie_guardv0.dataclass.Dish
import com.example.foodie_guardv0.dataclass.Reservation

class HistorialAdapter(private val dishList: List<Reservation>) :
    RecyclerView.Adapter<HistorialViewHolder>() {

    private val adapterScope = CoroutineScope(Dispatchers.Main)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistorialViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return HistorialViewHolder(layoutInflater.inflate(R.layout.reservation_list, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: HistorialViewHolder, position: Int) {
        val item = dishList[position]
        adapterScope.launch {
            try {
                holder.render(item)
            } catch (e: Exception) {
                Log.e("Render Error", "Failed to render item at position $position", e)
            }
        }
    }

    override fun getItemCount(): Int = dishList.size
}