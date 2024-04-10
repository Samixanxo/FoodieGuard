package com.example.foodie_guardv0.Activity

import android.content.Intent
import com.example.foodie_guard0.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guardv0.dataclass.User
import com.example.foodie_guardv0.retrofitt.RetrofitClient.apiService
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import retrofit2.Call


class RegisterActivity : AppCompatActivity() {

    lateinit var userSharedPreferences : UserSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_view)
        userSharedPreferences = UserSharedPreferences(this)

        val btnRegister = findViewById<Button>(R.id.signUp)

        btnRegister.setOnClickListener {
            val username = findViewById<View>(R.id.name) as EditText
            val name = username.text.toString()

            val lastNameInput = findViewById<View>(R.id.lastname) as EditText
            val surname = lastNameInput.text.toString()

            val emailInput  = findViewById<View>(R.id.email) as EditText
            val email = emailInput.text.toString()

            val passwordInput = findViewById<View>(R.id.passwordInput) as EditText
            val password = passwordInput.text.toString()

            val confirmPasswordInput  = findViewById<View>(R.id.confirm_password) as EditText
            val confirmPassword = confirmPasswordInput.text.toString()

            val passwordMatch = password == confirmPassword
            if (passwordMatch){
                val user = User(0,name,surname,email,confirmPassword,"")
                val call = apiService.createUser(user)
                call.enqueue(object : retrofit2.Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                        if (response.isSuccessful) {
                            println("Solicitud POST exitosa")
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                        } else {
                           errorDialog("No se ha podido registrar tu usuario, inténtalo mas tarde.")
                        }
                    }
                    override fun onFailure(call: Call<Void>, t: Throwable) {
                       errorDialog("No ha sido posible conectarse al servicio, por favor, inténtalo de nuevo mas tarde")
                    }
                })
            } else{
                println("las contraseñas no coinciden")
            }
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