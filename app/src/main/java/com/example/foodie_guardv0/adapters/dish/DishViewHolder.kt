package com.example.foodie_guardv0.adapters.dish

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.restaurants_info.InfoRestaurant
import com.example.foodie_guardv0.dataclass.Dish
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.dataclass.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DishViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val service = RetrofitClient.retrofit.create(ApiService::class.java)

    private val DishName = view.findViewById<TextView>(R.id.name)
    private val DishDescription = view.findViewById<TextView>(R.id.description)
    private val photo = view.findViewById<ImageView>(R.id.imageDish)


    fun render (dish: Dish){
        DishName.text=dish.name
        DishDescription.text=dish.description
        Glide.with(photo.context).load(dish.photo).into(photo)

        itemView.setOnClickListener {
            val context = itemView.context
            val intent = Intent(context, InfoRestaurant::class.java)
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val restaurant = restaurants(dish.idRes)
                    Log.e("Resultado", restaurant.toString())
                    intent.putExtra("name", restaurant.name)
                    intent.putExtra("description", restaurant.description)
                    intent.putExtra("phone", restaurant.phone.toString())
                    intent.putExtra("photo", restaurant.photo)
                    intent.putExtra("medianprice",restaurant.medianprice.toString())
                    intent.putExtra("address", restaurant.address)
                    intent.putExtra("email", restaurant.email)
                    intent.putExtra("type", restaurant.type)
                    intent.putExtra("lat", restaurant.lat)
                    intent.putExtra("long", restaurant.lon)
                    intent.putExtra("web",restaurant.website)

                    context.startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(context, "Error al obtener datos del restaurante", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }


    private suspend fun restaurants(id: Int): Restaurant {
        return suspendCoroutine { continuation ->
            var call = service.getRestaurantById(id)

            call.enqueue(object : Callback<Restaurant> {
                override fun onResponse(
                    call: Call<Restaurant>,
                    response: Response<Restaurant>
                ) {
                    if (response.isSuccessful) {
                        val respuesta = response.body()
                        continuation.resume(respuesta!!)
                    } else {
                        continuation.resumeWithException(Exception("Error de la API"))
                        Log.e("Resultado", "error Api")
                    }
                }

                override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }


}