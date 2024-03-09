package com.kevinhomorales.botcakotlin.customer.login.viewmodel

import android.accounts.Account
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.model.LoginModel
import com.kevinhomorales.botcakotlin.NetworkManager.model.VerifyMemberModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.CategoriesRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.LoginRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.VerifyMemberRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.LoginResponse
import com.kevinhomorales.botcakotlin.NetworkManager.response.Me
import com.kevinhomorales.botcakotlin.NetworkManager.response.UserResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.login.view.LoginActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GUEST_TOKEN
import com.kevinhomorales.botcakotlin.utils.GooglePictureQuality
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject


class LoginViewModel: ViewModel() {

    lateinit var view: LoginActivity

    fun checkIsLogged(mainActivity: MainActivity) {
        val loginResponse = UserManager.shared.getUser(mainActivity)
        if (!(loginResponse.me.token!!.isEmpty())) {
            view.openHome(null)
        }
        if (loginResponse.me.token == GUEST_TOKEN) {
            return
        }
    }

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

    fun guestSignIn(activity: MainActivity) {
        activity.showLoading(activity.getString(R.string.loading_login))
        FirebaseAuth.getInstance().signInAnonymously().addOnCompleteListener { result ->
            if (result.isSuccessful) {
                val account = GoogleSignInAccount.createDefault()
                verifyMember(account, result, activity)
            } else {
                Alerts.warning(activity.getString(R.string.error_title), activity.getString(R.string.error_message),activity)
                activity.hideLoading()
            }
        }
    }

    fun verifyMember(account: GoogleSignInAccount, result: Task<AuthResult>, mainActivity: MainActivity) {
        CoroutineScope(Dispatchers.IO).launch {
            if (account.email == "<<default account>>") {
                val loginResponse = LoginResponse(Me(UserResponse(Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, false, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString, Constants.clearString), GUEST_TOKEN, Constants.clearString))
                UserManager.shared.saveUser(loginResponse, mainActivity)
                getCategories(loginResponse, mainActivity)
                return@launch
            }
            val avatarURL = GooglePictureQuality.shared.setQuality(account.photoUrl.toString(), "200")
            val displayName = account.givenName.toString() + " " + account.familyName.toString()
            val email = account.email.toString()
            val password = result.result.user?.uid!!
            val verifyMemberModel = VerifyMemberModel(avatarURL, displayName, email, password, "GOOGLE")
            val call = mainActivity.getRetrofit().create(VerifyMemberRequest::class.java).postVerifyMember("users/verifyMember", jsonLoginVerifyMember(verifyMemberModel))
            var response = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    if (response!!.isMember) {
                        postLogin(account,result, mainActivity)
                        return@runOnUiThread
                    }
                    response.password = result.result.user!!.uid
                    view.openRegister(response!!)
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

    private fun jsonLoginVerifyMember(verifyMemberModel: VerifyMemberModel): JsonObject {
        val user = JsonObject()
        user.addProperty("avatarURL", verifyMemberModel.avatarURL)
        user.addProperty("displayName", verifyMemberModel.displayName)
        user.addProperty("email", verifyMemberModel.email)
        user.addProperty("provider", verifyMemberModel.provider)
        return user
    }

    private fun postLogin(account: GoogleSignInAccount, result: Task<AuthResult>, mainActivity: MainActivity) {
        CoroutineScope(Dispatchers.IO).launch {
            val avatarURL = GooglePictureQuality.shared.setQuality(account.photoUrl.toString(), "200")
            val displayName = account.givenName.toString() + " " + account.familyName.toString()
            val email = account.email.toString()
            val uid = result.result.user?.uid!!
            val loginModel = LoginModel(avatarURL, displayName, email, uid)
            val call = mainActivity.getRetrofit().create(LoginRequest::class.java).postLogin("users/login", jsonLogin(loginModel))
            val loginResponse = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    getCategories(loginResponse!!, mainActivity)
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

    private fun jsonLogin(loginModel: LoginModel): JsonObject {
        val user = JsonObject()
        user.addProperty("avatarURL", loginModel.avatarURL)
        user.addProperty("displayName", loginModel.displayName)
        user.addProperty("email", loginModel.email)
        user.addProperty("password", loginModel.password)
        return user
    }

    private fun getCategories(loginResponse: LoginResponse, mainActivity: MainActivity, includeProducts: String = "&includeProducts=true") {
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CategoriesRequest::class.java).getCategories("categorys?page=1&dataByPage=20" + includeProducts)
            val categoriesResponse = call.body()
            mainActivity.runOnUiThread {
                if(call.isSuccessful) {
                    UserManager.shared.saveUser(loginResponse, mainActivity)
                    view.openHome(categoriesResponse)
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
}