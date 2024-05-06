package com.example.foodie_guardv0.Activity.actions

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R

class PremiumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.premium_view)

        val back = findViewById<ImageButton>(R.id.backButton)
        back.setOnClickListener() {
            finish()
        }

    }
}