package com.kevinhomorales.botcakotlin.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.kevinhomorales.botcakotlin.NetworkManager.response.Card
import com.kevinhomorales.botcakotlin.NetworkManager.response.Metadatum

class CardManager {
    companion object {
        val shared = CardManager()
    }

    private val CARD_RESPONSE_KEY = "CARD_RESPONSE_KEY"

    fun saveCard(card: Card, context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(card)
        prefsEditor.putString(CARD_RESPONSE_KEY, json)
        prefsEditor.commit()

    }
    fun getCard(context: Context): Card {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json: String? = mPrefs.getString(CARD_RESPONSE_KEY, "")
        var obj: Card
        if (gson.fromJson(json, Card::class.java) == null) {
            obj = Card(Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, 0,  0, Constants.clearString, Constants.clearString, Constants.clearString, Metadatum(Constants.clearString), Constants.clearString)
        } else {
            obj = gson.fromJson(json, Card::class.java)
        }
        return  obj
    }
    fun removeCard(context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.remove(CARD_RESPONSE_KEY).apply()
    }
}