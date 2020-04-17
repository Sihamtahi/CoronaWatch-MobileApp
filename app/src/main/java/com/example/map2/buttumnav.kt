package com.example.map2
import android.content.res.Resources
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.AlertDialog
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

private  const val TAG="buttumnav"
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
        mMap = googleMap
        setMapStyle(mMap)
        // Add a marker in Sydney and move the camera
        val algeria = LatLng(28.0339,1.6596)
        val esi = LatLng(36.7050299,3.1739156)
        val poly = LatLng(36.7232318,3.1507596)
        val mameri = LatLng(36.6981966,4.0580482)
        val mira = LatLng(36.7502671,5.0407496)
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


        mMap.setOnMapLongClickListener {
                latlng ->
           Log.i(TAG,"OnMapLongClickListener")
           showAlertDialog(latlng)

        }
       fun OnDefaultToggleClick (view:View)
        {

        }
        fun onCostumToggleClick(view:View)
        {

            Toast.makeText(this,"CostumToggle",Toast.LENGTH_SHORT).show()
        }

        mMap.addMarker(
            MarkerOptions().position(algeria).title("Marker in Algeria").icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE)) )
        mMap.addMarker(
            MarkerOptions().position(esi).title("ESI").icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE)) )
        mMap.addMarker(
            MarkerOptions().position(poly).title("ENP").icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE)) )
        mMap.addMarker(
            MarkerOptions().position(mameri).title("MOULOUD MAMERI").icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE)))
        mMap.addMarker(
            MarkerOptions().position(mira).title("ABDRAHMAN MIRA").icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE)) )
        mMap.moveCamera(CameraUpdateFactory.newLatLng(algeria))
        mMap.addCircle(
            CircleOptions()
                .center(esi)
                .radius(50000.0)
                .strokeWidth(3f)
                .strokeColor(Color.YELLOW)
                .fillColor(Color.argb(70,0,150,150))
        )
        mMap.addCircle(
            CircleOptions()
                .center(algeria)
                .radius(100000.0)
                .strokeWidth(3f)
                .strokeColor(Color.YELLOW)
                .fillColor(Color.YELLOW)
        )
        mMap.addCircle(
            CircleOptions()
                .center(mira)
                .radius(50000.0)
                .strokeWidth(3f)
                .strokeColor(Color.YELLOW)
                .fillColor(Color.argb(70,0,150,150))
        )
        mMap.uiSettings.isZoomControlsEnabled =true
        mMap.isBuildingsEnabled=true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isCompassEnabled = true
    }

    private fun showAlertDialog(latlng: LatLng?)
    {   val placeForInformation = LayoutInflater.from(this).inflate(R.layout.layout_dialog_info,null)
       /* val placeForTite = LayoutInflater.from(this).inflate(R.layout.layout_title_dialog,null)
        val back = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout,null)

        AlertDialog.Builder(this)
            .setCustomTitle(placeForTite)
            .setView(placeForInformation)
            .setNegativeButton("Annuler",null)
            .show()*/
        val dial = AlertDialog.Builder(this)
        val cancel_btn:Button = placeForInformation.findViewById(R.id.cancel_dialog)

        dial.setView(placeForInformation)
        var d = dial.show()
        d.window.setBackgroundDrawableResource(R.drawable.dialog_backgroun_region_info)
        cancel_btn.setOnClickListener{
            d.dismiss()
        }
        mMap.addMarker(MarkerOptions().position(latlng!!).title("Marker in somwher").snippet("cooolololoz"))     }

    fun zoomIn(myMap: GoogleMap)
  {
    myMap.animateCamera(CameraUpdateFactory.zoomIn())
  }

}




