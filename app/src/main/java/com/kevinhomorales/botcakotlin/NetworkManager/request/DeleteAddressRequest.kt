package com.kevinhomorales.botcakotlin.NetworkManager.request

import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.response.DeleteAddressResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url

interface DeleteAddressRequest {
    @Headers("Content-Type: application/json", "Accept: application/json", "Origin: botca-kotlin-mobile")
    @HTTP(method = "DELETE", hasBody = true)
    suspend fun delete(@Url endpoint: String, @Header("access-token") token: String, @Body body: JsonObject): Response<DeleteAddressResponse>
}