package com.example.coronawatch

import com.google.gson.annotations.SerializedName

data class Town(
    @SerializedName("id")
    val id : Int,
    @SerializedName("location")
    val location : LocationTown,
    @SerializedName("name")
    val name : String ,
    @SerializedName("postal_code")
    val code_postal :String,
    @SerializedName("number_death")
    val number_death : Int,
    @SerializedName("number_suspect")
    val number_suspect :Int ,
    @SerializedName("number_sick")
    val  number_sick : Int ,
    @SerializedName("number_confirmed_cases")
    val number_confirmed_cases : Int,
    @SerializedName("number_carrier")
    val number_carrier : Int ,
    @SerializedName("number_recovered")
    val number_recovered : Int ,
    @SerializedName("last_update")
    val last_update : String,
    @SerializedName("is_risked")
    val is_risked : Boolean ,
    @SerializedName("is_validated")
    val is_validated : Boolean,
    @SerializedName("state")
    val state : Int
)
