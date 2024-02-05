package com.example.foodie_guardv0.Activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences

class MainActivity : AppCompatActivity() {

    lateinit var userSharedPreferences : UserSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_view)

        userSharedPreferences = UserSharedPreferences(this)

        if(userSharedPreferences.getUser() != null){
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        }

        val btnLogin = findViewById<Button>(R.id.button)
        val btnRegister = findViewById<Button>(R.id.button2)
        btnLogin.setOnClickListener {
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}
