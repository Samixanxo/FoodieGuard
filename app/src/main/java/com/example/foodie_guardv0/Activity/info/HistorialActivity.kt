package com.example.foodie_guardv0.Activity.info

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.adapters.historial.HistorialAdapter
import com.example.foodie_guardv0.adapters.restaurants.RestaurantAdapter
import com.example.foodie_guardv0.dataclass.Reservation
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
    lateinit var userSharedPreferences: UserSharedPreferences
    private val service = RetrofitClient.retrofit.create(ApiService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.historial_view)
        userSharedPreferences = UserSharedPreferences(this)
        val user = userSharedPreferences.getUser()?.user


        if (user == null) {
            Log.e("User Error", "User not found in SharedPreferences")
            finish()
            return
        }

        val back = findViewById<ImageButton>(R.id.backButton)
        back.setOnClickListener {
            finish()
        }

        val myButton = findViewById<Button>(R.id.myButton)
        val myTextView = findViewById<TextView>(R.id.myTextView)

        myButton.setOnClickListener {
            if (myTextView.visibility == View.GONE) {
                myTextView.visibility = View.VISIBLE
            } else {
                myTextView.visibility = View.GONE
            }
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                initRecyclerRestaurant(getReservationsByUser(user.id))
            } catch (e: Exception) {
                Log.e("Initialization Error", "Failed to initialize reservations", e)
            }
        }

    }

    private suspend fun getReservationsByUser(id: Int): List<Reservation> {
        return suspendCoroutine { continuation ->
            val call = service.getReservationsByIdUser(id)

            call.enqueue(object : Callback<List<Reservation>> {
                override fun onResponse(call: Call<List<Reservation>>, response: Response<List<Reservation>>) {
                    if (response.isSuccessful) {
                        val reservations = response.body()
                        Log.e("result", reservations.toString())
                        if (reservations != null) {
                            continuation.resume(reservations)
                        } else {
                            continuation.resumeWithException(Exception("Reservations response is null"))
                        }
                    } else {
                        continuation.resumeWithException(Exception("API error: ${response.code()}"))
                        Log.e("API Error", "Error code: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<List<Reservation>>, t: Throwable) {
                    continuation.resumeWithException(t)
                    Log.e("API Error", "Request failed", t)
                }
            })
        }
    }

    private fun initRecyclerRestaurant(reservations : List<Reservation>) {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewHistorial)
        recyclerView?.layoutManager = LinearLayoutManager(this)
        recyclerView?.adapter = HistorialAdapter(reservations)
    }
}