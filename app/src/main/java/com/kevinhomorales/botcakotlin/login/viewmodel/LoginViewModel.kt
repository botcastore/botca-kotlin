package com.kevinhomorales.botcakotlin.login.viewmodel

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.login.services.model.LoginModel
import com.kevinhomorales.botcakotlin.login.services.request.LoginRequest
import com.kevinhomorales.botcakotlin.login.services.response.LoginResponse
import com.kevinhomorales.botcakotlin.login.view.LoginActivity
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.ui.home.services.request.CategoriesRequest
import com.kevinhomorales.botcakotlin.ui.home.services.response.CategoriesResponse
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.GooglePictureQuality
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.create

class LoginViewModel: ViewModel() {

    lateinit var view: LoginActivity

    fun checkIsLogged(mainActivity: MainActivity) {
        val loginResponse = UserManager.shared.getUser(mainActivity)
        if (!(loginResponse.me.token.isEmpty())) {
            view.openHome(null)
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

    fun postLogin(account: GoogleSignInAccount, result: Task<AuthResult>, mainActivity: MainActivity) {
        CoroutineScope(Dispatchers.IO).launch {
            val avatarURL = GooglePictureQuality.shared.setQuality(account.photoUrl.toString(), "200")
            val displayName = account.givenName.toString() + " " + account.familyName.toString()
            val email = account.email.toString()
            val uid = result.result.user?.uid!!
            val loginModel = LoginModel(avatarURL, displayName, email, uid)
            val call = mainActivity.getRetrofit().create(LoginRequest::class.java).postLogin("users/login", jsonToSend(loginModel))
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

    private fun jsonToSend(loginModel: LoginModel): JsonObject {
        val user = JsonObject()
//        user.addProperty("avatarURL", loginModel.avatarURL)
//        user.addProperty("displayName", loginModel.displayName)
//        user.addProperty("email", loginModel.email)
//        user.addProperty("password", loginModel.password)

        user.addProperty("email", "goyeselcoca@gmail.com")
        user.addProperty("password", "123456789")
        return user
    }

    private fun getCategories(loginResponse: LoginResponse, mainActivity: MainActivity) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CategoriesRequest::class.java).getCategories("categorys?page=1&dataByPage=20")
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

    fun openTermsAndPrivacyView() {

    }
}