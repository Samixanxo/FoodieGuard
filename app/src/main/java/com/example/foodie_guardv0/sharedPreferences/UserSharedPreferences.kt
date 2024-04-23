package com.example.foodie_guardv0.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.dataclass.Restaurant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class UserSharedPreferences(context: Context) {
    private val sharedPreferences : SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val favRestaurants = ArrayList<Restaurant>()

    fun saveUser(user: ActualUser) {
        val editor = sharedPreferences.edit()
        val userJson = gson.toJson(user)
        editor.putString("user", userJson)
        editor.apply()
    }

    fun getUser(): ActualUser? {
        val userJson = sharedPreferences.getString("user", null)
        return if (userJson != null) {
            gson.fromJson(userJson, ActualUser::class.java)
        } else {
            null
        }
    }

    fun saveRes(restaurant: List<Restaurant>) {
        val editor = sharedPreferences.edit()
        val resJson = gson.toJson(restaurant)
        editor.putString("restaurant", resJson)
        editor.apply()
    }

    fun saveFav(restaurant: Restaurant) {
        val favRestaurants = getFav().toMutableList()
        favRestaurants.add(restaurant)
        val editor = sharedPreferences.edit()
        val favRestaurantsJson = gson.toJson(favRestaurants)
        editor.putString("favRestaurants", favRestaurantsJson)
        editor.apply()
    }
    fun getFav(): List<Restaurant> {
        val favRestaurantsJson = sharedPreferences.getString("favRestaurants", null)
        return if (favRestaurantsJson != null) {
            gson.fromJson(favRestaurantsJson, object : TypeToken<List<Restaurant>>() {}.type)
        } else {
            emptyList()
        }
    }

    fun deleteFav(restaurant: Restaurant) {
        val favRestaurants = getFav().toMutableList()
        favRestaurants.remove(restaurant)
        val editor = sharedPreferences.edit()
        val favRestaurantsJson = gson.toJson(favRestaurants)
        editor.putString("favRestaurants", favRestaurantsJson)
        editor.apply()
    }

    fun getRestaurants(): List<Restaurant>? {
        val resJson = sharedPreferences.getString("restaurant", null)
        return if (resJson != null) {
            gson.fromJson(resJson, object : TypeToken<List<Restaurant>>() {}.type)
        } else {
            null
        }
    }


    fun clearUser() {
        val editor = sharedPreferences.edit()
        editor.remove("user")
        editor.apply()
    }
}