package com.example.coronawatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.article.R
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.LinearLayout
import android.graphics.drawable.GradientDrawable

class MainActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.article.R.layout.activity_main)

        val actionbar = supportActionBar

      //  getSupportActionBar()!!.setShowHideAnimationEnabled(false)
      //  set actionbar title
        val titleActivit: String = getString(com.example.article.R.string.title_name)
        actionbar!!.title = titleActivit
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
       actionbar.setDisplayHomeAsUpEnabled(true)

        val titWeb: String = getString(com.example.article.R.string.news)
        val titYT: String = getString(com.example.article.R.string.yout)
        val adapter = MyviewPagerAdapter(supportFragmentManager)

        // split tab Layout
        val root = tabs.getChildAt(0)
        if (root is LinearLayout) {
            (root as LinearLayout).showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(resources.getColor(com.example.article.R.color.material_grey_600))
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
}
