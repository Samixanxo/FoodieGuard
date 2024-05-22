package com.example.foodie_guardv0.dataclass

import java.util.Date

data class Reservation(
    val id: Int,
    val date: Date,
    val idRes: Int,
    val idUser: Int
)
