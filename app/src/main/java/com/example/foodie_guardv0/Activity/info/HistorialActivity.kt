package com.example.foodie_guardv0.Activity.info

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.adapters.historial.HistorialAdapter
import com.example.foodie_guardv0.dataclass.Reservation
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HistorialActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HistorialAdapter
    lateinit var userSharedPreferences : UserSharedPreferences
    private val service = RetrofitClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historial_view)
        userSharedPreferences = UserSharedPreferences(this)
        val user = userSharedPreferences.getUser()!!.user

        val back = findViewById<ImageButton>(R.id.backButton)
        back.setOnClickListener {
            finish()
        }

        recyclerView = findViewById(R.id.recyclerViewHistorial)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Iniciar una coroutine dentro del alcance del ciclo de vida de la Activity
        lifecycleScope.launch {
            val reservations = getReservationsByUser(user.id)
            adapter = HistorialAdapter(reservations)
            recyclerView.adapter = adapter
        }
    }


    private suspend fun getReservationsByUser(id: Int): List<Reservation> {
        return suspendCoroutine { continuation ->
            var call = service.getReservationsByIdUser(id)

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