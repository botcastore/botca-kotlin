package com.kevinhomorales.botcakotlin.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.kevinhomorales.botcakotlin.customer.filterproduct.model.FilterProductModel

class FilterProductManager {
    companion object {
        val shared = FilterProductManager()
    }

    private val FILTER_RESPONSE_KEY = "FILTER_RESPONSE_KEY"

    fun saveFilterProduct(filterProductModel: FilterProductModel, context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(filterProductModel)
        prefsEditor.putString(FILTER_RESPONSE_KEY, json)
        prefsEditor.commit()

    }
    fun getFilterProduct(context: Context): FilterProductModel {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json: String? = mPrefs.getString(FILTER_RESPONSE_KEY, "")
        var obj: FilterProductModel
        if (gson.fromJson(json, FilterProductModel::class.java) == null) {
            obj = FilterProductModel(Constants.clearString, Constants.clearString, Constants.clearString)
        } else {
            obj = gson.fromJson(json, FilterProductModel::class.java)
        }
        return  obj
    }
    fun removeFilterProduct(context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.remove(FILTER_RESPONSE_KEY).apply()
    }
}