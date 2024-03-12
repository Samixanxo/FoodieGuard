package com.example.foodie_guardv0.dataclass

data class Review(
    val id: Int,
    val review: String,
    val rating: Int,
    val idRes: Int,
    val idUser: Int,
)
