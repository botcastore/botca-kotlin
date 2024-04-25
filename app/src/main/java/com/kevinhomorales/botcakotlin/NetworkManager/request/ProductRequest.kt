package com.kevinhomorales.botcakotlin.NetworkManager.request

import com.kevinhomorales.botcakotlin.NetworkManager.response.ProductResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Url

interface ProductRequest {
    @Headers("Content-Type: application/json", "Accept: application/json", "Origin: botca-kotlin-mobile")
    @GET
    suspend fun getProduct(@Url endpoint: String, @Header("access-token") token: String): Response<ProductResponse>
}