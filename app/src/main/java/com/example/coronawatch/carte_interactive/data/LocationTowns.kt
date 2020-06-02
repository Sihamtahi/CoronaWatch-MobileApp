package com.example.coronawatch

import com.google.gson.annotations.SerializedName

data class LocationTown(
    @SerializedName("id")
    val id : Int ,
    @SerializedName("longitude")
    val longitude : Float,
    @SerializedName("latitude")
    val latitude : Float,
    @SerializedName("reception_center")
    val reception_center  :String

)



