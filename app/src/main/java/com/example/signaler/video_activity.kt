package com.example.signaler

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Video : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_layout)

        var btn : Button = findViewById(R.id.uploadVideo)
        btn.setOnClickListener{

//            PostVideoToApi()

        }
    }


    /*fun PostVideoToApi(){

        var user : ArrayList<Int> = ArrayList()

       // user.add(1)
  //      var attach = Attachment("video","corona.jpg","video","image/upload/v1589848225/ad0m2fkg0yp9k1utdqfn.png","2020-06-19T13:20:20.155162Z")
//        var video_cas = video(attach,"2020-06-19T13:20:20.155162Z",false,"video sur corona","video pour test",1)

      //  var test = videoTest("hwhrascg","https://www.youtube.com/watch?v=-jStdn0jASU")

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

        val BaseUrl = "https://api.cloudinary.com/"
        val retrofit = Retrofit.Builder()
            .baseUrl(BaseUrl)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(APIService::class.java)
        val call = service.uploadVideoTest(test)


        call!!.enqueue(object : Callback<videoTest?> {
            override fun onFailure(call: Call<videoTest?>, t: Throwable) {
                Log.e("hhh", "Unable to submit post to API." + t.message)
            }

            override fun onResponse(call: Call<videoTest?>, response: Response<videoTest?>) {
                if (response.isSuccessful()) {
                    println(response.body()!!.toString())
                    Log.i( "hhh","post submitted to API." + response.body()!!.toString())
                }
            }
        })
    }*/

}