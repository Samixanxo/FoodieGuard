package com.example.foodie_guardv0.Activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.foodie_guardv0.Activity.auth.LoginActivity
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences

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
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        }
        finish()
}}
