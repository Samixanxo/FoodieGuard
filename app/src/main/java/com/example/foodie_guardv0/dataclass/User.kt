package com.example.foodie_guardv0.dataclass

import java.lang.reflect.Constructor


data class User(
    val id: Int,
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    var image: String,
    var premium: Int,
)
