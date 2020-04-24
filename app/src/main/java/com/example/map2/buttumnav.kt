package com.example.map2
import android.content.res.Resources
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlin.concurrent.thread
import com.example.map2.R.id.malade_map_bottom
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_buttumnav.*
import kotlinx.android.synthetic.main.botton_sheet_view.*
import kotlinx.android.synthetic.main.layout_dialog_info.*
import javax.security.auth.callback.Callback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import android.widget.TextView
import com.google.android.gms.tasks.Tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.*
private  const val TAG="buttumnav"
//OkHttpClient creates connection pool between client and server
val client = OkHttpClient()

class buttumnav : AppCompatActivity() ,OnMapReadyCallback {
    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buttumnav)

     /** la prtie ajoutée concernant la map**/
     val mapFragment = supportFragmentManager
         .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

     /** la partie pour le buttom sheet dialog
      * Configuration des deux buttons pour que le dialog apparait**/
        val bottomSheetDialog = BottomSheetDialog(this)
        val view=layoutInflater.inflate(R.layout.botton_sheet_view,null)
        bottomSheetDialog.setContentView(view)
        register.setOnClickListener { bottomSheetDialog.show() }
        swip_up.setOnClickListener { bottomSheetDialog.show() }


    }

    /**la partie ajoutée qui concerne la map @ovverride de la deuxème fonctions onMapReady**/
    /**SetMapStyle : cette une fonction qui applique le style aubergine qui se trouve dans le dossier row (stylemap.json)**/
    private val TAG = MapsActivity::class.java.simpleName
    private fun setMapStyle(map: GoogleMap) {
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
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }
    override fun onMapReady(googleMap: GoogleMap)
    {

        var Mal  : Int = 0
        var Susp : Int = 0
        var Port : Int = 0
        var Guer : Int = 0
        var Mort : Int = 0
        /**Afficher les cercles des zones affectés sur la map **/
        var listTownsInfo = afficherCercles(googleMap)
       // Thread.sleep(15000)
        if (listTownsInfo.isEmpty())
        {
            Log.d("Sonthing ","La liste retournée est vide !!!!!!!!")
        }
        else
        {
            Log.d("Sonthing ","La liste retournée n'est pas  vide ???")
            Log.d("Sonthing ","La valeur de la première valeu est "+listTownsInfo[1].name)
            var center:LatLng
            listTownsInfo.forEachIndexed {
                    idx,
                    town ->
                    Log.d("Sonthing ","--> "+town.name+" "+town.number_carrier.toString()+" "+town.number_death.toString()+" "+town.number_recovered.toString() +" "+town.number_confirmed_cases + " "+town.number_suspect)
                    center = LatLng(town.location.latitude.toDouble(), town.location.longitude.toDouble())
                    googleMap.addCircle(
                        CircleOptions()
                            .center(center)
                            .radius( 500.0 * town.number_confirmed_cases + 500.0*town.number_death - 1000.0 * town.number_recovered)
                            .strokeWidth(3f)
                            .strokeColor(Color.YELLOW)
                            .clickable(true)
                            .fillColor(Color.argb(70, 0, 150, 150))
                    )
                Mal = Mal +town.number_confirmed_cases
                Susp =Susp + town.number_suspect
                Port =Port +  town.number_carrier
                Guer = Guer +town.number_recovered
                Mort =Mort +  town.number_death
            }

        }
        /****Mettre le nombre  total de mort,porteurs, malades, guéris,suspets **/
       var   nbrCas : TextView = findViewById(R.id.malade1_map)
       var   nbrSus : TextView = findViewById(R.id.suspect1_map)
       var   nbrPort : TextView = findViewById(R.id.porteur1_map)
       var   nbrMrt : TextView = findViewById(R.id.mort1_map)
       var   nbrGuer : TextView = findViewById(R.id.Guer1_map)
        nbrCas.text = Mal.toString()
        nbrSus.text =Susp.toString()
        nbrPort.text =Port.toString()
        nbrGuer.text= Guer.toString()
        nbrMrt.text=Mort.toString()

        /***Appliquer le style à la map **/
        mMap = googleMap
        setMapStyle(mMap)

        /**Confuguer le zoom pour la map **/
        zoomin.setOnClickListener {
            zoomIn(mMap)
        }

         /**On Ajoute le OnLongClick Listner pour pouvoir afficher les différentes informations liées à une région à travers un LongClick sur la map **/


        mMap.setOnMapLongClickListener {
                latlng ->
           Log.i(TAG,"OnMapLongClickListener")
            Mal = 0
            Susp =0
            Port =0
            Guer = 0
            Mort =0
            listTownsInfo.forEachIndexed {
                    idx,
                    town ->
                 if (town.location.latitude - 1 <latlng.latitude && town.location.latitude + 1 > latlng.latitude && town.location.longitude - 1<latlng.longitude  && town.location.longitude + 1 > latlng.longitude)
                {
                    Mal = Mal +town.number_confirmed_cases
                    Susp =Susp + town.number_suspect
                    Port =Port +  town.number_carrier
                    Guer = Guer +town.number_recovered
                    Mort =Mort +  town.number_death

                }
            }
            /********** Recuperer la wilaya et la commune ensuite faire un appel à showAlertDialog por afficher le Dialog d'informations ***/
            var wilaya = getWilaya(latlng)
            var commune = getCommune(latlng)
            showAlertDialog(latlng,Mal,Susp,Port,Guer,Mort,wilaya,commune)

        }
       fun OnDefaultToggleClick (view:View)
        {

        }
        fun onCostumToggleClick(view:View)
        {

            Toast.makeText(this,"CostumToggle",Toast.LENGTH_SHORT).show()
        }


        mMap.uiSettings.isZoomControlsEnabled =true
    }
    /****************************La fonction showAlertDialog affiche le dialog pour afficher les différentes informations lieés à une region données ************/

    private fun showAlertDialog(latlng: LatLng?,n1:Int, n2 : Int ,n3:Int,n4 : Int,n5:Int, wil:String , com:String)
    {   val placeForInformation = LayoutInflater.from(this).inflate(R.layout.layout_dialog_info,null)
        val dial = AlertDialog.Builder(this)
        var infoRegion :ArrayList<Int> = ArrayList()
        val cancel_btn:Button = placeForInformation.findViewById(R.id.cancel_dialog)



        dial.setView(placeForInformation)
        var d = dial.show()
        d.window.setBackgroundDrawableResource(R.drawable.dialog_backgroun_region_info)

        var asjustWilaya:String = " "
        if ( wil.length> 9)
        {
            asjustWilaya = wil.substring(9)
        }

            placeForInformation.findViewById<TextView>(R.id.dialog_num0).text=n1.toString()
            placeForInformation.findViewById<TextView>(R.id.dialog_num1).text=n2.toString()
            placeForInformation.findViewById<TextView>(R.id.dialog_num2).text=n3.toString()
            placeForInformation.findViewById<TextView>(R.id.dialog_num3).text=n4.toString()
            placeForInformation.findViewById<TextView>(R.id.dialog_num4).text=n5.toString()
            placeForInformation.findViewById<TextView>(R.id.dialog_num5).text = com
            placeForInformation.findViewById<TextView>(R.id.dialog_num6).text =asjustWilaya

        cancel_btn.setOnClickListener{
            d.dismiss()
        }
        mMap.addMarker(MarkerOptions().position(latlng!!).title("Marker in somwher").snippet("cooolololoz"))     }
