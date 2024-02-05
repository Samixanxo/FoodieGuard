package com.example.foodie_guardv0.Activity


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.foodie_guard0.R
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences


class user_fragment : Fragment() {

    lateinit var userSharedPreferences : UserSharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        return view


    }
}
