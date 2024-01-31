package com.example.foodie_guardv0.Activity


import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.dataclass.User
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences


class user_fragment : Fragment() {
    lateinit var userSharedPreferences: UserSharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if(container != null){
            userSharedPreferences = UserSharedPreferences(container.context)
        }


        val view = inflater.inflate(R.layout.fragment_user, container, false)

        val actualUser = userSharedPreferences.getUser()
        Log.d("ActualUser","este el el user: $actualUser")
        if (actualUser != null) {
            val user = actualUser.user

            if (user != null) {
                Log.d("UserFragment", "Usuario recuperado: $user")

                view.findViewById<TextView>(R.id.UserLabel).text = user.name
                view.findViewById<TextView>(R.id.emailInput).text = user.email

            } else {
                Log.d("UserFragment", "No se pudo recuperar el usuario de ActualUser")
            }
        } else {
            Log.d("UserFragment", "No se pudo recuperar el usuario de las SharedPreferences")
        }
        val logoutButton = view.findViewById<Button>(R.id.logoutButton)
        logoutButton.setOnClickListener {
            userSharedPreferences.clearUser()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }


        return view
    }
}

