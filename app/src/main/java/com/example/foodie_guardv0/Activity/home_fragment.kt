package com.example.foodie_guardv0.Activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guardv0.restaurantAdapter.RestaurantAdapter
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

class home_fragment : Fragment(), SearchView.OnQueryTextListener {

    private val service = RetrofitClient.retrofit.create(ApiService::class.java)
    lateinit var userSharedPreferences : UserSharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.recycler_view, container, false)
        val svSearcher = view.findViewById<SearchView>(R.id.svSearcher)

        svSearcher.setOnClickListener {
            svSearcher.isIconified = false
        }

        svSearcher.setOnQueryTextListener(this)

        if (container != null) {
            userSharedPreferences = UserSharedPreferences(container.context)
        }

        GlobalScope.launch(Dispatchers.Main) {
            try {
                initRecyclerRestaurant(restaurants(""))
            } catch (e: Exception) {
                Log.e("Resultado", "Error" + e.message)
            }
        }

        return view
    }

    private fun initRecyclerRestaurant(restaurants: List<Restaurant>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recyclerRestaurant2)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        recyclerView?.adapter = RestaurantAdapter(restaurants)
    }

    private suspend fun restaurants(name: String): List<Restaurant> {
        return suspendCoroutine { continuation ->
            var call = service.getRestaurant()
            if (!name.isEmpty()) {
                call = service.getRestaurantByName(name)
            }
            call.enqueue(object : Callback<List<Restaurant>> {
                override fun onResponse(
                    call: Call<List<Restaurant>>,
                    response: Response<List<Restaurant>>
                ) {
                    if (response.isSuccessful) {
                        val respuesta = response.body()
                        userSharedPreferences.saveRes(respuesta!!)
                        continuation.resume(respuesta!!)
                    } else {
                        // Manejar error de la API
                        continuation.resumeWithException(Exception("Error de la API"))
                        Log.e("Resultado", "error Api")
                    }
                }

                override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                    // Manejar error de conexión
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        GlobalScope.launch(Dispatchers.Main) {
            initRecyclerRestaurant(restaurants(newText))
        }
        return true
    }
}