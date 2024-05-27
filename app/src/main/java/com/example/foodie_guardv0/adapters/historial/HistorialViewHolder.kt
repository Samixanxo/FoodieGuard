package com.example.foodie_guardv0.adapters.historial

import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.Reservation
import com.example.foodie_guardv0.dataclass.Restaurant
import com.example.foodie_guardv0.dataclass.Review
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HistorialViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val service = RetrofitClient.retrofit.create(ApiService::class.java)
    lateinit var userSharedPreferences: UserSharedPreferences
    private val DishName = view.findViewById<TextView>(R.id.name)
    private val DishDescription = view.findViewById<TextView>(R.id.description)
    private val photo = view.findViewById<ImageView>(R.id.imageDish)
    private val cardView = view.findViewById<CardView>(R.id.cardView)
    private val textInput = view.findViewById<EditText>(R.id.textInput)
    private val numberInput = view.findViewById<EditText>(R.id.numberInput)
    private val submitButton = view.findViewById<Button>(R.id.submitButton)

    private var isVisible = false
    private var currentRestaurant: Restaurant? = null

    init {
        cardView.setOnClickListener {
            isVisible = !isVisible
            val visibility = if (isVisible) View.VISIBLE else View.GONE
            textInput.visibility = visibility
            numberInput.visibility = visibility
            submitButton.visibility = visibility
        }

        submitButton.setOnClickListener {
            val reviewText = textInput.text.toString()
            val ratingText = numberInput.text.toString()
            userSharedPreferences = UserSharedPreferences(view.context)
            val user = userSharedPreferences.getUser()?.user
            if (reviewText.isNotBlank() && ratingText.isNotBlank()) {
                try {
                    val rating = ratingText.toInt()
                    if (rating in 0..10) {
                        if (currentRestaurant != null) {
                            val review = Review(
                                idRes = currentRestaurant!!.id,
                                idUser = user?.id ?: 0,
                                review = reviewText,
                                rating = rating,
                                id = 0
                            )
                            sendReview(review)
                        } else {
                            Toast.makeText(view.context, "Restaurant information is missing.", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(view.context, "La nota debe comprender un número entre 0 y 10.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(view.context, "Introduce una nota válida.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(view.context, "Porfavor rellena todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    suspend fun render(reservation: Reservation) {
        try {
            val restaurant = restaurants(reservation.idRes)
            currentRestaurant = restaurant
            DishName.text = restaurant.name
            val originalFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
            val desiredFormat = SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy", Locale("es", "ES"))
            val date: Date = originalFormat.parse(reservation.date.toString())
            var formattedDate: String = desiredFormat.format(date)
            formattedDate = formattedDate.split(" ").joinToString(" ") { word ->
                word.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            }
            DishDescription.text = formattedDate
            Glide.with(photo.context).load(restaurant.photo).into(photo)
        } catch (e: Exception) {
            Log.e("Render Error", "Error rendering reservation: ${reservation.id}", e)
        }
    }

    private suspend fun restaurants(id: Int): Restaurant {
        return suspendCoroutine { continuation ->
            val call = service.getRestaurantById(id)

            call.enqueue(object : Callback<Restaurant> {
                override fun onResponse(call: Call<Restaurant>, response: Response<Restaurant>) {
                    if (response.isSuccessful) {
                        val restaurant = response.body()
                        if (restaurant != null) {
                            continuation.resume(restaurant)
                        } else {
                            continuation.resumeWithException(Exception("Restaurant response is null"))
                        }
                    } else {
                        val errorMessage = "API error: ${response.code()} - ${response.message()}"
                        continuation.resumeWithException(Exception(errorMessage))
                        Log.e("API Error", errorMessage)
                    }
                }

                override fun onFailure(call: Call<Restaurant>, t: Throwable) {
                    continuation.resumeWithException(t)
                    Log.e("API Error", "Request failed", t)
                }
            })
        }
    }

    private fun sendReview(review: Review) {
        val call = service.createReview(review)
        call.enqueue(object : Callback<Map<String, Any>> {
            override fun onResponse(call: Call<Map<String, Any>>, response: Response<Map<String, Any>>) {
                if (response.isSuccessful) {
                    Toast.makeText(itemView.context, "La Review se ha enviado!", Toast.LENGTH_SHORT).show()
                    textInput.text.clear()
                    numberInput.text.clear()
                    textInput.visibility = View.GONE
                    numberInput.visibility = View.GONE
                    submitButton.visibility = View.GONE
                    isVisible = false
                } else {
                    Log.d("HistorialViewHolder", "Failed to submit review: ${response.message()}")
                    Toast.makeText(itemView.context, "Failed to submit review.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Map<String, Any>>, t: Throwable) {
                Log.d("HistorialViewHolder", "Error submitting review: ${t.message}")
                Toast.makeText(itemView.context, "Error submitting review.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
