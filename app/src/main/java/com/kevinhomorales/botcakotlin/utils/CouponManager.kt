package com.kevinhomorales.botcakotlin.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import com.kevinhomorales.botcakotlin.NetworkManager.response.Coupon
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponsResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Metadatum

class CouponManager {
    companion object {
        val shared = CouponManager()
    }

    private val COUPON_RESPONSE_KEY = "COUPON_RESPONSE_KEY"

    fun saveCoupon(coupon: CouponsResponse, context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(coupon)
        prefsEditor.putString(COUPON_RESPONSE_KEY, json)
        prefsEditor.commit()

    }
    fun getCoupon(context: Context): CouponsResponse {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json: String? = mPrefs.getString(COUPON_RESPONSE_KEY, "")
        var obj: CouponsResponse
        if (gson.fromJson(json, CouponsResponse::class.java) == null) {
            obj = CouponsResponse(Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, 0, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Coupon(Constants.clearString, Constants.clearString, Constants.clearString, false, false, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString))
        } else {
            obj = gson.fromJson(json, CouponsResponse::class.java)
        }
        return  obj
    }
    fun removCoupon(context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.remove(COUPON_RESPONSE_KEY).apply()
    }
}