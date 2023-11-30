package com.example.foodie_guardv0.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guardv0.provider.RestaurantProvider
import com.example.foodie_guardv0.restaurantAdapter.RestaurantAdapter
import com.example.foodie_guardv0.retrofitt.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)
       initRecyclerRestaurant()
    }
    private fun initRecyclerRestaurant(){
      val recyclerView = findViewById<RecyclerView>(R.id.recyclerRestaurant2)
        recyclerView.layoutManager = LinearLayoutManager(this)
       recyclerView.adapter = RestaurantAdapter(RestaurantProvider.restaurantList)
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://localhost:8080/api/restaurant/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


}
