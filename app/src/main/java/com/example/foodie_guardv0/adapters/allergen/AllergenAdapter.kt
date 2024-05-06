package com.example.foodie_guardv0.adapters.allergen

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Dish

class AllergenAdapter(private val dishList: List<Dish>):
        RecyclerView.Adapter<AllergenViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllergenViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return AllergenViewHolder(layoutInflater.inflate(R.layout.menu_list, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: AllergenViewHolder, position: Int) {
        val item = dishList[position]
        holder.render(item)
    }


    override fun getItemCount(): Int = dishList.size


}