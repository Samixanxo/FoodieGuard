package com.example.foodie_guardv0.Activity.actions

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.graphics.drawable.ColorDrawable
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddRestaurantActivity : AppCompatActivity() {
    lateinit var progressBar: ProgressBar

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_restaurant)
        progressBar = findViewById(R.id.progressBar)
        val sendButton = findViewById<Button>(R.id.button3)
        val back = findViewById<ImageButton>(R.id.backButton)

        val name = findViewById<EditText>(R.id.addRestaurantName)
        val address = findViewById<EditText>(R.id.addRestaurantAddress)
        val phone = findViewById<EditText>(R.id.addRestaurantPhone)
        val type = findViewById<EditText>(R.id.addRestaurantType)
        val email = findViewById<EditText>(R.id.addRestaruantEmail)

        sendButton.setOnClickListener {
            when {
                name.text.isEmpty() -> {
                    name.error = "Introduce el nombre del restaurante"
                    name.requestFocus()
                }
                address.text.isEmpty() -> {
                    address.error = "Introduce la direccion del restaurante"
                    address.requestFocus()
                }
                phone.text.isEmpty() -> {
                    phone.error = "Introduce un telefono de contacto"
                    phone.requestFocus()
                }
                !isValidPhone(phone.text.toString()) -> {
                    phone.error = "Introduce un número de teléfono válido"
                    phone.requestFocus()
                }
                type.text.isEmpty() -> {
                    type.error = "Introduce el tipo de restaurante"
                    type.requestFocus()
                }
                email.text.isEmpty() -> {
                    email.error = "Introduce un correo electrónico"
                    email.requestFocus()
                }
                !isValidEmail(email.text.toString()) -> {
                    email.error = "Introduce un correo electrónico válido"
                    email.requestFocus()
                }
                else -> {
                    progressBar.visibility = View.VISIBLE
                    val message = "Nombre del restaurante: ${name.text} \n" +
                            "Dirección: ${address.text} \n" +
                            "Teléfono: ${phone.text} \n" +
                            "Tipo de comida: ${type.text} \n" +
                            "Correo electrónico: ${email.text}"
                    sendEmail(message)
                }
            }
        }

        back.setOnClickListener {
            finish()
        }
    }

    private fun sendEmail(message: String) {
        val call = RetrofitClient.apiService.sendConfirmationEmail(message)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    Log.e("testeo", "se ha enviado correctamente")
                    showPopUpMenu()
                } else {
                    Log.e("testeo", "Ha habido un error en la respuesta")
                    Toast.makeText(this@AddRestaurantActivity, "Error en la respuesta del servidor", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                progressBar.visibility = View.GONE
                Log.e("testeo", "Error en la solicitud POST: ${t.message}")
                Toast.makeText(this@AddRestaurantActivity, "Error en la solicitud: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun showPopUpMenu() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.edit_menu)
        dialog.window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

        val acceptButton: Button = dialog.findViewById(R.id.acceptButton)
        acceptButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPhone(phone: String): Boolean {
        return phone.matches(Regex("^\\d{9}\$"))
    }
}
