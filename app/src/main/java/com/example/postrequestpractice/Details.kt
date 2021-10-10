package com.example.postrequestpractice

import com.google.gson.annotations.SerializedName


class Details{

     var data: List<UserDetails>? = null

     class UserDetails {

         @SerializedName("name")
         var name: String? = null

         @SerializedName("location")
         var location: String? = null

         constructor( location: String?,name: String?) {
             this.name = name
             this.location = location
         }
     }
 }