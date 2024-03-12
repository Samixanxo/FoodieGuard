package com.example.foodie_guardv0.Activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Restaurant
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class InfoRestaurant : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_restaurant)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapUbication) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        val id = intent.getIntExtra("id", Int.MIN_VALUE)
        val name = intent.getStringExtra("name")
        val photo = intent.getStringExtra("photo")
        val description = intent.getStringExtra("description")
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")
        val address = intent.getStringExtra("address")
        val medianprice = intent.getStringExtra("medianprice")
        val foodtype = intent.getStringExtra("type")


        val RestaurantName = findViewById<TextView>(R.id.restaurantName)
        val ImageRestaurant = findViewById<ImageView>(R.id.imageRestaurant)
        val DescriptionRestaurant = findViewById<TextView>(R.id.descriptionRestaurant)
        val PhoneRestaurant = findViewById<TextView>(R.id.phoneRestaurant)
        val EmailRestaurant = findViewById<TextView>(R.id.emailRestaurant)
        val AddressRestaurant = findViewById<TextView>(R.id.addressRestaurant)
        val MedianPrice = findViewById<TextView>(R.id.medianpriceRestaurant)
        val FoodType = findViewById<TextView>(R.id.foodtype)

        val volver = findViewById<ImageButton>(R.id.buttonReturn)
        val menu = findViewById<Button>(R.id.b_viewMenu)


        RestaurantName.text = name
        Glide.with(ImageRestaurant.context).load(photo).into(ImageRestaurant)
        DescriptionRestaurant.text = description
        PhoneRestaurant.text = phone.toString()
        EmailRestaurant.text = email
        AddressRestaurant.text = address
        MedianPrice.text = "Aproximate price of " + medianprice.toString() + "€"
        FoodType.text = foodtype
        if (phone != null) {
            Log.e("telefono", phone)
        }

        volver.setOnClickListener() {
            finish()
        }

        menu.setOnClickListener() {
            val intent = Intent(this, MenuRestaurant::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        PhoneRestaurant.setOnClickListener {
            val phoneNumber = PhoneRestaurant.text.toString()
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            if (dialIntent.resolveActivity(packageManager) != null) {
                startActivity(dialIntent)
            } else {

            }
        }

        EmailRestaurant.setOnClickListener {
            val emailAddress = EmailRestaurant.text.toString()
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:$emailAddress")
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent)
            } else {
            }
        }

    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isMyLocationButtonEnabled = false
        if (checkLocationPermission()) {
            map.isMyLocationEnabled = true
            addMarkerRestaurant()

        } else {
            requestLocationPermission()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun addMarkerRestaurant() {
        val lat = intent.getDoubleExtra("lat", 0.0)
        val long = intent.getDoubleExtra("long", 0.0)
        val name = intent.getStringExtra("name")

        map.clear()
        val restaurantLatLng = LatLng(lat, long)
        val markerOptions = MarkerOptions()
            .position(restaurantLatLng)
            .title(name)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        map.addMarker(markerOptions)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(restaurantLatLng, 16f))
    }
}