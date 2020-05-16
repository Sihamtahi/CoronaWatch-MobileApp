package com.example.map2.data


import android.view.LayoutInflater
import android.widget.Button
import android.widget.ProgressBar
import com.example.map2.MapsActivity
import com.example.map2.R
import kotlinx.android.synthetic.main.activity_main.*


class DrawCircles {


    private var progress_bar: ProgressBar? = null

    val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)


    fun runCircles(): List<Locations_>? {

        val call = request.getCorrdinates()
        println("ouii ")
        val response = call.execute()
        val myListCoord = response.body()!!.locations




        return myListCoord
    }
    fun runLatestData(): Map<String, latest>? {
      println("je suis ici 1")
        val call2 = request.getLatestData()

        println("noooooo 1")
        val response2 = call2.execute()
        val myLatestList = response2.body()!!
        return myLatestList
    }
    fun getLatestDataCountry(url:String): List<Locations_>? {
        println("je suis ici 2")
        var  myLatestList:List<Locations_>  ?= null

        val call = request.getlatestCountry(url)

        val response = call.execute()
         myLatestList = response.body()!!.locations
        println("liste est !"+response.body()!!)
        /* call.enqueue(object : Callback<MyList> {
        override fun onResponse(call: Call<MyList>, response: Response<MyList>) {
            if (response.isSuccessful){
                 myLatestList = response.body()!!.locations
            }
        }
        override fun onFailure(call: Call<MyList>, t: Throwable) {
            println("failure"+t.message)
        }
    })*/

        return myLatestList
    }
}