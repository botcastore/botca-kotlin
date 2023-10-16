package com.kevinhomorales.botcakotlin.login.services.request

import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.login.services.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface LoginRequest {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @POST
    suspend fun postLogin(@Url endpoint: String, @Body body: JsonObject): LoginResponse
}