/******************************zoomIn est une fonction qui effectue un zoom in quand un utilisateur clique sur le bouton de zoom************************/
    fun zoomIn(myMap: GoogleMap)
  {
    myMap.animateCamera(CameraUpdateFactory.zoomIn())
  }
    /*****************************La fonction
     * affichCercle fait an API call pour récuperer les données existantes dans la base de données
     * elle rends an ArrayList de Towns qui ont des valeurs non nuls
     * ces donnes seront utilisées pour la fonction d'affichage de detailes de chaque town(region)
     */
    fun afficherCercles (googleMap: GoogleMap) : List<Town>

    {
        var listTowns = ArrayList<Town>()
        var towns: List<Town> =ArrayList<Town>()
        var  list :ArrayList<Int> = ArrayList()

              Log.d("Sonthing ","aqli des affichier cercles")
        val t:Thread = Thread.currentThread()

        Log.d("Sonthing ","Le pid de 1 est : "+t.id.toString())
               val request = Request.Builder()
                   .url("https://corona-watch-api.herokuapp.com/corona-watch-api/v1/geolocation/towns/")
                   .header("Authorization","Basic YWRtaW46YWRtaW4=")
                   .build()
        val t2:Thread = Thread.currentThread()

        Log.d("Sonthing ","Le pid de 2 est : "+t2.id.toString())


        client.newCall(request).enqueue(object : Callback, okhttp3.Callback {


            override fun onFailure(call: Call, e: IOException) {

                Log.d("Sonthing ", "aqli deg on failure n afficher cercle ")
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("Sonthing ", "aqli deg on response n afficher cercle")
                var str_response = response.body()!!.string()
                val t3: Thread = Thread.currentThread()
                Log.d("Sonthing ", "Le pid de 3 est : " + t3.id.toString())
                val gson = Gson()
                val listTownType = object : TypeToken<List<Town>>() {}.type
                towns = gson.fromJson(str_response, listTownType)
                var center = LatLng(36.7050299,3.1739156)
                towns.forEachIndexed { idx,
                                       town ->

                    Log.i("Sonthing","> Item $idx:\n${town.name} ${town.location.latitude} ${town.location.longitude} ${town.number_sick} ${town.number_confirmed_cases}"
                    )
                    if (town.number_death>0 ||town.number_suspect> 0 ||town.number_carrier>0 || town.number_recovered>0 || town.number_sick>0  )
                    {
                        Log.i("Sonthing","> Item $idx celle la est trouvé"  )
                        listTowns.add(town)
                    }
                }
            }
        }
        )
        Thread.sleep(120000)
        return listTowns
    }
    /**********************************La fonction getCommune nretourne le nom de la Wilaya sur laquelle on a cliqué en utilisant le GeoCoder**/
    fun getWilaya(latlng: LatLng):String {
        val loc = Locale("ar")
        var state: String =  " "
        val gcd = Geocoder(this, Locale.getDefault())
        var addresses: MutableList<Address>? = gcd.getFromLocation(latlng.latitude, latlng.longitude, 1)

        //to get country code
        if (addresses!!.get(0).adminArea != null)
        {
            state = addresses.get(0).adminArea

        }
        return state

    }
/**********************************La fonction getCommune nretourne le nom de la commune sur laquelle on a cliqué en utilisant le GeoCoder**/
fun getCommune(latlng: LatLng):String {

    val loc = Locale("ar")

    var town: String =" "
    val gcd = Geocoder(this, Locale.getDefault())
    //Thread.sleep(1000);
    var addresses: MutableList<android.location.Address>? = gcd.getFromLocation(latlng.latitude, latlng.longitude, 1)
    //To get country name

    //to get country code
    if (addresses!!.get(0).locality != null) {
        town = addresses.get(0).locality

    }
    return town
}
}
