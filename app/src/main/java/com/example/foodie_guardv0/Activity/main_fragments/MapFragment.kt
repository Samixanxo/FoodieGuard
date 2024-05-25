package com.example.foodie_guardv0.Activity.main_fragments

import android.Manifest
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.android.gms.maps.model.LatLng
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guardv0.adapters.restaurants.slider.RestaurantSliderAdapter
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
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
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class MapFragment : Fragment(), OnMapReadyCallback, RestaurantSliderAdapter.OnRestaurantClickListener {
    private val service = RetrofitClient.retrofit.create(ApiService::class.java)

    private lateinit var map: GoogleMap
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    lateinit var userSharedPreferences: UserSharedPreferences
    private var searchRadius = 20
    private var currentRestaurants: List<Restaurant> = emptyList()
    private var showingFavorites = false

    private lateinit var seekBarContainer: View

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

        val distanceSeekBar = view.findViewById<SeekBar>(R.id.distanceSeekBar)
        val distanceTextView = view.findViewById<TextView>(R.id.distanceTextView)
        seekBarContainer = view.findViewById<View>(R.id.seekBarContainer)
        val searchView = view.findViewById<SearchView>(R.id.searchView)
        val myLocationButton = view.findViewById<ImageButton>(R.id.myLocationButton)
        val toggleListSwitch = view.findViewById<Switch>(R.id.toggleListSwitch)

        searchView.setOnClickListener {
            searchView.isIconified = false
        }

        seekBarContainer.visibility = View.GONE

        distanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                searchRadius = progress
                distanceTextView.text = "Radio de Búsqueda: $progress km"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                loadCurrentList()
            }
        })


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { filterRestaurants(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterRestaurants(it) }
                return true
            }
        })

        myLocationButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val userLocation = getUserLocation()
                    if (userLocation != null) {
                        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(userLocation, 15F)
                        map.animateCamera(cameraUpdate)
                    } else {
                        Toast.makeText(context, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Log.e("Resultado", "Error: " + e.message)
                }
            }
        }

        toggleListSwitch.setOnCheckedChangeListener { _, isChecked ->
            showingFavorites = isChecked
            loadCurrentList()
        }

        GlobalScope.launch(Dispatchers.Main) {
            loadCurrentList()
        }

        checkNewUser()

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
        map.uiSettings.isMyLocationButtonEnabled = false
        if (checkLocationPermission()) {
            map.isMyLocationEnabled = true
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
        Log.d("MapFragment", "Solicitando permisos de ubicación")
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val earthRadius = 6371.0

        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon1 - lon2)

        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        return earthRadius * c
    }

    private suspend fun getNearbyRestaurants(userLat: Double, userLon: Double): List<Restaurant> {
        return suspendCoroutine { continuation ->
            val call = service.getRestaurant()
            val limit = 20
            val nearbyResList = ArrayList<Restaurant>()

            call.enqueue(object : Callback<List<Restaurant>> {
                override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                    if (response.isSuccessful) {
                        val respuesta = response.body() ?: emptyList()
                        Log.d("MapFragment", "Respuesta de API recibida: ${respuesta.size} restaurantes")
                        val filteredRestaurants = respuesta.filter { restaurant ->
                            val distance = calculateDistance(userLat, userLon, restaurant.lat, restaurant.lon)
                            distance <= searchRadius
                        }

                        val randomResList = filteredRestaurants.shuffled().take(limit)
                        for (res in randomResList) {
                            for (fav in userSharedPreferences.getFav()) {
                                if (res.id == fav.id) {
                                    res.fav = true
                                    break
                                }
                            }
                        }
                        continuation.resume(randomResList)
                    } else {
                        continuation.resumeWithException(Exception("Error de la API"))
                    }
                }

                override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                    errorDialog("No se ha podido establecer conexión con el servidor, inténtalo de nuevo más tarde.")
                    continuation.resumeWithException(t)
                }
            })
        }
    }



    private suspend fun getRandomRestaurants(): List<Restaurant> {
        return suspendCoroutine { continuation ->
            val call = service.getRestaurant()
            val limit = 20
            val randomResList = ArrayList<Restaurant>()

            call.enqueue(object : Callback<List<Restaurant>> {
                override fun onResponse(call: Call<List<Restaurant>>, response: Response<List<Restaurant>>) {
                    if (response.isSuccessful) {
                        val respuesta = response.body() ?: emptyList()
                        val randomIndices = (0 until respuesta.size).shuffled().take(limit)
                        randomIndices.forEach { index ->
                            randomResList.add(respuesta[index])
                        }
                        Log.e("Respuesta", randomResList.toString())
                        continuation.resume(randomResList)
                        for (res in randomResList) {
                            for (fav in userSharedPreferences.getFav()) {
                                if (res.id == fav.id) {
                                    res.fav = true
                                    break
                                }
                            }
                        }
                    } else {
                        continuation.resumeWithException(Exception("Error de la API"))
                        Log.e("Resultado", "error Api")
                    }
                }

                override fun onFailure(call: Call<List<Restaurant>>, t: Throwable) {
                    errorDialog("No se ha podido establecer conexión con el servidor, inténtalo de nuevo más tarde.")
                    continuation.resumeWithException(t)
                }
            })
        }
    }

    private suspend fun getUserLocation(): LatLng? {
        return suspendCoroutine { continuation ->
            if (checkLocationPermission() && isGPSEnabled()) {
                val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if (location != null) {
                    Log.d("MapFragment", "Ubicación obtenida: ${location.latitude}, ${location.longitude}")
                    continuation.resume(LatLng(location.latitude, location.longitude))
                } else {
                    val locationListener = object : android.location.LocationListener {
                        override fun onLocationChanged(newLocation: Location) {
                            locationManager.removeUpdates(this)
                            Log.d("MapFragment", "Ubicación cambiada: ${newLocation.latitude}, ${newLocation.longitude}")
                            continuation.resume(LatLng(newLocation.latitude, newLocation.longitude))
                        }

                        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                        override fun onProviderEnabled(provider: String) {}
                        override fun onProviderDisabled(provider: String) {
                            continuation.resume(null)
                        }
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
                }
            } else {
                Log.d("MapFragment", "Permisos de ubicación no concedidos o GPS no habilitado")
                continuation.resume(null)
            }
        }
    }


    private fun isGPSEnabled(): Boolean {
        val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!enabled) {
            Toast.makeText(context, "Por favor, habilite el GPS", Toast.LENGTH_SHORT).show()
        }
        return enabled
    }

    private fun initRecyclerRestaurant(restaurants: List<Restaurant>) {
        val recyclerView = view?.findViewById<RecyclerView>(R.id.sliderView)
        recyclerView?.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RestaurantSliderAdapter(restaurants)

        map.clear()

        for (restaurant in restaurants) {
            Log.e("nombre", restaurant.name)
            val restaurantLatLng = LatLng(restaurant.lat, restaurant.lon)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(restaurantLatLng, 15F)
            val markerOptions = MarkerOptions()
                .position(restaurantLatLng)
                .title(restaurant.name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
            val marker = map.addMarker(markerOptions)
            if (marker != null) {
                marker.tag = restaurant
            }

            map.setOnMarkerClickListener { clickedMarker ->
                clickedMarker.showInfoWindow()
                val clickedRestaurant = clickedMarker.tag as? Restaurant
                val position = restaurants.indexOf(clickedRestaurant)
                val itemCount = adapter.itemCount
                if (position in 0 until itemCount) {
                    recyclerView?.smoothScrollToPosition(position)
                }

                true
            }
        }

        adapter.setOnRestaurantClickListener(object : RestaurantSliderAdapter.OnRestaurantClickListener {
            override fun onRestaurantClick(position: Int) {
                recyclerView?.smoothScrollToPosition(position)
                val selectedRestaurant = restaurants[position]
                val lat = selectedRestaurant.lat
                val lang = selectedRestaurant.lon
                val location = LatLng(lat, lang)
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15F)
                map.animateCamera(cameraUpdate, 2000, null)
            }
        })

        recyclerView?.adapter = adapter
    }

    private fun filterRestaurants(query: String) {
        val filteredList = currentRestaurants.filter { restaurant ->
            restaurant.name.contains(query, ignoreCase = true) ||
                    restaurant.type.contains(query, ignoreCase = true) ||
                    restaurant.address.contains(query, ignoreCase = true)
        }
        initRecyclerRestaurant(filteredList)
    }

    private fun errorDialog(message: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Error de conexión")
        builder.setMessage(message)
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            requireActivity().finish()
        }
        builder.show()
    }

    private fun checkNewUser() {
        if (userSharedPreferences.isFirstTime()) {
            showPopUpMenu()
        }
    }

    private fun showPopUpMenu() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.map_info)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.hashCode()))

        val acceptButton: Button = dialog.findViewById(R.id.acceptButton)

        acceptButton.setOnClickListener {
            userSharedPreferences.setFirstTime()
            dialog.dismiss()
        }
        dialog.show()
    }

    override fun onRestaurantClick(position: Int) {
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun loadCurrentList() {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                if (showingFavorites) {
                    val favRestaurants = userSharedPreferences.getFav()
                    if (favRestaurants.isNotEmpty()) {
                        currentRestaurants = favRestaurants
                        initRecyclerRestaurant(favRestaurants)
                    } else {
                        Toast.makeText(context, "No tienes restaurantes favoritos.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    if (isGPSEnabled()) {
                        seekBarContainer.visibility = View.VISIBLE
                        val userLocation = getUserLocation()
                        if (userLocation != null) {
                            Log.d("MapFragment", "Ubicación del usuario: ${userLocation.latitude}, ${userLocation.longitude}")
                            map.clear()
                            val nearbyRestaurants = getNearbyRestaurants(userLocation.latitude, userLocation.longitude)
                            currentRestaurants = nearbyRestaurants
                            initRecyclerRestaurant(currentRestaurants)
                        } else {
                            Log.e("MapFragment", "No se pudo obtener la ubicación del usuario")
                            map.clear()
                            val randomRestaurants = getRandomRestaurants()
                            currentRestaurants = randomRestaurants
                            initRecyclerRestaurant(currentRestaurants)
                        }
                    } else {
                        Log.d("MapFragment", "GPS no habilitado, usando restaurantes aleatorios")
                        seekBarContainer.visibility = View.GONE
                        val randomRestaurants = getRandomRestaurants()
                        currentRestaurants = randomRestaurants
                        initRecyclerRestaurant(currentRestaurants)
                    }
                }
            } catch (e: Exception) {
                Log.e("MapFragment", "Error: " + e.message)
                Toast.makeText(context, "Ocurrió un error al cargar los restaurantes.", Toast.LENGTH_SHORT).show()
            }
        }
    }




}