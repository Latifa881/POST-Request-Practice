package com.example.postrequestpractice

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIInterface {
    @Headers("Content-Type: application/json")
    @GET("/test/")
    fun getDetails(): Call<List<Details.UserDetails>>

    @Headers("Content-Type: application/json")
    @POST("/test/")
    fun addDetails(@Body details: Details.UserDetails): Call<List<Details.UserDetails>>


}