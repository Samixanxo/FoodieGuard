package com.example.foodie_guardv0.Activity

import android.content.Intent
import com.example.foodie_guard0.R
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guardv0.dataclass.User
import com.example.foodie_guardv0.retrofitt.RetrofitClient.apiService
import retrofit2.Call


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_view)




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
                val user = User(name,surname,email,confirmPassword)
                val call = apiService.createUser(user)
                call.enqueue(object : retrofit2.Callback<Void> {
                    override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                        if (response.isSuccessful) {
                            // La solicitud fue exitosa
                            println("Solicitud POST exitosa")
                            val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                            startActivity(intent)
                        } else {
                            val intent = Intent(this@RegisterActivity, HomeActivity::class.java)
                            startActivity(intent)
                            // La solicitud no fue exitosa
                            println("Error en la solicitud POST: ${response.code()}")
                        }
                    }

                    override fun onFailure(call: Call<Void>, t: Throwable) {
                        // Se produjo un error de red u otro tipo de error
                        println("Error en la solicitud POST: ${t.message}")
                    }
                })
            } else{
                println("las contraseñas no coinciden")
            }


        }




    }
}