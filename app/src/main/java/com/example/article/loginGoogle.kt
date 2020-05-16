package com.example.coronawatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.article.R
import kotlinx.android.synthetic.main.profilegoogle.*

class loginGoogle : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.profilegoogle)

        val googleId = intent.getStringExtra("google_id")
        val googleFirstName = intent.getStringExtra("google_first_name")
        val googleLastName = intent.getStringExtra("google_last_name")
        val googleEmail = intent.getStringExtra("google_email")
        val googleProfilePicURL = intent.getStringExtra("google_profile_pic_url")
        val googleAccessToken = intent.getStringExtra("google_id_token")


        google_id_textview.text = googleId
        google_first_name_textview.text = googleFirstName
        google_last_name_textview.text = googleLastName
        google_email_textview.text = googleEmail
        google_profile_pic_textview.text = googleProfilePicURL
        google_id_token_textview.text = googleAccessToken

    }
}