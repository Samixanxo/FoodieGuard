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
        val confirmChanges = popupView.findViewById<Button>(R.id.changePasswordButton)



        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT,
            true
        )

        popupWindow.isOutsideTouchable = false
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        popupWindow.showAtLocation(
            parentView,
            Gravity.TOP,
            0,
            0
        )
        cancelButton.setOnClickListener {
            val animationOut = AnimationUtils.loadAnimation(context, R.anim.pop_out_menu)
            popupView.startAnimation(animationOut)
            parentView.findViewById<View>(R.id.background_overlay).visibility = View.INVISIBLE
            parentView.findViewById<View>(R.id.UserCard).visibility = View.VISIBLE
            popupWindow.dismiss()
        }


        confirmChanges.setOnClickListener {
            val passwordInput = popupView.findViewById<EditText>(R.id.editPasswordInput)
            val confirmPasswordInput = popupView.findViewById<EditText>(R.id.changePasswordConfirmation)
            val actualUser = userSharedPreferences.getUser()
            val user = actualUser?.user
            val userEmail = user?.email
            val userPassword = user?.password

            if (passwordInput.text.toString().isEmpty()) {
                passwordInput.error = "Introduce la nueva contraseña"
                passwordInput.requestFocus()
            } else if (confirmPasswordInput.text.toString().isEmpty()) {
                confirmPasswordInput.error = "Confirma la nueva contraseña"
                confirmPasswordInput.requestFocus()
            } else {
                if (passwordInput.text.toString() == confirmPasswordInput.text.toString()) {
                    val datos = mapOf(
                        "email" to userEmail.toString(),
                        "password" to userPassword.toString(),
                        "newPassword" to passwordInput.text.toString(),
                    )
                    // Llamar a changePassword dentro de una coroutine
                    lifecycleScope.launch {
                        changePassword(datos)
                        parentView.findViewById<View>(R.id.background_overlay).visibility = View.INVISIBLE
                        parentView.findViewById<View>(R.id.UserCard).visibility = View.VISIBLE
                        popupWindow.dismiss()
                    }
                }
            }

        }




    }

    private suspend fun changePassword(body: Map<String,String>) {
        return suspendCancellableCoroutine { continuation ->
            val call = RetrofitClient.apiService.changePassword(body)

            call.enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        continuation.resume(Unit) // Resume successfully
                    } else {
                        continuation.resumeWithException(Exception("Error en la llamada: ${response.code()}"))
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    if (continuation.isCancelled) return
                    continuation.resumeWithException(t)
                }
            })

            continuation.invokeOnCancellation {
                call.cancel()
            }
        }
    }



}


