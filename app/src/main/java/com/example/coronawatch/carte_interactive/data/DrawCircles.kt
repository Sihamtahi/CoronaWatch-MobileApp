package com.example.coronawatch


import android.widget.ProgressBar


class DrawCircles {

    private var progress_bar: ProgressBar? = null
    val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)


    fun runCircles(): List<Locations_>? {

        val call = request.getCorrdinates()
        val response = call.execute()
        val myListCoord = response.body()!!.locations




        return myListCoord
    }

    fun runLatestData(): Map<String, latest>? {

        val call2 = request.getLatestData()
        val response2 = call2.execute()
        val myLatestList = response2.body()!!
        return myLatestList
    }

    fun getLatestDataCountry(url:String): List<Locations_>? {

        var  myLatestList:List<Locations_>  ?= null

        val call = request.getlatestCountry(url)

        val response = call.execute()
         myLatestList = response.body()!!.locations


        return myLatestList
    }
}