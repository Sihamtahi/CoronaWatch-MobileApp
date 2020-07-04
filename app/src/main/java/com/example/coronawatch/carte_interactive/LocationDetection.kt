package com.example.map2


import android.content.BroadcastReceiver
import android.content.Intent
import android.util.Log
import android.content.Context
import android.location.Geocoder
import androidx.core.content.ContextCompat.startForegroundService
import com.example.coronawatch.Town
import com.example.coronawatch.buttumnav
import com.google.android.gms.location.LocationResult
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.security.auth.callback.Callback

class MyService : BroadcastReceiver() {
    companion object {
        val ACTION_PROCESS_UPDATE= "com.example.map2.UPDATE_LOCATION"
    }
    var currentTown : String = ""
    val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)//connect time
        .writeTimeout(10, TimeUnit.SECONDS)//write time
        .readTimeout(30, TimeUnit.SECONDS)//socket read time
        .build()
    //information sur l'api API
    val API_LINK_TOWNS ="https://corona-watch-api.herokuapp.com/corona-watch-api/v1/geolocation/towns/"
    val API_HEADER_KEY="Authorization"
    val API_HEADRER_VALUE="token ee5f6766123e0fa438f03380f300a8f74f081c9f"
    override fun onReceive(context: Context?, intent: Intent?)
    {
         if (intent!= null)
         {
             val action = intent.action
             if (action.equals(ACTION_PROCESS_UPDATE))
             {
                 val result = LocationResult.extractResult(intent)
                 if (result != null)
                 {
                     val location = result.lastLocation
                     var town: String =" "
                     val gcd = Geocoder(buttumnav.instancee)
                     var addresses: MutableList<android.location.Address>? = gcd.getFromLocation(location.latitude, location.longitude, 1)

                     //to notify here
                     if (addresses!!.get(0).locality != null)
                     {
                         town = addresses.get(0).locality
                     }
                   if (!currentTown.equals(town))
                    {
                            currentTown=town
                            checkDangerZone(currentTown, buttumnav.instancee!!)

                    }


                 }
             }
         }

    }

    private fun checkDangerZone(current: String,contextNotif:Context)  {
        var towns: List<Town> =ArrayList<Town>()
        val request = Request.Builder()
            .url(API_LINK_TOWNS)
            .header(API_HEADER_KEY, API_HEADRER_VALUE)
            .build()

        client.newCall(request).enqueue(object : Callback, okhttp3.Callback {


            override fun onFailure(call: Call, e: IOException ) {

                Log.d("Sonthing ", "aqli deg on failure n afficher cercle ")

            }

            override fun onResponse(call: Call, response: Response)
            {

                Log.d("Sonthing ", "aqli deg on response n afficher cercle")
                        var str_response = "ll"
                        str_response = response.body!!.string()

                        val gson = Gson()
                        val listTownType = object : TypeToken<List<Town>>() {}.type
                        towns = gson.fromJson(str_response, listTownType)
                        towns.forEachIndexed { idx,
                                               town ->
                            if (town.number_death > 0 || town.number_suspect > 0 || town.number_carrier > 0 || town.number_recovered > 0 || town.number_sick > 0) {

                                if (town.name.equals(current))
                                {
                                    val intent = Intent(contextNotif,NotifService::class.java)
                                    startForegroundService(contextNotif, intent)
                                }
                            }
                        }
                }
            }

        )


    }



}

