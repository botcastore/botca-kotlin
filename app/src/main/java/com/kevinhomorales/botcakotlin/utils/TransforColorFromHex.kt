package com.kevinhomorales.botcakotlin.utils

import android.graphics.Color

class TransforColorFromHex {

    companion object {
        val shared = TransforColorFromHex()
    }

    fun getColorFromHex(hexColor: String): Int {
        // Remueve el carácter '#' si está presente
        val colorString = if (hexColor.startsWith("#")) hexColor.substring(1) else hexColor
        // Convierte la cadena hexadecimal en un color de Android
        return Color.parseColor("#$colorString")
    }

}