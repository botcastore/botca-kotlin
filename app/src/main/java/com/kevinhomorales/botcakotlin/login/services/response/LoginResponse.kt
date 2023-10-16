package com.kevinhomorales.botcakotlin.login.services.response

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    val me: Me
)
data class Me (
    val user: User,
    val token: String,
    val refeshToken: String
)
data class User (
    val userID: String,
    val email: String,
    val displayName: String,
    val phoneNumber: String,
    val isActive: Boolean,
    val birthday: String,
    val avatarURL: Any? = null,
    val provider: String,
    val password: String,
    val gender: String,
    val idRol: String,
    val createdAt: String,
    val updatedAt: String
)