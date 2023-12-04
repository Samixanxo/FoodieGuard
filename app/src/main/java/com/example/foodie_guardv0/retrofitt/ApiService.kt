package com.example.foodie_guardv0.retrofitt
import com.example.foodie_guardv0.dataclass.Restaurant
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface ApiService {
    @GET("http://localhost:8080/api/restaurant")
    fun getRestaurant(): Call<List<Restaurant>>

    @GET ("http://localhost:8080/api/restaurant/{id}")
    fun getRestaurantById(@Path("id") id:Int):Call<Restaurant>
}


