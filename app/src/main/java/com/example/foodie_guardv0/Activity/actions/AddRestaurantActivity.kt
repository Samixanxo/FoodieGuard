package com.example.foodie_guardv0.Activity.actions

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.PopupWindow
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import retrofit2.Call

class AddRestaurantActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId", "WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_restaurant)

        val sendButton = findViewById<Button>(R.id.button3)
        val back = findViewById<ImageButton>(R.id.backButton)

        val name = findViewById<EditText>(R.id.addRestaurantName)
        val address = findViewById<EditText>(R.id.addRestaurantAddress)
        val phone = findViewById<EditText>(R.id.addRestaurantPhone)
        val type = findViewById<EditText>(R.id.addRestaurantType)
        val email = findViewById<EditText>(R.id.addRestaruantEmail)

        val dialog = Dialog(this)
        val acceptButton = Button(this)


        sendButton.setOnClickListener{

            if(name.text.isEmpty()){
                name.error = "Introduce el nombre del restaurante"
                name.requestFocus()
            } else if(address.text.isEmpty()){
                address.error = "Introduce la direccion del restaurante"
                address.requestFocus()
            } else if(phone.text.isEmpty()){
                phone.error = "Introduce un telefono de contacto"
                phone.requestFocus()
            } else if(type.text.isEmpty()){
                type.error = "Introduce el tipo de restaurante"
                type.requestFocus()
            } else if(email.text.isEmpty()){
                email.error = "Introduce un correo electronico"
                email.requestFocus()
            } else {
                val message = "Nombre del restaurante: ${name.text} \n" +
                              "Dirección: ${address.text} \n" +
                              "Teléfono: ${phone.text} \n" +
                              "Tipo de comida: ${type.text} \n" +
                              "Correo electrónico: ${email.text}"
                sendEmail(message)
            }
        }

        back.setOnClickListener() {
            finish()
        }

    }
    private fun sendEmail(message:String) {
        val call = RetrofitClient.apiService.sendConfirmationEmail(message)
        call.enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: Call<Void>, response: retrofit2.Response<Void>) {
                if (response.isSuccessful) {
                    println("Solicitud POST exitosa")
                    showPopUpMenu()
                } else{
                    println("Me ise popo")
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                println("Error en la solicitud POST: ${t.message}")
            }
        })

    }

    private fun showPopUpMenu() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.edit_menu)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.hashCode()))

        val acceptButton : Button = dialog.findViewById(R.id.acceptButton)

        acceptButton.setOnClickListener {
            finish()
        }
        dialog.show()
    }



}