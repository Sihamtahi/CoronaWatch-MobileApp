package com.example.map2.data

import android.content.Context
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.IOException


data class MyList(
    val locations: List<Locations_>
)

data class coordinates(

    var latitude: String ,
    var longitude: String
)
data class latest(
    var confirmed: String ,
    var deaths: String,
    var recovered: String
)
data class Locations_(
    var id: Int ,
    var country: String ,
    var country_code: String ,
    var country_population: Int ,
    var province: String ,
    var last_updated: String ,
    var coordinates: coordinates,
    var latest: latest
)
data class Mymap(
    var latest: latest,
    var locations:Locations_
)
