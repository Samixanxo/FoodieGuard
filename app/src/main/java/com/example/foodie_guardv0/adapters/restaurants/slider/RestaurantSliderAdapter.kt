package com.example.foodie_guardv0.adapters.restaurants.slider

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Restaurant

class RestaurantSliderAdapter(private val restaurantList: List<Restaurant>) :
    RecyclerView.Adapter<RestaurantSliderViewHolder>()  {

    private var onRestaurantClickListener: OnRestaurantClickListener? = null

    interface OnRestaurantClickListener{
            fun onRestaurantClick(position: Int)
        }


    fun setOnRestaurantClickListener(listener: OnRestaurantClickListener) {
        this.onRestaurantClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantSliderViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return RestaurantSliderViewHolder(layoutInflater.inflate(R.layout.restaurant_slider, parent, false))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RestaurantSliderViewHolder, position: Int) {
        val item = restaurantList[position]
        holder.render(item)

        holder.itemView.setOnClickListener {
            onRestaurantClickListener?.onRestaurantClick(position)
        }


    }
    override fun getItemCount(): Int = restaurantList.size





}
