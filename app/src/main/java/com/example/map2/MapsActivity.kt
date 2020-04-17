package com.example.map2
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import androidx.appcompat.widget.Toolbar
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.activity_buttumnav.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyle(mMap)
        // Add a marker in Sydney and move the camera
        val algeria = LatLng(28.0339,1.6596)
        val esi = LatLng(36.7050299,3.1739156)
        val poly = LatLng(36.7232318,3.1507596)
        val mameri = LatLng(36.6981966,4.0580482)
        val mira = LatLng(36.7502671,5.0407496)

        mMap.addMarker(MarkerOptions().position(algeria).title("Marker in Algeria").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)) )
        mMap.addMarker(MarkerOptions().position(esi).title("ESI").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)) )
        mMap.addMarker(MarkerOptions().position(poly).title("ENP").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)) )
        mMap.addMarker(MarkerOptions().position(mameri).title("MOULOUD MAMERI").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)))
        mMap.addMarker(MarkerOptions().position(mira).title("ABDRAHMAN MIRA").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)) )
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



}
