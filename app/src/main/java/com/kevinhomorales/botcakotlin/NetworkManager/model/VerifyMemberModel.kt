package com.kevinhomorales.botcakotlin.NetworkManager.model

data class VerifyMemberModel (
    val avatarURL: String,
    val displayName: String,
    val email: String,
    val password: String,
    val provider: String
)