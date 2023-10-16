package com.kevinhomorales.botcakotlin.login.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.login.services.model.LoginModel
import com.kevinhomorales.botcakotlin.login.services.request.LoginRequest
import com.kevinhomorales.botcakotlin.login.services.response.LoginResponse
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class LoginViewModel: ViewModel() {

    fun googleSignIn(activity: MainActivity) {
        activity.showLoading(activity.getString(R.string.loading_login))
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(activity, googleConf)
        activity.startActivityForResult(googleClient.signInIntent, Constants.GOOGLE_SIGN_IN)
        googleClient.signOut()
    }

    fun postLogin(result: Task<AuthResult>, mainActivity: MainActivity, completion:(LoginResponse) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val loginModel = LoginModel("","","goyeselcoca@gmail.com", "123456789")
            val call = mainActivity.getRetrofit().create(LoginRequest::class.java).postLogin("users/login", jsonToSend(loginModel))
            val loginResponse = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    completion(loginResponse!!)
                } else {
                    val error = call.errorBody()
                    val jsonObject = JSONObject(error!!.string())
                    val message = jsonObject.getString("status")
                    if (message == Constants.sessionExpired) {
                        Alerts.warning(message, mainActivity.getString(R.string.error_message), mainActivity)
                        return@runOnUiThread
                    }
                    Alerts.warning(message, mainActivity.getString(R.string.error_message), mainActivity)
                }
                mainActivity.hideLoading()
            }
        }
    }

    private fun jsonToSend(loginModel: LoginModel): JsonObject {
        val user = JsonObject()
        user.addProperty("avatarURL", loginModel.avatarURL)
        user.addProperty("displayName", loginModel.displayName)
        user.addProperty("email", loginModel.email)
        user.addProperty("password", loginModel.password)
        return user
    }

    private fun getCategories() {

    }

    fun openTermsAndPrivacyView() {

    }
}