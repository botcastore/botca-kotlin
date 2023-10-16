package com.kevinhomorales.botcakotlin.login.services.response

import java.io.Serializable

data class LoginResponse (
    val me: Me
): Serializable
data class Me (
    val user: User,
    val token: String,
    val refeshToken: String
): Serializable
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
): Serializable