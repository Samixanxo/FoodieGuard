package com.example.foodie_guardv0.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.dataclass.User
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.retrofitt.RetrofitClient.apiService
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


class LoginActivity : AppCompatActivity() {

    lateinit var userSharedPreferences : UserSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)
        userSharedPreferences = UserSharedPreferences(this)

        val entrar = findViewById<Button>(R.id.button3)

        entrar.setOnClickListener(){
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    comprobardatos()
                    Log.e("Resultado", "correcto")
                } catch (e: Exception) {
                    Log.e("Resultado", "Error" + e.message)
                }
            }
        }

    }

    private suspend fun iniciarSesion(body: Map<String,String>){
        return suspendCoroutine { continuation ->
            val call = apiService.postUser(body)

            call.enqueue(object : Callback<ActualUser> {
                override fun onResponse(call: Call<ActualUser>, response: Response<ActualUser>) {
                    if (response.isSuccessful) {
                        val respuesta = response.body()
                        userSharedPreferences.saveUser(respuesta!!)
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        continuation.resumeWithException(Exception("Error de la API"))
                        Log.e("Resultado", "error Api")
                    }
                }

                override fun onFailure(call: Call<ActualUser>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private suspend fun comprobardatos(){
        var email = findViewById<EditText>(R.id.emailLogin)
        var pass = findViewById<EditText>(R.id.passowrdLogin)

        if (email.text.toString().isEmpty()){
            email.error = "Introduce el Mail"
            email.requestFocus()
        } else if(pass.text.toString().isEmpty()){
            pass.error = "Introduce la contraseña"
            pass.requestFocus()
        } else{
            val datos = mapOf<String, String>(
                "email" to email.text.toString(),
                "password" to pass.text.toString()
            )
            iniciarSesion(datos)
        }

    }

}
