package com.example.coronawatch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.profile.*

class profileactivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        var nameTV: TextView = findViewById(R.id.nameTV)
        var emailTV: TextView = findViewById(R.id.emailTV)
        var genderTV: TextView = findViewById(R.id.genderTV)
        var profileIV: ImageView = findViewById(R.id.imageViewProfile)

        var name : String = intent.getStringExtra("name");
        var email : String = intent.getStringExtra("email");
        var gender : String = intent.getStringExtra("gender");
        var url : String = intent.getStringExtra("url");
        //Log.e("Prefile", name + " "+ email+"  "+ gender+" "+ url)

        nameTV.setText(name)
        emailTV.setText(email)
        genderTV.setText(gender)
        profileIV.loadUrl(url)

    }
    fun ImageView.loadUrl(url: String) {
        Picasso.with(context).load(url).into(this)
    }
}