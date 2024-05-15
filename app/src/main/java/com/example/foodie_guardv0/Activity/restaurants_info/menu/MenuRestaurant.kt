package com.example.foodie_guardv0.Activity.restaurants_info.menu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Dish
import com.example.foodie_guardv0.adapters.allergen.AllergenAdapter
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MenuRestaurant : AppCompatActivity() {

    private val service = RetrofitClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_restaurant)

        val goBack = findViewById<ImageButton>(R.id.buttonReturn)

        goBack.setOnClickListener(){
            finish()
        }

        val id = intent.getIntExtra("id", Int.MIN_VALUE)
        Log.e("Resultado", id.toString())

        GlobalScope.launch(Dispatchers.Main) {
            try {
                initRecyclerRestaurant(dishes(id))
            } catch (e: Exception) {
                Log.e("Resultado", "Error" + e.message)
            }
        }
    }

    private fun initRecyclerRestaurant(dishes: List<Dish>) {
        val recyclerView = findViewById<RecyclerView>(R.id.rv_menu)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = AllergenAdapter(dishes)
    }

    private suspend fun dishes(name: Int): List<Dish> {
        return suspendCoroutine { continuation ->
            val call = service.getDishesForRest(name)
            call.enqueue(object : Callback<List<Dish>> {
                override fun onResponse(
                    call: Call<List<Dish>>,
                    response: Response<List<Dish>>
                ) {
                    if (response.isSuccessful) {
                        val respuesta = response.body()
                        continuation.resume(respuesta!!)
                    } else {
                        continuation.resumeWithException(Exception("Error de la API"))
                    }
                }
                override fun onFailure(call: Call<List<Dish>>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}