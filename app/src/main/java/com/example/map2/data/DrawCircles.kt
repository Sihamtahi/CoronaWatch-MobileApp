package com.example.map2.data


import android.graphics.Color
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.CircleOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DrawCircles {

    private var recyclerView: RecyclerView? = null
    private var progress_bar: ProgressBar? = null

    var myListCoord: List<Locations_> ?= null
    fun runCircles(): List<Locations_>? {
        val request = ServiceBuilder.buildService(TmdbEndpoints::class.java)
        val call = request.getCorrdinates()
        println("ouii ")
        val response = call.execute()
        val myListCoord = response.body()!!.locations


            //  println("je suis dans ui thread "+d)



       /* call.enqueue(object : Callback<MyList> {
            override fun onResponse(call: Call<MyList>, response: Response<MyList>) {
                if (response.isSuccessful){
                 //   Toast.makeText(this@DrawCircles, "Succès", Toast.LENGTH_LONG).show()
                    println("ouiiiiiiiiii")
                    myListCoord = response.body()!!.locations
                    println("res est:"+response.body())
                   // progress_bar?.visibility = View.GONE
                   /* recyclerView.apply {
                        this!!.setHasFixedSize(true)
                        recyclerView!!.layoutManager = LinearLayoutManager(this@MainActivity)
                        var adapter = ToDoList(response.body()!!)
                        recyclerView?.adapter = adapter
                    }*/
                }
            }
            override fun onFailure(call: Call<MyList>, t: Throwable) {
                println("failure"+t.message)
                //Toast.makeText(this@DrawCircles, "${t.message}", Toast.LENGTH_SHORT).show()
            }
        })*/
        return myListCoord
    }
}