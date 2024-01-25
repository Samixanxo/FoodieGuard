package com.example.foodie_guardv0.provider

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.foodie_guardv0.dataclass.Address
import com.example.foodie_guardv0.dataclass.User
import java.time.LocalDate

class UserProvider {
    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        val userList = listOf(
            User(
                name = "Saioa",
                surname = "Alejo",
                email = "saioa@example.com",
                password = "1234"

            ))

    }
}
