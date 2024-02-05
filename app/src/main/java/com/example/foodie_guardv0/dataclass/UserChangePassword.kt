package com.example.foodie_guardv0.dataclass

data class UserChangePassword(
    val email: String,
    val password: String,
    val newPassword: String
)
