package com.example.foodie_guardv0.dataclass
import java.time.LocalDate

public data class User(
    val name: String,
    val lastname: String,
    val email: String,
    val address: Address,
    val dateOfBirth: LocalDate
)
