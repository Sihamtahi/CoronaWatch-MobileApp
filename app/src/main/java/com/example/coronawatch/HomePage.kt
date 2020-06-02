package com.example.coronawatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.article.R
import kotlinx.android.synthetic.main.activity_main.*

import com.example.coronawatch.Article.web
import com.example.coronawatch.Login.facebook


class HomePage : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val actionbar = supportActionBar

        //set actionbar title
        val titleActivit: String = getString(R.string.app_name)
        actionbar!!.title = titleActivit
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val titWeb: String = getString(R.string.news)
        val titYT: String = getString(R.string.yout)
        val titFB: String = getString(R.string.fb)
        val adapter = MyviewPagerAdapter(supportFragmentManager)

        //val btn_click_me = findViewById(R.id.like) as Button

        adapter.addFragment(web(),title = titWeb)
        adapter.addFragment(youtube(),title = titYT)
        adapter.addFragment(facebook(),title = titFB)

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
}
