package com.example.signaler

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.xml.datatype.DatatypeConstants.SECONDS
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull





class MainActivity : AppCompatActivity() {



    private val INTENT_REQUEST_CODE = 100

    val URL = "http://corona-watch-api.herokuapp.com/"

    private var mBtImageSelect: Button? = null
    private var mBtImageShow: Button? = null
    private var mProgressBar: ProgressBar? = null
    private var mImageUrl = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_test)

        var btn :Button = findViewById<Button>(R.id.image)
        btn.setOnClickListener{
            PostDataToApi()

        }
    }
    fun PostDataToApi(){

        var user : ArrayList<Int> = ArrayList()
        user.add(1)
        var attach = Attachment("image","corona.png","image","image/upload/v1589848225/ad0m2fkg0yp9k1utdqfn.png","2020-06-19T13:16:59.155162Z")
        var cas_suspect =  suspected (attach,"2020-06-19T13:16:59.155162Z",false,18,user)


        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient().newBuilder()
            .retryOnConnectionFailure(true)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)

        okHttpClient.addInterceptor(loggingInterceptor).build()
        okHttpClient.retryOnConnectionFailure(true)

        okHttpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Accept", "application/json")
                .addHeader("Connection", "close")
                .build()
            chain.proceed(request)
        }

        val BaseUrl = "http://corona-watch-api.herokuapp.com/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)
        val call = service.uploadImageToApi(cas_suspect)


        call!!.enqueue(object : Callback<suspected?> {
            override fun onFailure(call: Call<suspected?>, t: Throwable) {
                Log.e("hhh", "Unable to submit post to API." + t.message)
            }

            override fun onResponse(call: Call<suspected?>, response: Response<suspected?>) {
                if (response.isSuccessful()) {
                    println(response.body()!!.toString())
                    Log.i( "hhh","post submitted to API." + response.body()!!.toString())
                }
            }
        })
    }

}


