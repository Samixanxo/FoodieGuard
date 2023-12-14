package com.example.foodie_guardv0.Activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.foodie_guard0.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_view)

        val a = search_Fragment()
        val b = home_fragment()
        val c = user()

        val menu = findViewById<BottomNavigationView>(R.id.bottom_bar)

        menu.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.search_item -> {
                    set(a)
                    true
                }
                R.id.home_item -> {
                    set(b)
                    true
                }
                R.id.user_item -> {
                    set(c)
                    true
                }

                else -> false
            }

    }


}
    private fun set(fragment: Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.navHostFragment, fragment)
            commit()
        }
    }
}
