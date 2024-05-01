package com.kevinhomorales.botcakotlin.customer.register.viewmodel

import androidx.lifecycle.ViewModel
import com.kevinhomorales.botcakotlin.NetworkManager.response.VerifyMemberResponse
import com.kevinhomorales.botcakotlin.customer.register.view.RegisterActivity
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.model.CompleteProfileModel
import com.kevinhomorales.botcakotlin.NetworkManager.request.CategoriesRequest
import com.kevinhomorales.botcakotlin.NetworkManager.request.CompleteProfileRequest
import com.kevinhomorales.botcakotlin.NetworkManager.response.LoginResponse
import com.kevinhomorales.botcakotlin.R
import com.kevinhomorales.botcakotlin.customer.register.model.CountryCode
import com.kevinhomorales.botcakotlin.customer.register.model.CountryCodes
import com.kevinhomorales.botcakotlin.main.MainActivity
import com.kevinhomorales.botcakotlin.utils.Alerts
import com.kevinhomorales.botcakotlin.utils.Constants
import com.kevinhomorales.botcakotlin.utils.UserManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class RegisterViewModel: ViewModel() {
    lateinit var verifyMemberResponse: VerifyMemberResponse
    lateinit var view: RegisterActivity


    fun getCountryCodes(mainActivity: MainActivity): CountryCodes {
        val json: String? = try {
            // Lee el archivo assets/data.json
            val inputStream = mainActivity.assets.open("countrycodes.json")
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()

            // Convierte el buffer en una cadena
            String(buffer, Charset.forName("UTF-8"))
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            null
        }
        var countryCodesArray = mutableListOf<CountryCode>()
        json?.let {
            try {
                val jsonArray = JSONArray(json)
                // Iterar sobre cada objeto en el array
                for (i in 0 until jsonArray.length()) {
                    val countryObject: JSONObject = jsonArray.getJSONObject(i)
                    val name = countryObject.getString("name")
                    val flag = countryObject.getString("flag")
                    val code = countryObject.getString("code")
                    val dialCode = countryObject.getString("dial_code")
                    val countryCode = CountryCode(name, flag, code, dialCode)
                    countryCodesArray.add(countryCode)
                    println("Name: $name, Flag: $flag, Code: $code, Dial Code: $dialCode")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val countryCodes = CountryCodes(countryCodesArray)
        return  countryCodes
    }

    fun completeProfile(mainActivity: MainActivity, phoneNumber: String) {
        mainActivity.showLoading(mainActivity.getString(R.string.loading_login))
        val avatarURLString = verifyMemberResponse.avatarURL!!
        val displayName = verifyMemberResponse.displayName!!
        val email = verifyMemberResponse.email
        val provider = verifyMemberResponse.provider!!
        val completeProfileModel = CompleteProfileModel(avatarURLString, verifyMemberResponse.password!!, displayName, email, verifyMemberResponse.password!!, phoneNumber, Constants.clearString, Constants.clearString, provider)
        CoroutineScope(Dispatchers.IO).launch {
            val call = mainActivity.getRetrofit().create(CompleteProfileRequest::class.java).completeProfile("users/complete-profile", jsonLogin(completeProfileModel))
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

    private fun jsonLogin(completeProfileModel: CompleteProfileModel): JsonObject {
        val user = JsonObject()
        user.addProperty("avatarURL", completeProfileModel.avatarURL)
        user.addProperty("idUser", completeProfileModel.idUser)
        user.addProperty("displayName", completeProfileModel.displayName)
        user.addProperty("email", completeProfileModel.email)
        user.addProperty("password", completeProfileModel.password)
        user.addProperty("phoneNumber", completeProfileModel.phoneNumber)
        user.addProperty("provider", completeProfileModel.provider)
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