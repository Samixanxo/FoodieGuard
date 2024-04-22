package com.example.foodie_guardv0.dataclass

data class Restaurant(
    val id:Int,
    val name: String,
    val description: String,
    val phone: Long,
    val email: String,
    val medianprice: Int,
    val address: String,
    val photo:String,
    val website:String,
    val type:String,
    val lat:Double,
    val lon:Double,
    var fav:Boolean = false

)
