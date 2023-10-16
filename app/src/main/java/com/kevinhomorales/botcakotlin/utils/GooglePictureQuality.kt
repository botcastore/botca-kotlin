package com.kevinhomorales.botcakotlin.utils

class GooglePictureQuality {
    companion object {
        val shared = GooglePictureQuality()
    }

    fun setQuality(url: String, quality: String): String {
        val url = url.split("=")
        val finalUrl = url[0] + "=s" + quality
        return finalUrl
    }
}