package com.example.foodie_guardv0.Activity

import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.os.Bundle
import com.example.foodie_guard0.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapView : AppCompatActivity(),OnMapReadyCallback {
    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mai)
        createFragment()
    }

    private fun createFragment(){
        val mapFragment : SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap){
        map=googleMap
        createMarker()
    }

    private fun createMarker(){
        val coordinates = LatLng(41.403706,2.173504)
        val marker:MarkerOptions = MarkerOptions().position(coordinates).title("prueba")
        map.addMarker(marker)
    }
}