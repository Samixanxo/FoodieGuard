package com.example.foodie_guardv0.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_view)
        val btnLogin = findViewById<Button>(R.id.button3)
        // Asigna un listener al botón para manejar el clic
        btnLogin.setOnClickListener {
            // Aquí es donde se dirigirá a la LoginActivity
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}
