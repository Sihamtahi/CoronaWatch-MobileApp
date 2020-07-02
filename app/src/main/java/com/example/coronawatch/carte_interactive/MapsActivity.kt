package com.example.coronawatch

import android.content.Intent
import android.content.SharedPreferences
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.content.res.Resources.NotFoundException
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.article.R
import com.example.coronawatch.Login.*
import com.example.coronawatch.Signaler.EnvoyerVideo
import com.example.coronawatch.model.CircleTransform
import com.example.signaler.SignalerActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_maps.nav_view_menu
import org.jetbrains.anko.doAsync
import okhttp3.*
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.text.DecimalFormat
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback , NavigationView.OnNavigationItemSelectedListener{


    var malade: TextView? = null
    var retablis: TextView? = null

    var btnMalade : Button ?= null
    var btnConfirme : Button ?= null
    var btnRetablis : Button ?= null
    var switchMap : Button ?= null
    var Btnmenu : Button ?= null
    private val TAG = MapsActivity::class.java.simpleName
    val client = OkHttpClient()
    var myListCoord: List<Locations_> ?= null
    var MyLatestData: Map<String, latest> ?= null
    var listcercle = ArrayList<Circle>()

    var nameTxt: TextView ?= null
    var emailTxt: TextView ?= null
    var photo: ImageView?= null
    private var PRIVATE_MODE = 0
    private var PREF_NAME ="coronawatch"
    private var sharedPrefIdUser: SharedPreferences?= null
    private var Islogin: Boolean ?= null

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        malade = this.findViewById(R.id.malade)
        retablis = findViewById(R.id.suspect)
        switchMap = findViewById(R.id.affGen)
        Btnmenu = findViewById(R.id.menu_maps)
        /** la partie concernant menu **/

        nav_view_menu.bringToFront()

        Btnmenu!!.setOnClickListener{
            drawer_layout_maps.openDrawer(Gravity.LEFT)

        }

        nav_view_menu.setNavigationItemSelectedListener(this)
        nav_view_menu.itemIconTintList = null
        nav_view_menu.setCheckedItem(R.id.nav_home)



        /*******************************/

        /** la partie ajoutée concernant la carte (map)**/
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
        btnMalade = bottomSheetDialog.findViewById(R.id.malade_map_bottom)
        btnConfirme = bottomSheetDialog.findViewById(R.id.suspet_map_bottom)
        btnRetablis = bottomSheetDialog.findViewById(R.id.porteur_map_bottom)


        /** Update user info in header of Menu **/
        var header = nav_view_menu.getHeaderView(0)
        nameTxt = header.findViewById(R.id.nom)
        emailTxt = header.findViewById(R.id.mail)
        photo = header.findViewById(R.id.photoProfile)

        /// Vérification de l'authentification avec Google
         sharedPrefIdUser = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
         Islogin = sharedPrefIdUser!!.getBoolean("login_google",false)

        if(Islogin==true){

        var lastName = sharedPrefIdUser!!.getString("user_name","user")
            println("le nom est "+lastName)


        var firstName = sharedPrefIdUser!!.getString("user_name_last","user")
        nameTxt!!.text = lastName+ " "+firstName
        emailTxt!!.text = sharedPrefIdUser!!.getString("user_email","")
        Picasso.get().load(sharedPrefIdUser!!.getString("user_profilePic","")).transform( CircleTransform()).into(photo)
        }


         if(Islogin==true){
           hideLogInItem()
           showLogoutItem()
       }else{
           showLoginItem()
           hideLogoutItem()
       }

        /// Vérification de l'authentification avec Facebook
        var islogged = intent.getBooleanExtra("logged",false)
        if (islogged){

            val name = intent.getStringExtra("name")
            val email = intent.getStringExtra("email")
            val ProfilePicURL = intent.getStringExtra("url")

            Picasso.get().load(ProfilePicURL).transform( CircleTransform()).into(photo)

            nameTxt!!.text = name
            emailTxt!!.text = email

        }

    }

    /**la partie ajoutée qui concerne la map @ovverride de la deuxème fonctions onMapReady**/
    /**SetMapStyle : cette une fonction qui applique le style aubergine qui se trouve dans le dossier row (stylemap.json)**/
    private fun setMapStyle(map: GoogleMap)
    {
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
        } catch (e: NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }
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
        /** lisner pour swicher entre les cartes (aller vers carte de Algérie) **/
        switchMap!!.setOnClickListener {
            val intent = Intent(this, buttumnav::class.java)
            startActivity(intent)
        }

        /**Configurer le zoom pour la map **/
        zoomin.setOnClickListener {
            zoomIn(mMap)
        }

        /**Récuperer code et nom du pays selon lat et long (clic sur la carte) **/
        googleMap.setOnMapLongClickListener {
                latlng ->

            val count_code:String = getCountryCode(latlng)
            Log.i("country code est : ", count_code)
            val nameCountry = getCountryName(latlng)
            showAlertDialog(latlng,count_code,nameCountry)

        }
        fun OnDefaultToggleClick (view:View)
        {

        }
        fun onCostumToggleClick(view:View)
        {

            Toast.makeText(this,"CostumToggle",Toast.LENGTH_SHORT).show()
        }
        mMap.uiSettings.isZoomControlsEnabled =true
        mMap.isBuildingsEnabled=true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isCompassEnabled = true

        /**  Appel de la fonction qui déssine les cercles (par défaut la couleur est jaune (cas confirmés)**/
        doAsync {
            listcercle = DrawCirclesMap(googleMap)
        }
        //////
        /**  filtre pour afficher les cerles en mauve (cas confirmé) **/
        btnMalade!!.setOnClickListener{
            if(!listcercle.isEmpty())
            {
                listcercle.forEachIndexed { idx,
                                            circle ->
                    circle.strokeColor = Color.argb(100,58, 204, 255)
                    circle.fillColor = Color.argb(50, 58, 204, 255)
                    circle.strokeWidth=3f
                }
            }
            else
            {
                Toast.makeText(this,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
            }
        }
        /////
        /**  filtre pour afficher les cerles en rouge(nbr morts) **/
        btnConfirme!!.setOnClickListener {
            if(!listcercle.isEmpty())
            {
                listcercle.forEachIndexed { idx,
                                            circle ->
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
        /////
        /**  filtre pour afficher les cerles en vert (cas rétablis)**/
        btnRetablis!!.setOnClickListener {
            if(!listcercle.isEmpty())
            {
                listcercle.forEachIndexed { idx,
                                            circle ->
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
    }

    /**  fonction pour afficher alert dialog avec des infos du pays sur lequel on clique **/
    private fun showAlertDialog(latlng: LatLng?,count_code:String,count_name:String)
    {
        val placeForInformation = LayoutInflater.from(this).inflate(R.layout.layout_title_dialog,null)
        val dial = AlertDialog.Builder(this)
        val cancel_btn:Button = placeForInformation.findViewById(R.id.cancel_dialog)

        ////////////////////////
        /**  API CALL pour dessiner cercle et avoir données de chaque pays **/
        var nbrMal  : Int = 0
        var nbrSusp : Int = 0
        var nbrPort : Int = 0

        ///////////////////////////

        dial.setView(placeForInformation)
        var d = dial.show()
        d.window!!.setBackgroundDrawableResource(R.drawable.dialog_backgroun_region_info)
        val url = "https://coronavirus-tracker-api.herokuapp.com/v2/locations?country_code="+count_code
        doAsync{
            //latestDataCountry = GetDataCountry(url)
            val request = okhttp3.Request.Builder()
                .url(url)
                .build()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Toast.makeText(this@MapsActivity,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
                }

                override fun onResponse(call: Call, response: okhttp3.Response) {


                    var str_response = response.body!!.string()
                    val json_contact: JSONObject = JSONObject(str_response)
                    var res: JSONObject = json_contact.getJSONObject("latest")

                    runOnUiThread{

                        nbrMal =  res.getInt("confirmed")
                        nbrSusp = res.getInt("deaths")
                        nbrPort = res.getInt("recovered")
                        placeForInformation.findViewById<TextView>(R.id.dialog_num0).text=nbrMal.toString()
                        placeForInformation.findViewById<TextView>(R.id.dialog_num1).text=nbrSusp.toString()
                        placeForInformation.findViewById<TextView>(R.id.dialog_num2).text=nbrPort.toString()
                        placeForInformation.findViewById<TextView>(R.id.country).text=count_name.toString()
                    }
                }
            })
        }
        cancel_btn.setOnClickListener{
            d.dismiss()
        }
        mMap.addMarker(MarkerOptions().position(latlng!!).title(count_name))
    }

    /**  fonction pour zoomer dans la carte **/
    fun zoomIn(myMap: GoogleMap)
    {
        myMap.animateCamera(CameraUpdateFactory.zoomIn())
    }

    /**  fonction pour avoir le code du pays selon latitude et longitude **/
    fun getCountryCode(latlng: LatLng):String {

        val loc = Locale("ar")

        var country_code: String? = null
        val gcd = Geocoder(this, Locale.getDefault())
        if(gcd == null){
            println("oui null")
        }
        var addresses: MutableList<Address>? = gcd.getFromLocation(latlng.latitude, latlng.longitude, 1)
        if(addresses == null){
            Toast.makeText(this,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
        }else{
            //to get country code
            if (addresses!!.get(0).countryCode != null) {
                country_code = addresses.get(0).countryCode
                Log.d("COUNTRY CODE", country_code)
            }
        }
        return country_code!!

    }

    /**  fonction pour avoir le nom du pays selon latitude et longitude **/
    fun getCountryName(latlng: LatLng): String {

        var country_cname: String? = null
        val loc = Locale("ar")

        val gcd = Geocoder(this, loc)
        if(gcd == null){
            Toast.makeText(this,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
        }else{
            var addresses: MutableList<Address>? = gcd.getFromLocation(latlng.latitude, latlng.longitude, 1)
            //To get country name
            if (addresses!!.get(0).countryName != null) {
                country_cname = addresses.get(0).countryName
                Log.d("COUNTRY", country_cname)
            }
        }

        return country_cname!!
    }

    /**  fonction pour déssiner les cercles dans le Map**/
    fun DrawCirclesMap(googleMap: GoogleMap): ArrayList<Circle> {

        var cir: Circle
        var listcercle = ArrayList<Circle>()
        var a = DrawCircles()
        myListCoord= a.runCircles()
        MyLatestData = a.runLatestData()
        if(myListCoord == null){
            progressBar.visibility = View.GONE
            Toast.makeText(this@MapsActivity,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()

        }else {
            runOnUiThread {
                progressBar.visibility = View.GONE
                var country:LatLng
                myListCoord!!.forEachIndexed {
                        idx,
                        coord ->
                    country= LatLng( coord.coordinates.latitude.toDouble(),coord.coordinates.longitude.toDouble())
                    var nb:Double ? = null
                    if(coord.latest.confirmed.toDouble() > 10000){
                        nb = coord.latest.confirmed.toDouble() / 2
                    }else{
                        if( coord.latest.confirmed.toDouble() < 900){
                            nb = coord.latest.confirmed.toDouble() *95
                        }else{
                            nb = coord.latest.confirmed.toDouble() *50
                        }
                    }
                    cir = googleMap.addCircle(
                        CircleOptions()
                            .center(country)
                            .radius(nb)
                            .strokeWidth(3f)
                            .strokeColor(Color.YELLOW)
                            .fillColor(Color.argb(70,0,150,150)))
                    listcercle.add(cir)
                }
                if(MyLatestData == null){
                    Toast.makeText(this@MapsActivity,"تم رفض هذه العملية ، لم يتم تحميل البيانات ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
                }else {
                    progressBar.visibility = View.GONE
                    var formatter = DecimalFormat("#,###")
                   var formattedNumber = formatter.format(MyLatestData!!.getValue("latest").confirmed.toDouble())
                   var formattedNumberdeath = formatter.format(MyLatestData!!.getValue("latest").deaths.toDouble())

                    malade!!.text = formattedNumber
                    retablis!!.text = formattedNumberdeath

                }
            }
        }
        return listcercle
    }
    fun GetDataCountry(url:String): List<Locations_>? {
        var listData : List<Locations_> ?
        var aa = DrawCircles()
        listData = aa.getLatestDataCountry(url)
        return listData
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return super.onCreateOptionsMenu(menu)
        val infl = menuInflater
        infl.inflate(R.menu.main_menu, menu)
        for (i in 0 until menu!!.size()) {
            val item = menu!!.getItem(i)
            val spanString = SpannableString(menu.getItem(i).title.toString())
            val end: Int = spanString.getSpanEnd(spanString)
            val start = spanString.getSpanStart(spanString)
            spanString.setSpan(AbsoluteSizeSpan(55,true), 0, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            item.setTitle(spanString)
        }
        return true
    }
    override fun onBackPressed() {
        if (drawer_layout_maps!!.isDrawerOpen(GravityCompat.START)) {
            drawer_layout_maps!!.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {

        when (menuItem.getItemId()) {
            R.id.nav_home -> {
                val intent = Intent(this,MapsActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_login -> {

                val intent = Intent(this, login::class.java)
                startActivity(intent)

            }
            R.id.nav_reports-> {
                val intent = Intent(this, SignalerActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_feed -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_videoFeeds-> {
                val intent = Intent(this, EnvoyerVideo::class.java)
                startActivity(intent)

            }
            R.id.nav_logout -> {
                var  gso : GoogleSignInOptions = GoogleSignInOptions.
                    Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                    build()
                var googleSignInClient= GoogleSignIn.getClient(this,gso)

                if(Islogin==true){

                    // show the login button
                    showLoginItem()
                    // hide log out button
                    hideLogoutItem()

                    if(googleSignInClient != null){

                        googleSignInClient.signOut()
                        //remove user info from shared preferences
                        val editor:SharedPreferences.Editor =  sharedPrefIdUser!!.edit()
                        editor.remove("login_google")
                        editor.remove("user_name")
                        editor.remove("user_email")
                        editor.remove("user_name_last")
                        editor.remove("user_profilePic")
                        editor.commit()
                        editor.apply()

                        Toast.makeText(this@MapsActivity, "أنت الآن غير متصل بهذا التطبيق", Toast.LENGTH_LONG).show()
                    } else{
                        println("user nest pas connecte ")
                    }

                } else println("nooo")

            }

        }
        drawer_layout_maps.closeDrawer(GravityCompat.START)
        return true
    }
    fun showLoginItem(){
        val item: MenuItem = nav_view_menu.menu.findItem(R.id.nav_login)
        item.isVisible = true
    }
    fun hideLogInItem(){
        val item: MenuItem = nav_view_menu.menu.findItem(R.id.nav_login)
        item.isVisible = false
    }
    fun showLogoutItem(){
        val itemlogout: MenuItem = nav_view_menu.menu.findItem(R.id.nav_logout)
        itemlogout.isVisible = true
    }
    fun hideLogoutItem(){
        val itemlogout: MenuItem = nav_view_menu.menu.findItem(R.id.nav_logout)
        itemlogout.isVisible = false
    }

}