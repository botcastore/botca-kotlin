package com.kevinhomorales.botcakotlin.NetworkManager.request

import com.google.gson.JsonObject
import com.kevinhomorales.botcakotlin.NetworkManager.response.CouponApplyResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url

interface CouponApplyRequest {
    @Headers("Content-Type: application/json", "Accept: application/json", "Origin: botca-kotlin-mobile")
    @POST
    suspend fun upload(@Url endpoint: String, @Header("access-token") token: String, @Body body: JsonObject): Response<CouponApplyResponse>
}