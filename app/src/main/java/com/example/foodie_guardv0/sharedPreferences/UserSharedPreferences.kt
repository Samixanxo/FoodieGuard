package com.example.foodie_guardv0.sharedPreferences

import android.content.Context
import android.content.SharedPreferences
import com.example.foodie_guardv0.dataclass.ActualUser
import com.google.gson.Gson

class UserSharedPreferences(context: Context) {
    private val sharedPreferences : SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

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

    fun clearUser() {
        val editor = sharedPreferences.edit()
        editor.remove("user")
        editor.apply()
    }
}