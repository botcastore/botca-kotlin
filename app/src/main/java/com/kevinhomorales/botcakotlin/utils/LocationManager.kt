package com.kevinhomorales.botcakotlin.utils

import android.content.Context
import android.os.Build
import java.util.Locale

class LocationManager {

    companion object {
        val shared = LocationManager()
    }
    fun getDeviceLanguage(context: Context): String {
        val locale: Locale

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.resources.configuration.locales[0]
        } else {
            @Suppress("DEPRECATION")
            locale = context.resources.configuration.locale
        }

        return locale.language
    }
}