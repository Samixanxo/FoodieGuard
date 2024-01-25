package com.example.foodie_guardv0.Activity


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.navigation.fragment.findNavController
import com.example.foodie_guard0.R
import com.google.android.material.bottomnavigation.BottomNavigationView


class user : Fragment() {
    companion object {
        fun newInstance(iconId: Int): home_fragment {
            val fragment = home_fragment()
            val args = Bundle()
            args.putInt("iconId", iconId)
            fragment.arguments = args
            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño del fragmento
        val view = inflater.inflate(R.layout.fragment_user, container, false)

        return view
    }
}
