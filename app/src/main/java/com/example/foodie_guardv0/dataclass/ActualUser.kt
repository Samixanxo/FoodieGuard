package com.example.foodie_guardv0.dataclass

import com.google.gson.annotations.SerializedName

data class ActualUser (
    @SerializedName("User") val user: User,
    @SerializedName("Token") val token: String,
)