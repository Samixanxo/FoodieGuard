package com.example.foodie_guardv0.Activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Reservation
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CalendarActivity : AppCompatActivity() {
    private val service = RetrofitClient.retrofit.create(ApiService::class.java)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        val photo = intent.getStringExtra("photo")
        val id = intent.getIntExtra("id", 0)
        val ImageRestaurant = findViewById<ImageView>(R.id.imageRestaurant)
        Glide.with(ImageRestaurant.context).load(photo).into(ImageRestaurant)

        lifecycleScope.launch {
            val reservations = getReservations(id)
            Log.e("reservas",id.toString())
            Log.e("reservas",reservations.toString())

        }
    }

    private suspend fun getReservations(id: Int): List<Reservation> {
        return suspendCoroutine { continuation ->
            var call = service.getReservationsByIdRes(id)

            call.enqueue(object : Callback<List<Reservation>> {
                override fun onResponse(
                    call: Call<List<Reservation>>,
                    response: Response<List<Reservation>>
                ) {
                    if (response.isSuccessful) {
                        Log.e("Respuesta existosa", "todo fresco")
                        val respuesta = response.body()
                        Log.e("respuesta", response.body().toString())
                        continuation.resume(respuesta!!)
                    } else {
                        continuation.resumeWithException(Exception("Error de la API"))
                        Log.e("Resultado", "error Api")
                    }
                }

                override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}