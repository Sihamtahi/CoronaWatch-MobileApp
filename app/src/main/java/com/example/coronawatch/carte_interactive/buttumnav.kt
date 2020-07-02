package com.example.coronawatch

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_buttumnav.*
import javax.security.auth.callback.Callback
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import java.io.IOException
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.example.article.R
import com.example.map2.MyService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


private  const val TAG="buttumnav"
//OkHttpClient creates connection pool between client and server
val client = OkHttpClient.Builder()
    .connectTimeout(10, TimeUnit.SECONDS)//connect time
    .writeTimeout(10, TimeUnit.SECONDS)//write time
    .readTimeout(30, TimeUnit.SECONDS)//socket read time
    .build()
//information sur l'api API
val API_LINK_TOWNS ="https://corona-watch-api.herokuapp.com/corona-watch-api/v1/geolocation/towns/"
val API_HEADER_KEY="Authorization"
val API_HEADRER_VALUE="token ee5f6766123e0fa438f03380f300a8f74f081c9f"

//liste des towns and
var listTownsInfo:List<Town> = ArrayList<Town>() // une liste pour savegarder la liste des Towns qui contiennent des informations
var listCircles:List<Circle> = ArrayList<Circle>() //une liste pour sauvegader les cercles pour pouvoir changer leur couleurs
var Mal  : Int = 0
var Susp : Int = 0
var Port : Int = 0
var Guer : Int = 0
var Mort : Int = 0
lateinit var bottomSheetDialog :BottomSheetDialog

