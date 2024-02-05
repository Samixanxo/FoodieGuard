package com.example.foodie_guardv0.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Restaurant

class InfoRestaurant : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_restaurant)

        val name = intent.getStringExtra("name")
        val photo = intent.getStringExtra("photo")
        val description = intent.getStringExtra("description")
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")
        val address = intent.getStringExtra("address")


        val RestaurantName = findViewById<TextView>(R.id.restaurantName)
        val ImageRestaurant = findViewById<ImageView>(R.id.imageRestaurant)
        val DescriptionRestaurant = findViewById<TextView>(R.id.descriptionRestaurant)
        val PhoneRestaurant = findViewById<TextView>(R.id.phoneRestaurant)
        val EmailRestaurant = findViewById<TextView>(R.id.emailRestaurant)
        val AddressRestaurant = findViewById<TextView>(R.id.addressRestaurant)

        val volver = findViewById<ImageButton>(R.id.buttonReturn)


        RestaurantName.text = name
        Glide.with(ImageRestaurant.context).load(photo).into(ImageRestaurant)
        DescriptionRestaurant.text = description
        PhoneRestaurant.text = phone
        EmailRestaurant.text = email
        AddressRestaurant.text = address

        volver.setOnClickListener() {
            finish()
        }

    }
}