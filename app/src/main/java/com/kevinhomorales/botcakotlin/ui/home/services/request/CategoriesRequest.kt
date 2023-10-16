package com.kevinhomorales.botcakotlin.ui.home.services.request

import com.kevinhomorales.botcakotlin.ui.home.services.response.CategoriesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Url

interface CategoriesRequest {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET
    suspend fun getCategories(@Url endpoint: String): Response<CategoriesResponse>
}