class buttumnav : AppCompatActivity() ,OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    var switchMap: Button ?= null
    /*********************************Variables utilisées dans la locatisation et la notification**/
    lateinit var locationRequest : LocationRequest
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    companion object {
        var instancee: buttumnav? = null
        fun getMainInstancee():buttumnav{return instancee!!}
    }
    fun updateTextView(value :String )
    {        this@buttumnav.runOnUiThread{
        // text_location2.text=value
        // locationText.text=value
    }
    }
    fun getPending(): PendingIntent?{
        return getPendingIntent()
    }


    /**********************Fin localisation **************************************************************/
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buttumnav)
        /**********Retourner le contexte d***/
        instancee = this//
        /** la partie ajoutée concernant la map**/
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        /** la partie pour le buttom sheet dialog
         * Configuration des deux buttons pour que le dialog apparait**/
        bottomSheetDialog = BottomSheetDialog(this)
        val view=layoutInflater.inflate(R.layout.botton_sheet_view,null)
        bottomSheetDialog.setContentView(view)
        register.setOnClickListener { bottomSheetDialog.show() }
        swip_up.setOnClickListener { bottomSheetDialog.show() }

        switchMap = findViewById(R.id.switchmap)
        /****************launch the background location *************/
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(object : PermissionListener {
                    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                        updateLocation()
                    }
                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?){}
                    override fun onPermissionDenied(response: PermissionDeniedResponse?)
                    {
                        Toast.makeText(this@buttumnav,"You must accpet location permission",Toast.LENGTH_SHORT).show()

                    }
                })
                .check()


    }
    /****************paramétrge de a localisation et le update *************************************************/
    private fun updateLocation() /**faire un update de la localisation c'est à dire récupérer lat et long ***/
    {
        buildLocationRequest()
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent())
    }

    private fun getPendingIntent(): PendingIntent? {
        val intent = Intent(this@buttumnav,MyService::class.java)
        intent.setAction(MyService.ACTION_PROCESS_UPDATE)
        return PendingIntent.getBroadcast(this@buttumnav,0,intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun buildLocationRequest()/****Parametrer la localisation *****/
    {
        locationRequest=LocationRequest()
        locationRequest.priority=LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval=5000
        locationRequest.fastestInterval=3000
        locationRequest.smallestDisplacement=30f //faire un update pour chaque 150 m
    }
    /**la partie ajoutée qui concerne la map @ovverride de la deuxème fonctions onMapReady**/
    /**SetMapStyle : cette une fonction qui applique le style aubergine qui se trouve dans le dossier row (stylemap.json)**/
    private val TAG = MapsActivity::class.java.simpleName
    private fun setMapStyle(map: GoogleMap) {
        try {
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
        /*******************Effectuer un zoom sur l'algèrie************************************/
        var alg = LatLng(31.3605672,2.0176203)
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(alg, 5F),15000,null)
    }
    override fun onMapReady(googleMap: GoogleMap)
    {
        /**Afficher les cercles des zones affectés sur la map **/
        var cercles = ArrayList<Circle>()
        listTownsInfo = afficherCercles(googleMap,cercles)///ArrayList<Town>()

        if (listTownsInfo.isEmpty())
        {
            Log.d("Sonthing ","La liste retournée est vide !!!!!!!!")
        }
        else
        {
            Log.d("Sonthing ","La liste retournée n'est pas  vide ???")
        }

        /***Appliquer le style à la map **/
        mMap = googleMap
        setMapStyle(mMap)

        /**Confuguer le zoom pour la map **/
        zoomin.setOnClickListener {
            zoomIn(mMap)
        }
        /**********lisner pour swicher entre les deux cartes**/
        switchMap!!.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
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
        /****************************************Dans cette partie à chaque fois il clique sur les button je change les couleurs selon le buuton cliqué**************/
        var btnMal: Button = bottomSheetDialog.findViewById<Button>(R.id.malade_map_bottom)!!
        var btnSusp: Button = bottomSheetDialog.findViewById<Button>(R.id.suspet_map_bottom)!!
        var btnPort: Button = bottomSheetDialog.findViewById<Button>(R.id.porteur_map_bottom)!!
        var btnMort: Button = bottomSheetDialog.findViewById<Button>(R.id.mort_map_bottom)!!
        var btnGuer: Button = bottomSheetDialog.findViewById<Button>(R.id.guer_map_bottom)!!

        btnMal.setOnClickListener {
            Log.d("Sonthing","btnMal")
            if(!listCircles.isEmpty())
            {
                listCircles.forEachIndexed { idx,
                                             circle ->
                    circle.radius = listTownsInfo.get(idx).number_confirmed_cases * 100.0 + 2000.0
                    Log.d("Sonthing","l'element vaut : "+ listTownsInfo.get(idx).number_death)
                    circle.strokeColor = Color.argb(100,200, 64, 233)
                    circle.fillColor = Color.argb(30, 200, 64, 233)
                    circle.strokeWidth=3f
                }
            }
            else
            {
                Toast.makeText(this,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
            }
        }
        btnSusp.setOnClickListener {
            Log.d("Sonthing","btnSuso")
            if(!listCircles.isEmpty())
            {
                listCircles.forEachIndexed { idx,
                                             circle ->
                    circle.radius = listTownsInfo.get(idx).number_suspect * 100.0 + 2000.0
                    circle.strokeColor = Color.argb(100,255, 185, 0)
                    circle.fillColor = Color.argb(30,255, 185, 0)
                    circle.strokeWidth=3f
                }
            }
            else
            {
                Toast.makeText(this,"لتم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
            }
        }
        btnPort.setOnClickListener {
            Log.d("Sonthing","btnPort")
            if(!listCircles.isEmpty())
            {
                listCircles.forEachIndexed { idx,
                                             circle ->
                    circle.radius = listTownsInfo.get(idx).number_carrier * 200.0 + 1000.0
                    circle.strokeColor = Color.argb(100,58, 204, 225)
                    circle.fillColor = Color.argb(50, 58, 204, 225)
                    circle.strokeWidth=3f
                }
            }
            else
            {
                Toast.makeText(this,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
            }
        }
        btnMort.setOnClickListener {
            Log.d("Sonthing","btnMort")
            if(!listCircles.isEmpty())
            {
                listCircles.forEachIndexed { idx,
                                             circle ->
                    circle.radius = listTownsInfo.get(idx).number_death * 500.0 + 1000.0
                    circle.strokeColor = Color.argb(100,255,0,0)
                    circle.fillColor = Color.argb(50,255,0,0)
                    circle.strokeWidth=3f
                }
            }
            else
            {
                Toast.makeText(this,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
            }
        }
        btnGuer.setOnClickListener {
            Log.d("Sonthing","btnGuer")
            if(!listCircles.isEmpty())
            {
                listCircles.forEachIndexed { idx,
                                             circle ->
                    circle.radius = listTownsInfo.get(idx).number_recovered * 500.0 + 1000.0
                    circle.strokeColor = Color.argb(100,10,191,10)
                    circle.fillColor = Color.argb(50, 10,191,10)
                    circle.strokeWidth=3f
                }
            }
            else
            {
                Toast.makeText(this,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
            }
        }
        /*affGen.setOnClickListener {
            Log.d("Sonthing","affect")
            if(!listCircles.isEmpty())
            {
                listCircles.forEachIndexed { idx,
                                             circle ->
                    circle.radius = (listTownsInfo.get(idx).number_confirmed_cases + listTownsInfo.get(idx).number_death) *50.0 + listTownsInfo.get(idx).number_suspect* 50.0
                    circle.strokeColor = Color.YELLOW
                    circle.fillColor = Color.argb(50, 0,150,150)
                    circle.strokeWidth=3f
                }
            }
            else
            {
                Toast.makeText(this,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
            }
        }*/
        /***************************************************************************************************************************************/
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
    {
        val placeForInformation = LayoutInflater.from(this).inflate(R.layout.layout_dialog_info,null)
        val dial = AlertDialog.Builder(this)
        var infoRegion :ArrayList<Int> = ArrayList()
        val cancel_btn:Button = placeForInformation.findViewById(R.id.cancel_dialog)



        dial.setView(placeForInformation)
        var d = dial.show()
        d.window!!.setBackgroundDrawableResource(R.drawable.dialog_backgroun_region_info)
        placeForInformation.findViewById<TextView>(R.id.dialog_num0).text=n1.toString()
        placeForInformation.findViewById<TextView>(R.id.dialog_num1).text=n2.toString()
        placeForInformation.findViewById<TextView>(R.id.dialog_num2).text=n3.toString()
        placeForInformation.findViewById<TextView>(R.id.dialog_num3).text=n4.toString()
        placeForInformation.findViewById<TextView>(R.id.dialog_num4).text=n5.toString()
        placeForInformation.findViewById<TextView>(R.id.dialog_num5).text = com
        placeForInformation.findViewById<TextView>(R.id.dialog_num6).text =wil

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
    fun afficherCercles (googleMap: GoogleMap,listC:ArrayList<Circle>) : List<Town>

    {
        var listTowns = ArrayList<Town>()
        var listcercle = ArrayList<Circle>()

        var towns: List<Town> =ArrayList<Town>()
        var  list :ArrayList<Int> = ArrayList()

        Log.d("Sonthing ","aqli des affichier cercles")
        val t:Thread = Thread.currentThread()

        Log.d("Sonthing ","Le pid de 1 est : "+t.id.toString())
        val request = Request.Builder()
            .url(API_LINK_TOWNS)
            .header(API_HEADER_KEY, API_HEADRER_VALUE)
            .build()

        client.newCall(request).enqueue(object : Callback, okhttp3.Callback {


            override fun onFailure(call: Call, e: IOException) {

                Log.d("Sonthing ", "aqli deg on failure n afficher cercle ")
                runOnUiThread {
                    Toast.makeText(
                        this@buttumnav,
                        "خطأ أثناء تحميل البيانات من واجهة برمجة التطبيقات ، يرجى التحقق من اتصال الإنترنت الخاص بك",
                        Toast.LENGTH_LONG
                    ).show()
                    progressBar.visibility = View.GONE

                }
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d("Sonthing ", "aqli deg on response n afficher cercle")
                response.use {
                    if (!response.isSuccessful)
                    {
                        runOnUiThread {
                            Toast.makeText(
                                this@buttumnav,
                                "خطأ أثناء تحميل البيانات من واجهة برمجة التطبيقات ، سرعة الاتصال الخاصة بك غير كافية لتحميل البيانات",
                                Toast.LENGTH_LONG
                            ).show()
                            progressBar.visibility = View.GONE
                        }
                    }
                    else {
                        var str_response = ""
                        str_response = response.body!!.string()

                        Log.d("Sonthing ", " La réponse est : " + str_response)
                        runOnUiThread {
                            progressBar.visibility = View.GONE
                        }
                        val gson = Gson()
                        val listTownType = object : TypeToken<List<Town>>() {}.type
                        towns = gson.fromJson(str_response, listTownType)
                        var center = LatLng(36.7050299, 3.1739156)
                        var cir: Circle
                        towns.forEachIndexed { idx,
                                               town ->
                            if (town.number_death > 0 || town.number_suspect > 0 || town.number_carrier > 0 || town.number_recovered > 0 || town.number_sick > 0) {
                                listTowns.add(town)
                                runOnUiThread {
                                    center = LatLng(town.location.latitude.toDouble(), town.location.longitude.toDouble())
                                    cir = googleMap.addCircle(
                                        CircleOptions()
                                            .center(center)
                                            .radius( (town.number_confirmed_cases + town.number_death) *50.0 + town.number_suspect*50.0  )
                                            .strokeWidth(3f)
                                            .strokeColor(Color.YELLOW)
                                            .clickable(true)
                                            .fillColor(Color.argb(70, 0, 150, 150))
                                    )
                                    listcercle.add(cir)
                                    if (idx == 1) {
                                        cir.strokeColor = Color.WHITE
                                    }
                                    Mal = Mal + town.number_confirmed_cases
                                    Susp = Susp + town.number_suspect
                                    Port = Port + town.number_carrier
                                    Guer = Guer + town.number_recovered
                                    Mort = Mort + town.number_death
                                }
                            }
                        }
                        runOnUiThread {
                            /****Mettre le nombre  total de mort,porteurs, malades, guéris,suspets **/
                            var nbrCas: TextView = findViewById(R.id.malade1_map)
                            var nbrSus: TextView = findViewById(R.id.suspect1_map)
                            var nbrPort: TextView = findViewById(R.id.porteur1_map)
                            var nbrMrt: TextView = findViewById(R.id.mort1_map)
                            var nbrGuer: TextView = findViewById(R.id.Guer1_map)
                            nbrCas.text = Mal.toString()
                            nbrSus.text = Susp.toString()
                            nbrPort.text = Port.toString()
                            nbrGuer.text = Guer.toString()
                            nbrMrt.text = Mort.toString()

                        }
                        listCircles = listcercle
                    }
                }
            } }
        )
        return listTowns
    }
    /**********************************La fonction getCommune nretourne le nom de la Wilaya sur laquelle on a cliqué en utilisant le GeoCoder**/
    fun getWilaya(latlng: LatLng):String {
        val loc = Locale("ar")
        var state: String =  " "
        val gcd = Geocoder(this, loc)
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
        val gcd = Geocoder(this, loc)
        //Thread.sleep(1000);
        var addresses: MutableList<android.location.Address>? = gcd.getFromLocation(latlng.latitude, latlng.longitude, 1)
        //To get country name
        if (addresses!!.get(0).locality != null)
        {
            town = addresses.get(0).locality

        }
        return town
    }
}