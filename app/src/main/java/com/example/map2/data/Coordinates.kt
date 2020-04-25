package com.example.map2.data

import android.content.Context
import com.google.gson.annotations.SerializedName
import java.io.IOException

public class Coordinates{

    var latitude: Double = 0.0
    var longitude: Double = 0.0


    constructor(latitude:Double,longitude:Double) {

        this.latitude = latitude
        this.longitude = longitude
    }

    constructor()
}