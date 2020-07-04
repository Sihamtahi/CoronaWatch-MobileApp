package com.example.coronawatch

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.article.R
import kotlinx.android.synthetic.main.activity_main.*
import android.graphics.drawable.GradientDrawable
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.coronawatch.Article.web
import com.google.android.gms.common.api.GoogleApiClient



class MainActivity : AppCompatActivity() {


    var Islogin : Boolean = false
    private var PRIVATE_MODE = 0
    private var PREF_NAME ="coronawatch"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       /* val actionbar = supportActionBar
          getSupportActionBar()!!.setShowHideAnimationEnabled(false)
          val titleActivit: String = getString(com.example.article.R.string.title_name)
          actionbar!!.title = titleActivit
        */

       // toolbar.setTitle( getString(R.string.title_name_feeds) )
        //placing toolbar in place of actionbar


        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setElevation(0.0F)
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        //////////////////////////////////////////////////////////////

        val titWeb: String = getString(R.string.news)
        val titYT: String = getString(R.string.yout)
        var titVideo : String = getString(R.string.vide)
        val adapter = MyviewPagerAdapter(supportFragmentManager)

        // split tab Layout
        val root = tabs.getChildAt(0)
        if (root is LinearLayout) {
            (root ).showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE

            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(R.color.material_grey_600))
            drawable.setSize(2, 1)
            (root ).dividerPadding = 10
            (root ).dividerDrawable = drawable
        }

 // Get shared pref of login with google & fb

        val sharedPrefIdUser: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        Islogin = sharedPrefIdUser!!.getBoolean("login_google",false)

       if(Islogin){
         invalidateOptionsMenu()
          }


        adapter.addFragment(web(),title = titWeb)
        adapter.addFragment(youtube(),title = titYT)
        adapter.addFragment(FragementvideoFeeds(),title = titVideo)

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),0

            )
        }
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
//            btn!!.isEnabled = false
//            video!!.isEnabled = false
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),
                0
            )
        }



    }
    class MyviewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager){
        private val fragmentList: MutableList<Fragment> =ArrayList()
        private val titleList: MutableList<String> =ArrayList()
        override fun getCount(): Int {
            return fragmentList.size
        }
        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }
        fun addFragment(fragment: Fragment,title:String){
            fragmentList.add(fragment)
            titleList.add(title)
        }
        override fun getPageTitle(position: Int): CharSequence? {
            return titleList[position]
        }
    }


}
