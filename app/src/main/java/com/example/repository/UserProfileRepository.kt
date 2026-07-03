package com.example.repository

import com.example.data.UserProfile
import com.example.data.UserProfileDao
import kotlinx.coroutines.flow.Flow

class UserProfileRepository(private val userProfileDao: UserProfileDao) {
    val userProfile: Flow<UserProfile?> = userProfileDao.getUserProfile()

    suspend fun saveUserProfile(profile: UserProfile) {
        userProfileDao.saveUserProfile(profile)
    }

    suspend fun getUserProfileOneShot(): UserProfile? {
        return userProfileDao.getUserProfileOneShot()
    }
}
