package com.example.foodie_guardv0.Activity

import MapFragment
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.foodie_guard0.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_view)

        if(intent.getBooleanExtra("edituser", false)){
            finish()
        }


        setupBottomMenu()
    }
    private fun setupBottomMenu() {
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_bar)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            onItemSelectedListener(item)
            true
        }

        bottomNavigationView.selectedItemId = R.id.homePage
    }

    private fun onItemSelectedListener(item: MenuItem): Boolean {
        val fragmentManager = supportFragmentManager

        return when (item.itemId) {
            R.id.homePage -> {
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment = home_fragment()
                fragmentTransaction.replace(R.id.navHostFragment, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                true
            }

            R.id.searchPage -> {
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment = search_Fragment()
                fragmentTransaction.replace(R.id.navHostFragment, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                true
            }

            R.id.profilePage -> {
                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment = user_fragment()
                fragmentTransaction.replace(R.id.navHostFragment, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                true
            }

            R.id.mapPage -> {

                val fragmentTransaction = fragmentManager.beginTransaction()
                val fragment = MapFragment()
                fragmentTransaction.replace(R.id.navHostFragment, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                true
            }

            else -> throw IllegalArgumentException("item not implemented : " + item.itemId)
        }
    }
}

