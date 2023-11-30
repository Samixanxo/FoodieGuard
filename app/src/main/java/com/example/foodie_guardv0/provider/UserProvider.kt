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
                lastname = "Alejo",
                email = "saioa@example.com",
                address = Address(
                    city = "City1",
                    street = "Street1",
                    number = "123",
                    postalCode = "12345"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            User(
                name = "Miguel",
                lastname = "Vazquez",
                email = "miguel@example.com",
                address = Address(
                    city = "City2",
                    street = "Street2",
                    number = "456",
                    postalCode = "67890"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            User(
                name = "Fran",
                lastname = "Sonvico",
                email = "fran@example.com",
                address = Address(
                    city = "City3",
                    street = "Street3",
                    number = "789",
                    postalCode = "54321"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            User(
                name = "Miguel",
                lastname = "Vazquez",
                email = "miguel@example.com",
                address = Address(
                    city = "City2",
                    street = "Street2",
                    number = "456",
                    postalCode = "67890"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            User(
                name = "Miguel",
                lastname = "Vazquez",
                email = "miguel@example.com",
                address = Address(
                    city = "City2",
                    street = "Street2",
                    number = "456",
                    postalCode = "67890"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            User(
                name = "Miguel",
                lastname = "Vazquez",
                email = "miguel@example.com",
                address = Address(
                    city = "City2",
                    street = "Street2",
                    number = "456",
                    postalCode = "67890"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            User(
                name = "Miguel",
                lastname = "Vazquez",
                email = "miguel@example.com",
                address = Address(
                    city = "City2",
                    street = "Street2",
                    number = "456",
                    postalCode = "67890"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            User(
                name = "Miguel",
                lastname = "Vazquez",
                email = "miguel@example.com",
                address = Address(
                    city = "City2",
                    street = "Street2",
                    number = "456",
                    postalCode = "67890"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            User(
                name = "Miguel",
                lastname = "Vazquez",
                email = "miguel@example.com",
                address = Address(
                    city = "City2",
                    street = "Street2",
                    number = "456",
                    postalCode = "67890"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            User(
                name = "Miguel",
                lastname = "Vazquez",
                email = "miguel@example.com",
                address = Address(
                    city = "City2",
                    street = "Street2",
                    number = "456",
                    postalCode = "67890"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            User(
                name = "Miguel",
                lastname = "Vazquez",
                email = "miguel@example.com",
                address = Address(
                    city = "City2",
                    street = "Street2",
                    number = "456",
                    postalCode = "67890"
                ),
                dateOfBirth = LocalDate.of(1990, 5, 15)
            ),
            // ... Agrega los demás usuarios con sus direcciones
        )
    }
}
