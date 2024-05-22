package com.example.foodie_guardv0.Activity.actions

import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.dataclass.User
import com.example.foodie_guardv0.retrofitt.ApiService
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.retrofitt.RetrofitClient.apiService
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PremiumActivity : AppCompatActivity() {
    lateinit var userSharedPreferences: UserSharedPreferences
    private val service = RetrofitClient.retrofit.create(ApiService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.premium_view)
        userSharedPreferences = UserSharedPreferences(this)
        val actualUser = userSharedPreferences.getUser()?.user ?: return

        val back = findViewById<ImageButton>(R.id.backButton)
        back.setOnClickListener {
            finish()
        }
        val premiumButton = findViewById<Button>(R.id.button3)
        premiumButton.text = if (actualUser.premium == 1) "Desactivar Premium" else "Activar Premium"
        premiumButton.setOnClickListener {
            togglePremiumStatus(actualUser.id)
        }
    }
    private fun togglePremiumStatus(userId: Int) {
        apiService.changePremium(userId).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@PremiumActivity, "Estado premium actualizado correctamente.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@PremiumActivity, "Error al actualizar estado premium: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@PremiumActivity, "Fallo en la llamada: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }

}