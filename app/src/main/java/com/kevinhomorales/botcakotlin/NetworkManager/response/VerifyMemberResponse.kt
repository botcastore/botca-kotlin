package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable

data class VerifyMemberResponse (
    val isMember: Boolean,
    val email: String,
    val avatarURL: String? = null,
    val displayName: String? = null,
    val provider: String? = null,
    var password: String? = null
): Serializable