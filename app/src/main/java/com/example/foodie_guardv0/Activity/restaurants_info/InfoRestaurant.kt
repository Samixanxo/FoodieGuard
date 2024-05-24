package com.example.foodie_guardv0.Activity.restaurants_info

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.Activity.restaurants_info.menu.MenuRestaurant
import com.example.foodie_guardv0.dataclass.Review
import com.example.foodie_guardv0.adapters.review.ReviewAdapter
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class InfoRestaurant : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val service = RetrofitClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info_restaurant)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapUbication) as? SupportMapFragment
        mapFragment?.getMapAsync(this)


        val id = intent.getIntExtra("id", Int.MAX_VALUE)
        val name = intent.getStringExtra("name")
        val photo = intent.getStringExtra("photo")
        val description = intent.getStringExtra("description")
        val phone = intent.getStringExtra("phone")
        val email = intent.getStringExtra("email")
        val address = intent.getStringExtra("address")
        val website = intent.getStringExtra("web") ?: ""
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
        val web = findViewById<TextView>(R.id.webRestaurant)

        val volver = findViewById<ImageButton>(R.id.buttonReturn)
        val menu = findViewById<Button>(R.id.b_viewMenu)
        val reservation = findViewById<Button>(R.id.reservation)
        val displayedWebsite = website.replaceFirst("https://", "")


        RestaurantName.text = name
        Glide.with(ImageRestaurant.context).load(photo).into(ImageRestaurant)
        DescriptionRestaurant.text = description
        PhoneRestaurant.text = phone.toString()
        EmailRestaurant.text = email
        AddressRestaurant.text = address
        MedianPrice.text = "Precio aproximado: " + medianprice.toString() + "€"
        FoodType.text = foodtype
        web.text = displayedWebsite
        if (phone != null) {
            Log.e("telefono", phone)
        }

        volver.setOnClickListener() {
            finish()
        }

        web.setOnClickListener(){
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(website))
            startActivity(intent)
        }

        menu.setOnClickListener() {
            val intent = Intent(this, MenuRestaurant::class.java)
            intent.putExtra("id", id)
            startActivity(intent)
        }

        reservation.setOnClickListener(){
            val intent = Intent(this, CalendarActivity::class.java)
            intent.putExtra("id",id)
            intent.putExtra("photo", photo)
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

        GlobalScope.launch(Dispatchers.Main) {
            try {
                initReviews(getReviews(id))
            }catch (e: Exception){
                Log.e("Reviews Error", "No se ha podido iniciar la lista")

            }
        }

    }

    private suspend fun getReviews(id: Int): List<Review> {
        return suspendCoroutine { continuation ->
            var call = service.getReviewsByIdRes(id)

            call.enqueue(object : Callback<List<Review>> {
                override fun onResponse(
                    call: Call<List<Review>>,
                    response: Response<List<Review>>
                ) {
                    if (response.isSuccessful) {
                        Log.e("Respuesta existosa", "todo fresco")
                        val respuesta = response.body()
                        Log.e("respuesta", response.body().toString())
                        continuation.resume(respuesta!!)
                    } else {
                        continuation.resumeWithException(Exception("Error de la API"))
                        Log.e("Resultado", "error Api")
                    }
                }

                override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                    continuation.resumeWithException(t)
                }


            })
        }
    }

   fun initReviews(reviews: List<Review>){
        val recyclerView = findViewById<RecyclerView>(R.id.reviewRecyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = ReviewAdapter(reviews)
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