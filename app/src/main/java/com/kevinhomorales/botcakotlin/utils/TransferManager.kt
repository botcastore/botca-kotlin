package com.kevinhomorales.botcakotlin.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.kevinhomorales.botcakotlin.customer.payments.transfer.model.TransferToCheckOut

class TransferManager {
    companion object {
        val shared = TransferManager()
    }

    private val TRANSFER_RESPONSE_KEY = "TRANSFER_RESPONSE_KEY"

    fun saveTransfer(transfer: TransferToCheckOut, context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(transfer)
        prefsEditor.putString(TRANSFER_RESPONSE_KEY, json)
        prefsEditor.commit()

    }
    fun getTransfer(context: Context): TransferToCheckOut {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json: String? = mPrefs.getString(TRANSFER_RESPONSE_KEY, "")
        var obj: TransferToCheckOut
        if (gson.fromJson(json, TransferToCheckOut::class.java) == null) {
            obj = TransferToCheckOut(Constants.clearString, Constants.clearString, Constants.clearString)
        } else {
            obj = gson.fromJson(json, TransferToCheckOut::class.java)
        }
        return  obj
    }
    fun removeTransfer(context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.remove(TRANSFER_RESPONSE_KEY).apply()
    }
}