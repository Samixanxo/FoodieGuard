package com.example.foodie_guardv0.Activity



import MapFragment
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.sharedPreferences.UserSharedPreferences
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    lateinit var userSharedPreferences : UserSharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_view)

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
    /*private fun showPageFragment() {
       val frg: Fragment = home_fragment.newInstance(iconId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, frg)
            .commitAllowingStateLoss()
    }*/
}

