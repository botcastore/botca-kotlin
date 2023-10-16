package com.kevinhomorales.botcakotlin.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.kevinhomorales.botcakotlin.login.services.response.LoginResponse
import com.kevinhomorales.botcakotlin.login.services.response.Me
import com.kevinhomorales.botcakotlin.login.services.response.User

class UserManager {
    companion object {
        val shared = UserManager()
    }

    private val LOGIN_RESPONSE_KEY = "LOGIN_RESPONSE_KEY"

    fun saveUser(user: LoginResponse, context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        val gson = Gson()
        val json = gson.toJson(user)
        prefsEditor.putString(LOGIN_RESPONSE_KEY, json)
        prefsEditor.commit()

    }
    fun getUser(context: Context): LoginResponse {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
        val gson = Gson()
        val json: String? = mPrefs.getString(LOGIN_RESPONSE_KEY, "")
        var obj: LoginResponse
        if (gson.fromJson(json, LoginResponse::class.java) == null) {
            obj = LoginResponse(Me(User(Constants.clearString,Constants.clearString,Constants.clearString,Constants.clearString,false,Constants.clearString,Constants.clearString,Constants.clearString,Constants.clearString,Constants.clearString,Constants.clearString,Constants.clearString, Constants.clearString),Constants.clearString,Constants.clearString))
        } else {
            obj = gson.fromJson(json, LoginResponse::class.java)
        }
        return  obj
    }
    fun removeUser(context: Context) {
        val mPrefs = PreferenceManager.getDefaultSharedPreferences(context)
//        FirebaseNotificationsManager.unsubscribeTopic(context, getUser(context).role)
        val prefsEditor: SharedPreferences.Editor = mPrefs.edit()
        prefsEditor.remove(LOGIN_RESPONSE_KEY).apply()
        FirebaseAuth.getInstance().signOut()
    }
}