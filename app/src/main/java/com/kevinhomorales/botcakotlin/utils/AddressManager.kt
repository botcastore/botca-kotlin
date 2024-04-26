package com.kevinhomorales.botcakotlin.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.kevinhomorales.botcakotlin.NetworkManager.response.Address

class AddressManager {
    companion object {
        val shared = AddressManager()
    }

    private val ADDRESS_RESPONSE_KEY = "ADDRESS_RESPONSE_KEY"

    fun saveAddress(address: Address, context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(address)
        prefsEditor.putString(ADDRESS_RESPONSE_KEY, json)
        prefsEditor.commit()

    }
    fun getAddress(context: Context): Address {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json: String? = mPrefs.getString(ADDRESS_RESPONSE_KEY, "")
        var obj: Address
        if (gson.fromJson(json, Address::class.java) == null) {
            obj = Address(Constants.clearString,Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, 0, Constants.clearString, false, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString)
        } else {
            obj = gson.fromJson(json, Address::class.java)
        }
        return  obj
    }
    fun removeAddress(context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.remove(ADDRESS_RESPONSE_KEY).apply()
    }
}