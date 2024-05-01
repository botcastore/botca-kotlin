package com.kevinhomorales.botcakotlin.NetworkManager.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AvailableColorsResponse (
    val colors: MutableList<ColorAvailable>
): Serializable

data class ColorAvailable (
    val label: String,
    val total: Int
): Serializable