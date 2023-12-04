package com.example.foodie_guardv0.Activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guardv0.provider.RestaurantProvider
import com.example.foodie_guardv0.restaurantAdapter.RestaurantAdapter
import com.example.foodie_guardv0.retrofitt.ApiService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeActivity : AppCompatActivity() {

    lateinit var service: ApiService


    private val TAG_LOGS = "YourTag"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:8080/api/restaurant/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service= retrofit.create(ApiService::class.java)

       initRecyclerRestaurant()

    }
    private fun initRecyclerRestaurant() {
        var restaurantsList: List<Restaurant>?

        service.getRestaurant().enqueue(object : Callback<List<Restaurant>> {
            override fun onResponse(
                call: Call<List<Restaurant>>,
                response: Response<List<Restaurant>>
            ) {
                if (response.isSuccessful) {
                    restaurantsList = response.body()
                    Log.i(TAG_LOGS, Gson().toJson(restaurantsList))


                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerRestaurant2)
                    recyclerView.layoutManager = LinearLayoutManager(this@HomeActivity)
                    recyclerView.adapter = restaurantsList?.let { RestaurantAdapter(it) }
                } else {
                    Log.e(TAG_LOGS, "Error en la respuesta de la API: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                Log.e(TAG_LOGS, "Error en la llamada a la API: ${t.message}")
                t.printStackTrace()
            }
        })


        fun getRestaurantByid(){
            //Recibimos los datos del post con ID = 1
            var post: Restaurant? = null
            service.getRestaurantById(1).enqueue(object: Callback<Restaurant>{
                override fun onResponse(call: Call<Restaurant>?, response: Response<Restaurant>?) {
                    post = response?.body()
                    Log.i(TAG_LOGS, Gson().toJson(post))
                }
                override fun onFailure(call: Call<Restaurant>?, t: Throwable?) {
                    t?.printStackTrace()
                }
                })}


    }






}
