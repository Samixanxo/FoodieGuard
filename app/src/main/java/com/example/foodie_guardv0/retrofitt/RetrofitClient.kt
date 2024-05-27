package com.example.foodie_guardv0.retrofitt

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {




    private const val BASE_URL = "http://192.168.81.18:8080/api/"


    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService = retrofit.create(ApiService::class.java)

}
