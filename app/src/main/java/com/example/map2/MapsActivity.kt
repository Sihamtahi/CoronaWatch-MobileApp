package com.example.map2
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import com.google.android.gms.maps.model.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_buttumnav.*
import android.content.res.Resources.NotFoundException
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.UiThread
import androidx.appcompat.app.AlertDialog
import com.example.map2.MapsActivity
import com.example.map2.R
import com.example.map2.R.id.malade_map_bottom
//import com.example.map2.data.Location
//import com.example.map2.data.Request
import com.example.map2.data.*
import com.google.android.gms.common.api.Response
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_buttumnav.*
import kotlinx.android.synthetic.main.botton_sheet_view.*
import kotlinx.android.synthetic.main.layout_dialog_info.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.w3c.dom.Text
import java.net.URL
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var malade: TextView? = null
    var confirme: TextView? = null
    var retablis: TextView? = null

    var malade_cont: TextView? = null
    var confirme_cont: TextView? = null
    var retablis_cont: TextView? = null


    //OkHttpClient creates connection pool between client and server
    val client = OkHttpClient()
    //  var malade: TextView? = null



    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // run("https://coronavirus-tracker-api.herokuapp.com/v2/locations?country_code=IT")
        // partie test fichier json
        malade = findViewById(R.id.malade)
        confirme = findViewById(R.id.confirmé)
        retablis = findViewById(R.id.suspect)
        // val url = "https://coronavirus-tracker-api.herokuapp.com/v2/locations?country_code=IT"

        val client = OkHttpClient()

        doAsync {
            //  Request("https://coronavirus-tracker-api.herokuapp.com/all").run()
            // var arrayList_details:ArrayList<Coordinates> = ArrayList()

        }
