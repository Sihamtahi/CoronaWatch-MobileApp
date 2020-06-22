package com.example.signaler


import android.app.ProgressDialog
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.collections.ArrayList
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody


class VideoActivity: AppCompatActivity() {
    var button: Button? = null
     var uploadVideo:Button? = null
    val REQUEST_PICK_VIDEO = 3
    var pDialog: ProgressDialog? = null
    var mVideoView: VideoView? = null
    var mBufferingTextView: TextView? = null
     var video: Uri? = null
    var videoPath: String? = null

    // Current playback position (in milliseconds).
    private var mCurrentPosition = 0

    // Tag for the instance state bundle.
    private val PLAYBACK_TIME = "play_time"

     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_layout)
        button = findViewById(R.id.pickVideo) as Button
        uploadVideo = findViewById(R.id.uploadVideo) as Button
        button!!.setOnClickListener {
            val pickVideoIntent = Intent(Intent.ACTION_GET_CONTENT)
            pickVideoIntent.type = "video/*"
            startActivityForResult(pickVideoIntent, REQUEST_PICK_VIDEO)
        }

      /* var hasPermission = (ContextCompat.checkSelfPermission(this,
             Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
         if (!hasPermission) {
             ActivityCompat.requestPermissions(this,
                 arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),REQUEST_WRITE_STORAGE)
         }*/

        uploadVideo!!.setOnClickListener(View.OnClickListener {
            if (video != null) {

                Toast.makeText(this, "path is " + getPath(video!!), Toast.LENGTH_SHORT)
                    .show()

                //  uploadFile()
             PostVideoToApi()
            } else {
                Toast.makeText(this, "Please select a video", Toast.LENGTH_SHORT)
                    .show()
            }

        })
        mVideoView = findViewById(R.id.videoview)
        mBufferingTextView = findViewById(R.id.buffering_textview) as TextView?
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME)
        }
        // Set up the media controller widget and attach it to the video view.
        val controller = MediaController(this)
        controller.setMediaPlayer(mVideoView)
        mVideoView!!.setMediaController(controller)
        initDialog()
    }


     override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            mVideoView!!.pause()
        }
    }

   /* override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode)
       {
        REQUEST_WRITE_STORAGE -> {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //reload my activity with permission granted or use the features what required the permission
            } else
            {
                Toast.makeText(this, "The app was not allowed to write to your storage. Hence, it cannot function properly. Please consider granting it this permission", Toast.LENGTH_LONG).show();
            }
        }
      }

    }*/
     override fun onStop() {
        super.onStop()
        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer()
    }

     override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current playback position (in milliseconds) to the
         // instance state bundle.
        outState.putInt(PLAYBACK_TIME, mVideoView!!.currentPosition)
    }

     fun initializePlayer(uri: Uri?) { // Show the "Buffering..." message while the video loads.
        mBufferingTextView!!.visibility = VideoView.VISIBLE
        if (uri != null) {
            mVideoView!!.setVideoURI(uri)
        }
        // Listener for onPrepared() event (runs after the media is prepared).
        mVideoView!!.setOnPreparedListener {
            // Hide buffering message.
            mBufferingTextView!!.visibility = VideoView.INVISIBLE
            // Restore saved position, if available.
            if (mCurrentPosition > 0) {
                mVideoView!!.seekTo(mCurrentPosition)
            } else { // Skipping to 1 shows the first frame of the video.
                mVideoView!!.seekTo(1)
            }
            // Start playing!
            mVideoView!!.start()
        }
        // Listener for onCompletion() event (runs after media has finished
// playing).
        mVideoView!!.setOnCompletionListener {
            Toast.makeText(
                this,
                "video",
                Toast.LENGTH_SHORT
            ).show()
            // Return the video position to the start.
            mVideoView!!.seekTo(0)
        }
    }

     fun releasePlayer() {
        mVideoView!!.stopPlayback()
    }

     override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_PICK_VIDEO) {
                if (data != null) {
                    Toast.makeText(
                        this, "Video content URI: " + data.data,
                        Toast.LENGTH_LONG
                    ).show()
                    video = data.data
                    videoPath = getPath(video!!)
                    Toast.makeText(this, "this is it! $videoPath", Toast.LENGTH_LONG).show()
                    initializePlayer(video)

                    // uploadFile(video.getPath());
                }
            }
        } else if (resultCode != RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show()
        }
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(
            MediaStore.Video.Media.DATA
        )
        val cursor: Cursor? =
            getContentResolver().query(uri, projection, null, null, null)
        if (cursor != null) { // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)



        } else {
            println("cest nul")
            return null
        }

    }

    fun initDialog() {
        pDialog = ProgressDialog(this)
        pDialog!!.setMessage("test")
        pDialog!!.setCancelable(true)
    }


    protected fun showpDialog() {
        if (!pDialog!!.isShowing) pDialog!!.show()
    }

    protected fun hidepDialog() {
        if (pDialog!!.isShowing) pDialog!!.dismiss()
    }
    fun PostVideoToApi(){

        var user : ArrayList<Int> = ArrayList()

        // user.add(1)
       // var attach = Attachment("video","corona.jpg","video",pathVideo,"2020-06-19T13:20:20.155162Z")
      //  var video_cas = video(attach,"2020-06-19T13:20:20.155162Z",false,"video sur corona","video pour test",1)
        var path = getPath(video!!)
        println("le path est " + path)
        // Map is used to multipart the file using okhttp3.RequestBody
        //val map = ArrayList<RequestBody>()
        var map =  HashMap<String, RequestBody>()
        val file = File(path)

       var upload_preset = RequestBody.create("text/plain".toMediaTypeOrNull(),
                                       "hwhrascg")

        // Parsing any Media type file
        var requestBody = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
        val vFile = MultipartBody.Part.createFormData("file", file.getName(), requestBody)
        // map.add(requestBody)
        map.put("file\"; filename=\"" + file.getName() + "\"", requestBody)
       // var test = videoTest("hwhrascg",pathVideo)

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
       // val getResponse = AppConfig.retrofit.create<APIService>(APIService::class.java!!)
     //   val call = getResponse.
        val call = service.uploadVideoTest(upload_preset,vFile)


        call!!.enqueue(object : Callback<ServerResponse?> {
            override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {
                Log.e("hhh", "Unable to submit post to API." + t.message)//To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<ServerResponse?>, response: Response<ServerResponse?>) {
                if (response.isSuccessful()) {
                    println(response.body()!!.toString())
                    Log.i( "hhh","post submitted to API." + response.body()!!.toString())
                }
            }
        })
    }
    }