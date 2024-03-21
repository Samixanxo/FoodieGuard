package com.example.foodie_guardv0.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.ActualUser
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

        val textoClicable = findViewById<TextView>(R.id.button2)
        textoClicable.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        val entrar = findViewById<Button>(R.id.button3)

        entrar.setOnClickListener(){
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    comprobardatos()
                } catch (e: Exception) {
                    Log.e("Resultado", e.message.toString())
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
                        finish()
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {

                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)

                        Log.e("Resultado", response.body().toString())
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
