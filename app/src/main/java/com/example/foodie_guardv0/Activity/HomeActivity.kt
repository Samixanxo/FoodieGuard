package com.example.foodie_guardv0.Activity

import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guardv0.restaurantAdapter.RestaurantAdapter
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.suspendCoroutine

class HomeActivity : AppCompatActivity(),SearchView.OnQueryTextListener {

    private val service = RetrofitClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recycler_view)

        val svSearcher = findViewById<SearchView>(R.id.svSearcher)
        svSearcher.setOnQueryTextListener(this)

        GlobalScope.launch(Dispatchers.Main) {
            try {
                initRecyclerRestaurant(restaurants(""))
                Log.e("Resultado", "correcto")
            } catch (e: Exception) {
                Log.e("Resultado", "Error" + e.message)
            }
        }

    }

    private fun initRecyclerRestaurant(restaurants: List<Restaurant>){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerRestaurant2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = RestaurantAdapter(restaurants)
    }

    private suspend fun restaurants(name: String): List<Restaurant> {
        return suspendCoroutine { continuation ->
            var call = service.getRestaurant()
            if(!name.isEmpty()) {
                call = service.getRestaurantByName(name)
            }
            call.enqueue(object : Callback<List<Restaurant>> {
                override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>
                ){
                    if (response.isSuccessful) {
                        val respuesta = response.body()
                        Log.e("Resultado",response.body().toString())

                        continuation.resumeWith(Result.success(respuesta!!))
                    } else {
                        // Manejar error de la API
                        continuation.resumeWith(Result.failure(Exception("Error de la API")))
                        Log.e("Resultado", "error Api")
                    }
                }

                override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                    // Manejar error de conexión
                    continuation.resumeWith(Result.failure(t))
                }
            })
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false;
    }

    override fun onQueryTextChange(newText: String): Boolean {
        GlobalScope.launch(Dispatchers.Main) {
            initRecyclerRestaurant(restaurants(newText))
        }
        return true;
    }


}
