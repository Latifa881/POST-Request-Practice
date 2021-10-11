package com.example.postrequestpractice

import retrofit2.Call
import retrofit2.http.*

interface APIInterface {
    @Headers("Content-Type: application/json")
    @GET("/test/")
    fun getUserDetails():  Call<List<Details.UserDetails>>

    @Headers("Content-Type: application/json")
    @POST("/test/")
    fun addUserDetails(@Body details: Details.UserDetails): Call<List<Details.UserDetails>>

    @Headers("Content-Type: application/json")
    @PUT("/test/{id}")
    fun updateUserDetails(@Path("id")id:Int,@Body details: Details.UserDetails): Call<Details.UserDetails>

    @Headers("Content-Type: application/json")
    @DELETE("/test/{id}")
    fun deleteUserDetails(@Path("id")id:Int): Call<Void>


}