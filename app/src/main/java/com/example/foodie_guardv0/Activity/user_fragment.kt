package com.example.foodie_guardv0.Activity


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.example.foodie_guard0.R
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences


class user_fragment : Fragment() {
    lateinit var userSharedPreferences: UserSharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_user, container, false)

        if(container != null){
            userSharedPreferences = UserSharedPreferences(container.context)
        }


        val actualUser = userSharedPreferences.getUser()!!.user
        view.findViewById<TextView>(R.id.tv_User).text = actualUser!!.name + " " + actualUser!!.surname
        view.findViewById<TextView>(R.id.tv_Email).text = actualUser!!.email

        val buttonEditUser = view.findViewById<Button>(R.id.bt_editUser)
        buttonEditUser.setOnClickListener() {
            startActivity(Intent(activity, EditUser::class.java))
        }

        val logoutButton = view.findViewById<Button>(R.id.bt_CloseSession)
        logoutButton.setOnClickListener {
            userSharedPreferences.clearUser()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

        val ownerButton = view.findViewById<Button>(R.id.bt_NewRestaurant)
        ownerButton.setOnClickListener {
            val intent = Intent(activity, AddRestaurantActivity::class.java)
            startActivity(intent)
        }



        return view
    }


}


