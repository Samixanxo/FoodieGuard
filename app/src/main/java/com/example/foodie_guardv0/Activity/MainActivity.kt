package com.example.foodie_guardv0.Activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.dataclass.ActualUser
import com.example.foodie_guardv0.deprecated.LoginActivity
import com.example.foodie_guardv0.retrofitt.RetrofitClient
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class MainActivity : AppCompatActivity() {

    lateinit var userSharedPreferences : UserSharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        val splashscreen = installSplashScreen()

        userSharedPreferences = UserSharedPreferences(this)
        splashscreen.setKeepOnScreenCondition{true}

        if(userSharedPreferences.getUser() != null){
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
        } else{
            splashscreen.setKeepOnScreenCondition{false}
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))        }

        finish()
}}