////////////////////////

        //  Thread.sleep(15000)


        /** la partie ajoutée concernant la map**/
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        /** la partie pour le buttom sheet dialog
         * Configuration des deux buttons pour que le dialog apparait**/
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)
        bottomSheetDialog.setContentView(view)
        register.setOnClickListener { bottomSheetDialog.show() }
        swip_up.setOnClickListener { bottomSheetDialog.show() }

    }
    fun run( url:String) {
        val request = okhttp3.Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Sonthing ","On faiture")
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {

                var str_response = response.body()!!.string()//   body!!.string()

                val json_contact: JSONObject = JSONObject(str_response)

                var res: JSONObject = json_contact.getJSONObject("latest")
                Thread.sleep(1000);
                var conf:Int = res.getInt("confirmed")
                println("cas confirmés: " +conf.toString())
                malade!!.setText(conf.toString())
                retablis!!.setText(res.getInt("deaths").toString())
                confirme!!.setText(res.getInt("recovered").toString())
            }
            //    }
        })
    }
    /**la partie ajoutée qui concerne la map @ovverride de la deuxème fonctions onMapReady**/
    /**SetMapStyle : cette une fonction qui applique le style aubergine qui se trouve dans le dossier row (stylemap.json)**/
    //private val TAG = MapsActivity::class.java.simpleName
    private fun setMapStyle(map: GoogleMap)
    {
        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this,
                    R.raw.style_map
                )
            )

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }
    private val TAG = MapsActivity::class.java.simpleName


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap)
    {

        mMap = googleMap
        setMapStyle(mMap)
        /**********lisner pour swicher entre les maps**/
         val btnSwitch = findViewById<Button>(R.id.switch_to_algeria_map) //findViewById(R.id.switch_to_algeria_map)
      if(btnSwitch != null)
      {
          btnSwitch.setOnClickListener
          {
              val intent = Intent(btnSwitch.context, SwitchWToAlg::class.java)
              btnSwitch.context.startActivity(intent)
          }
      }

        /**Confuguer le zoom pour la map **/
        zoomin.setOnClickListener {
            zoomIn(mMap)
        }
        /**gestion des buttons du Bottom sheet **/
/*
         var malade_map:ToggleButton= findViewById(R.id.malade_map_bottom)
         var suspect_map:Button= findViewById(R.id.suspet_map_bottom)
         var porteur_map :Button=findViewById(R.id.porteur_map_bottom) */
        /**On Ajoute le OnClick Listner pour pouvoir afficher **/


        googleMap.setOnMapLongClickListener {
                latlng ->

            val count_code:String = getCountryCode(latlng)
            Log.i("country code est : ", count_code)
            val nameCountry = getCountryName(latlng)
            showAlertDialog(latlng,count_code,nameCountry)

        }
        // val confirmed_filter:TextView = findViewById(R.id.malade_map_bottom)

        fun OnDefaultToggleClick (view:View)
        {

        }
        fun onCostumToggleClick(view:View)
        {

            Toast.makeText(this,"CostumToggle",Toast.LENGTH_SHORT).show()
        }
        // RunUrl()
        run("https://coronavirus-tracker-api.herokuapp.com/v2/latest")
        doAsync {
            var arrayList_details = RunUrl("https://coronavirus-tracker-api.herokuapp.com/v2/locations",googleMap)
        }
        /*var cityCircle = googleMap.addCircle(
            CircleOptions()
                .center(country)
                .radius(90000.0)
                .strokeWidth(3f)
                .strokeColor(Color.YELLOW)
                .fillColor(Color.argb(70,0,150,150)))
        google.maps.event.addListener(cityCircle, "click", function(ev) {
            infoWindow.setPosition(ev.latLng);
            infoWindow.open(map);
        });*/

        mMap.uiSettings.isZoomControlsEnabled =true
        mMap.isBuildingsEnabled=true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isCompassEnabled = true
    }

    private fun showAlertDialog(latlng: LatLng?,count_code:String,count_name:String)
    {

        val placeForInformation = LayoutInflater.from(this).inflate(R.layout.layout_title_dialog,null)
        val dial = AlertDialog.Builder(this)
        val cancel_btn:Button = placeForInformation.findViewById(R.id.cancel_dialog)


        ////////////////////////
        var nbrMal  : Int = 0
        var nbrSusp : Int = 0
        var nbrPort : Int = 0
        val url = "https://coronavirus-tracker-api.herokuapp.com/v2/locations?country_code="+count_code
        val request = okhttp3.Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Sonthing ","aqli deg on faiture")
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                Log.d("Something ","aqli deg on Response")

                var str_response = response.body()!!.string()

                val json_contact: JSONObject = JSONObject(str_response)

                var res: JSONObject = json_contact.getJSONObject("latest")

                nbrMal =  res.getInt("confirmed")
                nbrSusp = res.getInt("deaths")
                nbrPort = res.getInt("recovered")

                println("conffffffffff:"+res.getInt("confirmed"))
                println("deathhhhhhhhh:"+res.getInt("deaths"))
            }
        })
        Thread.sleep(1000)

        /* doAsync {
             uiThread {
                // Thread.sleep(15000);
                 println("ouiiiiiiiiii")
             }
          } */

        ///////////////////////////

        dial.setView(placeForInformation)
        var d = dial.show()
        d.window.setBackgroundDrawableResource(R.drawable.dialog_backgroun_region_info)
        //malade_cont = placeForInformation.findViewById<TextView>(R.id.dialog_num0)

        placeForInformation.findViewById<TextView>(R.id.dialog_num0).text=nbrMal.toString()
        placeForInformation.findViewById<TextView>(R.id.dialog_num1).text=nbrSusp.toString()
        placeForInformation.findViewById<TextView>(R.id.dialog_num2).text=nbrPort.toString()
        placeForInformation.findViewById<TextView>(R.id.country).text=count_name.toString()

        cancel_btn.setOnClickListener{
            d.dismiss()
        }
        mMap.addMarker(MarkerOptions().position(latlng!!).title(count_name))     }

    fun zoomIn(myMap: GoogleMap)
    {
        myMap.animateCamera(CameraUpdateFactory.zoomIn())
    }



    fun getCountryCode(latlng: LatLng):String {

        val loc = Locale("ar")

        var country_code: String? = null
        val gcd = Geocoder(this, Locale.getDefault())
        //Thread.sleep(1000);
        var addresses: MutableList<Address>? = gcd.getFromLocation(latlng.latitude, latlng.longitude, 1)
        //To get country name
        /*  if (addresses!!.get(0).countryName != null) {
              val country = addresses.get(0).countryName
              Log.d("COUNTRY", country)
          }*/

        //to get country code
        if (addresses!!.get(0).countryCode != null) {
            country_code = addresses.get(0).countryCode
            Log.d("COUNTRY CODE", country_code)

        }

        return country_code!!

    }
    fun getCountryName(latlng: LatLng): String {

        var country_cname: String? = null
        val loc = Locale("ar")
        // val geocoder = Geocoder(this, loc)
        val gcd = Geocoder(this, loc)

        var addresses: MutableList<Address>? = gcd.getFromLocation(latlng.latitude, latlng.longitude, 1)
        //To get country name
        if (addresses!!.get(0).countryName != null) {
            country_cname = addresses.get(0).countryName
            Log.d("COUNTRY", country_cname)
        }
        return country_cname!!
    }

    fun RunUrl(url:String , googleMap: GoogleMap): ArrayList<Coordinates> {
        var list_coord:ArrayList<Coordinates> = ArrayList()
        // add circles
        var coord = Coordinates ()
        val request = okhttp3.Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("Sonthing ","aqli deg on faiture")
            }

            override fun onResponse(call: Call, response: okhttp3.Response) {
                Log.d("Something ","aqli deg on Response")

                var str_response = response.body()!!.string()
                val json_contact: JSONObject = JSONObject(str_response)
                //creating json array
                var jsonarray_info: JSONArray = json_contact.getJSONArray("locations")
                var i: Int = 0
                var size: Int = jsonarray_info.length()

                var country:LatLng
                var d:Int=0
                for (i in 0..size - 1) {
                    var json_objectdetail: JSONObject = jsonarray_info.getJSONObject(i)

                    var lat: JSONObject = json_objectdetail.getJSONObject("coordinates")

                    coord.latitude = lat.getString("latitude").toDouble()
                    coord.longitude = lat.getString("longitude").toDouble()
                    list_coord.add(coord)

                    country = LatLng(lat.getString("latitude").toDouble(),lat.getString("longitude").toDouble())
                    Thread.sleep(10)
                    runOnUiThread {
                        d = d+ 1
                        //  println("je suis dans ui thread "+d)
                        googleMap.addCircle(
                            CircleOptions()
                                .center(country)
                                .radius(90000.0)
                                .strokeWidth(3f)
                                .strokeColor(Color.YELLOW)
                                .fillColor(Color.argb(70,0,150,150)))
                    }
                }
            }
            // affichage des cercles
        })

        return list_coord
    }

}
