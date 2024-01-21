package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable

data class LoginResponse (
    val me: Me
): Serializable
data class Me (
    val user: UserResponse,
    val token: String? = null,
    val refeshToken: String? = null
): Serializable
data class UserResponse (
    val userID: String,
    val email: String,
    val displayName: String,
    val phoneNumber: String,
    val isActive: Boolean,
    val birthday: String? = null,
    val avatarURL: String? = null,
    val provider: String,
    val password: String,
    val gender: String? = null,
    val idRol: String,
    val createdAt: String,
    val updatedAt: String,
    val customStripeID: String,
    val rolName: String? = null
): Serializable