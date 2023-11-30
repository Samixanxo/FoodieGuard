package com.example.foodie_guardv0.retrofitt
import com.example.foodie_guardv0.dataclass.Restaurant
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService {

    @GET("Restaurant")
    fun getRestaurant(@Url url:String): Response<Restaurant>
}