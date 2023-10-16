package com.kevinhomorales.botcakotlin.login.services.model

import com.kevinhomorales.botcakotlin.login.services.response.User

data class LoginModel (
    val avatarURL: String,
    val displayName: String,
    val email: String,
    val password: String
)