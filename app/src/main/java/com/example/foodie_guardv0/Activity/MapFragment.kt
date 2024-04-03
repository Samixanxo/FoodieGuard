
import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.android.gms.maps.model.LatLng

import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guardv0.restaurantAdapter.RestaurantSliderAdapter
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


 class MapFragment : Fragment(), OnMapReadyCallback, RestaurantSliderAdapter.OnRestaurantClickListener {
    private val service = RetrofitClient.retrofit.create(ApiService::class.java)

    private lateinit var map: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    lateinit var userSharedPreferences : UserSharedPreferences

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        if (container != null) {
            userSharedPreferences = UserSharedPreferences(container.context)
        }
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        createFragment()

       GlobalScope.launch(Dispatchers.Main) {
            try {
                initRecyclerRestaurant(getRestaurants(""))
                Log.e("Resultado", "correcto")
            } catch (e: Exception) {
                Log.e("Resultado", "Error" + e.message)
            }
        }

        return view
    }

    private fun createFragment() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.uiSettings.isZoomControlsEnabled = true
        if (checkLocationPermission()) {
            map.isMyLocationEnabled = true
            GlobalScope.launch(Dispatchers.Main) {
                userSharedPreferences.getRestaurants()?.let { addMarkers() }
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun checkLocationPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }
    private suspend fun addMarkers() {
        map.clear()
        val list = getRestaurants("")
        for (restaurant in  list) {
            val restaurantLatLng = LatLng(restaurant.lat, restaurant.lon)
            val markerOptions = MarkerOptions()
                .position(restaurantLatLng)
                .title(restaurant.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
            map.addMarker(markerOptions)
        }
    }


    private fun initRecyclerRestaurant(restaurants: List<Restaurant>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.sliderView)
        recyclerView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RestaurantSliderAdapter(restaurants)
        adapter.setOnRestaurantClickListener(object : RestaurantSliderAdapter.OnRestaurantClickListener {
            override fun onRestaurantClick(position: Int) {
                val selectedRestaurant = restaurants[position]

                val lat = selectedRestaurant.lat
                val lang = selectedRestaurant.lon
                val location = LatLng(lat,lang)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15F)
                map.animateCamera(cameraUpdate, 2000, null)
            }
        })

        recyclerView?.adapter = adapter
    }

    private suspend fun getRestaurants(name: String): List<Restaurant> {
        return suspendCoroutine { continuation ->
            var call = service.getRestaurant()
            if (!name.isEmpty()) {
                call = service.getRestaurantByName(name)
            }
            call.enqueue(object : Callback<List<Restaurant>> {
                override fun onResponse(
                    call: Call<List<Restaurant>>,
                    response: Response<List<Restaurant>>
                ) {
                    if (response.isSuccessful) {
                        val respuesta = response.body()

                        userSharedPreferences.saveRes(respuesta!!)
                        Log.e("Resultado", "aaaaaaaaaaaaa")
                        Log.e("Resultado", "--------------------------------------------------------------" + userSharedPreferences.getRestaurants().toString())
                        continuation.resume(respuesta!!)
                    } else {
                        // Manejar error de la API
                        continuation.resumeWithException(Exception("Error de la API"))
                        Log.e("Resultado", "error Api")
                    }
                }

                override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                    // Manejar error de conexión
                    continuation.resumeWithException(t)
                }
            })
        }
    }

     override fun onRestaurantClick(position: Int) {
         TODO("Not yet implemented")
     }


 }


