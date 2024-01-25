package com.example.foodie_guardv0.Activity


import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foodie_guard0.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

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
        return when (item.itemId) {
            R.id.homePage -> {

                showPageFragment(R.drawable.baseline_home_24, R.string.homeString)
                true
            }

            R.id.searchPage -> {
                showPageFragment(R.drawable.baseline_search_24, R.string.searchString)
                true
            }

            R.id.profilePage -> {
                showPageFragment(R.drawable.baseline_person_24, R.string.profileString)
                true
            }

            else -> throw IllegalArgumentException("item not implemented : " + item.itemId)
        }
    }
    private fun showPageFragment(@DrawableRes iconId: Int, @StringRes title: Int) {
        val frg: Fragment = home_fragment.newInstance(iconId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, frg)
            .commitAllowingStateLoss()
    }
}

