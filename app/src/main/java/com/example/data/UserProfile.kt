package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1, // Single row constraint
    val username: String = "Hamba Allah",
    val profilePicturePath: String = "",
    val isDarkTheme: Boolean = false,
    val isGpsEnabled: Boolean = true,
    val selectedCity: String = "Jakarta",
    val notifyFajr: Boolean = true,
    val notifyDhuhr: Boolean = true,
    val notifyAsr: Boolean = true,
    val notifyMaghrib: Boolean = true,
    val notifyIsha: Boolean = true
)
