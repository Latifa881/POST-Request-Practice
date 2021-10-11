package com.example.postrequestpractice

import com.google.gson.annotations.SerializedName


class Details{

     var data: List<UserDetails>? = null

     class UserDetails {
         @SerializedName("pk")
         var id: Int? = null


         @SerializedName("name")
         var name: String? = null

         @SerializedName("location")
         var location: String? = null

         constructor( location: String?,name: String?,id:Int?) {
             this.name = name
             this.location = location
             this.id=id
         }
         constructor( location: String?,name: String?) {
             this.name = name
             this.location = location
         }
     }
 }