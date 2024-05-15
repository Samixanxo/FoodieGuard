package com.example.foodie_guardv0.Activity.info

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R

class AboutUsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.aboutus_view)

        val back = findViewById<ImageButton>(R.id.backButton)
        back.setOnClickListener() {
            finish()
        }

    }
}