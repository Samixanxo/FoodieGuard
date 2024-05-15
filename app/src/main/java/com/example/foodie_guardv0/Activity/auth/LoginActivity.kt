package com.example.foodie_guardv0.Activity.auth

import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.HomeActivity
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.retrofitt.RetrofitClient.apiService
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.suspendCoroutine


class LoginActivity : AppCompatActivity() {

    lateinit var userSharedPreferences : UserSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)
        userSharedPreferences = UserSharedPreferences(this)

        val textoClicable = findViewById<TextView>(R.id.button2)
        val textoCompleto = "No tienes cuenta? Registrate aquí."
        val spannableString = SpannableString(textoCompleto)
        val colorAzul = resources.getColor(android.R.color.holo_blue_dark)
        val start = textoCompleto.indexOf("Registrate aquí")
        spannableString.setSpan(
            ForegroundColorSpan(colorAzul),
            start,
            start + "registrate aquí".length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textoClicable.text = spannableString
        textoClicable.movementMethod = LinkMovementMethod.getInstance()
        textoClicable.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        val entrar = findViewById<Button>(R.id.button3)



        entrar.setOnClickListener(){
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
                        Log.e("Resultado", response.body().toString())
                        userSharedPreferences.saveUser(respuesta!!)
                        finish()
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                    } else {
                        if (response.code() == 404) {
                            errorDialog("El usuario que has introducido no se encuentra en nuestra base de datos, por favor, revisa tus credenciales.")
                        } else{
                            errorDialog("Ha ocurrido un error durante la conexión, por favor, inténtalo de nuevo mas tarde")
                        }
                    }
                }
                override fun onFailure(call: Call<ActualUser>, t: Throwable) {
                    errorDialog("No ha sido posible conectarse al servicio, por favor, inténtalo de nuevo mas tarde")
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

    private fun errorDialog(message:String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error al iniciar sesión")
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->

        }
        builder.show()
    }

    

}
