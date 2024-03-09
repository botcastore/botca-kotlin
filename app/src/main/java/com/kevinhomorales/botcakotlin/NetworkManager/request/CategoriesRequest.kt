package com.kevinhomorales.botcakotlin.NetworkManager.request

import com.kevinhomorales.botcakotlin.NetworkManager.response.CategoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface CategoriesRequest {
    @Headers("Content-Type: application/json", "Accept: application/json", "Origin: botca-kotlin-mobile")
    @GET
    suspend fun getCategories(@Url endpoint: String): Response<CategoriesResponse>
}
