package com.example.foodie_guardv0.Activity


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle

import android.util.Log
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.PopupWindow
import android.widget.TextView
import android.graphics.Color
import android.view.animation.AnimationUtils

import com.example.foodie_guard0.R
import com.example.foodie_guardv0.deprecated.LoginActivity
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

        val editButton = view.findViewById<Button>(R.id.EditButton)
        editButton.setOnClickListener {
            showPopupWindow(inflater, view)

        }


        return view
    }

    private fun showPopupWindow(inflater: LayoutInflater, parentView: View) {

        val popupView = inflater.inflate(R.layout.edit_menu, null)
        val animation = AnimationUtils.loadAnimation(context, R.anim.pop_up_menu)
        popupView.startAnimation(animation)


        parentView.findViewById<View>(R.id.background_overlay).visibility = View.VISIBLE
        parentView.findViewById<View>(R.id.UserCard).visibility = View.INVISIBLE
        val cancelButton = popupView.findViewById<Button>(R.id.canecelChangeButton)



        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )
        cancelButton.setOnClickListener {
            parentView.findViewById<View>(R.id.background_overlay).visibility = View.INVISIBLE
            parentView.findViewById<View>(R.id.UserCard).visibility = View.VISIBLE
            popupWindow.dismiss()
        }
        popupWindow.isOutsideTouchable = false
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        popupWindow.showAtLocation(
            parentView,
            Gravity.TOP,
            0,
            0
        )


    }
}

