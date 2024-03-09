package com.kevinhomorales.botcakotlin.NetworkManager.model

data class CompleteProfileModel (
    val avatarURL: String,
    val idUser: String,
    val displayName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val birthday: String,
    val gender: String,
    val provider: String
)