package com.example.map2

import android.Manifest
import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.IBinder
import android.util.Log
import android.content.Context
import android.content.Context.LOCATION_SERVICE
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startForegroundService
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_buttumnav.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.lang.Exception
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
    val API_HEADRER_VALUE="Basic YWRtaW46YWRtaW4="
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
                            checkDangerZone(currentTown, DangerZone.instance!!)

                    }
                       Log.d("Sonthing","----> the new town is :"+currentTown)

                 }
             }
         }

    }

    private fun checkDangerZone(current: String,contextNotif:Context)  {
        var towns: List<Town> =ArrayList<Town>()
        Log.d("Sonthing ","aqli des affichier cercles")
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
                        str_response = response.body()!!.string()
                        //Log.d("Sonthing ", " La réponse est : " + str_response)
                        val gson = Gson()
                        val listTownType = object : TypeToken<List<Town>>() {}.type
                        towns = gson.fromJson(str_response, listTownType)
                        towns.forEachIndexed { idx,
                                               town ->
                            if (town.number_death > 0 || town.number_suspect > 0 || town.number_carrier > 0 || town.number_recovered > 0 || town.number_sick > 0) {
                                Log.d(
                                    "Sonthing ",
                                    "--> " + town.name + " " + town.number_carrier.toString() + " " + town.number_death.toString() + " " + town.number_recovered.toString() + " " + town.number_confirmed_cases + " " + town.number_suspect
                                )
                                if (town.name.equals(current))
                                {
                                    val intent = Intent(contextNotif,NotifService::class.java)
                                    startForegroundService(contextNotif, intent)
                                    Log.d("Sonthing","j'ai trouvé une commune avec le nom de votre currentTown")
                                }
                            }
                        }
                }
            }

        )


    }



}

