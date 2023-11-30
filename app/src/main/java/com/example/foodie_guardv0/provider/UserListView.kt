package com.example.foodie_guardv0.provider

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodie_guard0.R
import com.example.foodie_guardv0.adapter.UserAdapter

class UserListView : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Utiliza setContentView para mostrar la vista desde el archivo XML
        setContentView(
            R.layout.recycler_view

        ) // Reemplaza "activity_main" con el nombre de tu archivo XML de diseño

        initRecyclerView()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRecyclerView(){
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerRestaurant2)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = UserAdapter(UserProvider.userList)
    }

}