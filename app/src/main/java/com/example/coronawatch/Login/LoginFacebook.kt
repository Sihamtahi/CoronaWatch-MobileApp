package com.example.coronawatch.Login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.profile.*
import com.example.article.R
import com.squareup.picasso.Picasso

class ProfileUser : AppCompatActivity() {

     var nameTxt: TextView ?= null
     var emailTxt: TextView ?= null
     var genderTxt: TextView ?= null
     var urlTxt: TextView ?= null
     var photoProfile: ImageView?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profile)

        nameTxt = findViewById(R.id.name)
        emailTxt = findViewById(R.id.mail)
        genderTxt = findViewById(R.id.gender)
        photoProfile = findViewById(R.id.profileImage)

        val name = intent.getStringExtra("name")
        val email = intent.getStringExtra("email")
        val gender = intent.getStringExtra("gender")
        val ProfilePicURL = intent.getStringExtra("url")

        Picasso.get().load(ProfilePicURL).resize(600*2,278*2).into(photoProfile)

        nameTxt!!.text = name
        emailTxt!!.text = email
        genderTxt!!.text = gender

        println("ouiii c bn")

    }
}