package com.example.foodie_guardv0.dataclass

import com.google.gson.annotations.SerializedName

data class ResponseErrors (
    @SerializedName("message") val message: String,
    @SerializedName("error") val error: String,
)