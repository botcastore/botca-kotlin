package com.kevinhomorales.botcakotlin.NetworkManager.response

import java.io.Serializable

data class VersionResponse (
    val config: MutableList<Config>,
): Serializable

data class Config (
    val name: String,
    val value: String
): Serializable