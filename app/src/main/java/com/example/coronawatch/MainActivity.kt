package com.example.coronawatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.article.R
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.LinearLayout
import android.graphics.drawable.GradientDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.example.coronawatch.Article.web
import com.example.coronawatch.Login.*
import com.google.android.material.navigation.NavigationView
import com.squareup.picasso.Picasso

class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener{
    var nameTxt: TextView ?= null
    var emailTxt: TextView ?= null
    var genderTxt: TextView ?= null
    var photo: ImageView?= null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //  val actionbar = supportActionBar

        // getSupportActionBar()!!.setShowHideAnimationEnabled(false)

        //val titleActivit: String = getString(com.example.article.R.string.title_name)
        //actionbar!!.title = titleActivit
        //set back button
        //actionbar.setDisplayHomeAsUpEnabled(true)
        //actionbar.setDisplayHomeAsUpEnabled(true)

        setSupportActionBar(toolbar)

        nav_view_menu.bringToFront()

        var toggle = ActionBarDrawerToggle(this,
            drawer_layout, toolbar,
            R.string.navigation_drawer_close,
            R.string.navigation_drawer_close
        )

        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view_menu.setNavigationItemSelectedListener(this)
        nav_view_menu.itemIconTintList = null
        nav_view_menu.setCheckedItem(R.id.nav_home)

        val placeForInformation = LayoutInflater.from(this).inflate(R.layout.header,null)
        nameTxt = placeForInformation.findViewById(R.id.nom)
        emailTxt = placeForInformation.findViewById(R.id.mail)
        photo = placeForInformation.findViewById(R.id.photoProfile)

        var islogged = intent.getBooleanExtra("logged",false)
        if (islogged){

            val name = intent.getStringExtra("name")
            val email = intent.getStringExtra("email")
            val ProfilePicURL = intent.getStringExtra("url")

            Picasso.get().load(ProfilePicURL).resize(600*2,278*2).into(photo)

            nameTxt!!.text = name
            emailTxt!!.text = email
        }

        val titWeb: String = getString(R.string.news)
        val titYT: String = getString(R.string.yout)
        val adapter = MyviewPagerAdapter(supportFragmentManager)

        // split tab Layout
        val root = tabs.getChildAt(0)
        if (root is LinearLayout) {
            (root as LinearLayout).showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(R.color.material_grey_600))
            drawable.setSize(2, 1)
            (root as LinearLayout).dividerPadding = 10
            (root as LinearLayout).dividerDrawable = drawable
        }


        adapter.addFragment(web(),title = titWeb)
        adapter.addFragment(youtube(),title = titYT)

        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)


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
        if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
            drawer_layout!!.closeDrawer(GravityCompat.START)
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
            R.id.nav_feed -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)

            }
            R.id.nav_login -> {
                val intent = Intent(this,login::class.java)
                startActivity(intent)

            }
            R.id.nav_logout -> {

//
//                menu!!.findItem(R.id.nav_logout).isVisible = false
//                menu!!.findItem(R.id.nav_profile).isVisible = false
//                menu!!.findItem(R.id.nav_login).isVisible = true
            }

        }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
