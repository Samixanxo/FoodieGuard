package com.example.foodie_guardv0.dataclass

data class Dish(

    val id: Int,
    val idRes: Int,
    val name: String,
    val description: String,
    val price: Int,
    val photo:String,
    val allergens: String
)
