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
import android.widget.EditText
import androidx.lifecycle.lifecycleScope

import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.deprecated.LoginActivity
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


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

        val actualUser = userSharedPreferences.getUser()!!.user
        view.findViewById<TextView>(R.id.tv_User).text = actualUser!!.name + " " + actualUser!!.surname
        view.findViewById<TextView>(R.id.tv_Email).text = actualUser!!.email

        val logoutButton = view.findViewById<Button>(R.id.bt_CloseSession)
        logoutButton.setOnClickListener {
            userSharedPreferences.clearUser()

            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }


        return view
    }


}


