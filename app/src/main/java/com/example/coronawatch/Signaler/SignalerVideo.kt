package com.example.coronawatch.Signaler

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.database.Cursor
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.article.R
import com.example.signaler.Attachment
import com.example.signaler.SignalerActivity
import com.example.signaler.videoFeed
import kotlinx.android.synthetic.main.envoyervideo.*
import kotlinx.android.synthetic.main.signaler.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.HashMap
import java.util.concurrent.TimeUnit

class EnvoyerVideo: AppCompatActivity(),View.OnClickListener {

    var size: Int = 0
    private val mMediaUri: Uri? = null
    var encodedString: String = ""
    private var fileUri: Uri? = null

    private var mediaPath: String? = null

    private val btnCapturePicture: Button? = null
    private var mCurrentPosition = 0
    private var mImageFileLocation = ""
    private lateinit var pDialog: ProgressDialog
    private var postPath: String? = null
    private var editTextDesc: EditText? = null
    private  var editTextTitre: EditText? = null
    private val PLAYBACK_TIME = "play_time"
    private var PRIVATE_MODE = 0
    private var PREF_NAME ="coronawatch"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.envoyervideo)

        //getting the toolbar
        var toolbar = findViewById<Toolbar>(R.id.toolbar)

        //setting the title
        toolbar.setTitle(getString(R.string.video))

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

         editTextDesc = findViewById(R.id.description)
         editTextTitre = findViewById<EditText>(R.id.title)
        var previewv = findViewById<VideoView>(R.id.previewv)

        uploadv.setOnClickListener(this)
        pickImagev.setOnClickListener(this)
        exitv.setOnClickListener(this)
        if (savedInstanceState != null) {
            mCurrentPosition = savedInstanceState.getInt(PLAYBACK_TIME)
        }
        // Set up the media controller widget and attach it to the video view.
        val controller = MediaController(this)
        controller.setMediaPlayer(previewv)
        previewv!!.setMediaController(controller)

        initDialog()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.pickImagev ->
            {
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, REQUEST_PICK_VIDEO)
            }
            R.id.uploadv -> {
                    uploadFile()
            }
            R.id.exitv -> {
                postPath = ""
                hide()
            }
        }
    }
    // Uploading Video
    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadFile() {
        if (postPath == null || postPath == "") {
            Toast.makeText(this, "please select a video ", Toast.LENGTH_LONG).show()
            return
        } else {
            showpDialog()
            val file = File(postPath!!)

            var upload_preset = RequestBody.create(
                "text/plain".toMediaTypeOrNull(),
                "hwhrascg")

            // Parsing any Media type file
            var requestBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(),file)
            val vFile = MultipartBody.Part.createFormData("file", file.getName(), requestBody)

            PostVideoToApi(upload_preset,vFile)
        }
    }
    override fun onStop() {
        super.onStop()
        // Media playback takes a lot of resources, so everything should be
        // stopped and released at this time.
        releasePlayer()
    }
    fun releasePlayer() {
        previewv!!.stopPlayback()
    }
    override fun onPause() {
        super.onPause()
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            previewv!!.pause()
        }
    }
    fun initializePlayer(uri: Uri?) {

        if (uri != null) {
            previewv!!.setVideoURI(uri)
        }
        // Listener for onPrepared() event (runs after the media is prepared).
        previewv!!.setOnPreparedListener {

            // Restore saved position, if available.
            if (mCurrentPosition > 0) {
                previewv!!.seekTo(mCurrentPosition)
            } else { // Skipping to 1 shows the first frame of the video.
                previewv!!.seekTo(1)
            }
            // Start playing!
            previewv!!.start()
        }
        // Listener for onCompletion() event (runs after media has finished
// playing).
        previewv!!.setOnCompletionListener {
            Toast.makeText(
                this,
                "video",
                Toast.LENGTH_SHORT
            ).show()
            // Return the video position to the start.
            previewv!!.seekTo(0)
        }
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the current playback position (in milliseconds) to the
        // instance state bundle.
        outState.putInt(PLAYBACK_TIME, previewv!!.currentPosition)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("WrongConstant")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EnvoyerVideo.REQUEST_TAKE_VIDEO || requestCode == EnvoyerVideo.REQUEST_PICK_VIDEO) {
                if (data != null) {
                    // Get the Image from data
                    val selectedVideo = data.data
                    initializePlayer(selectedVideo)
                    val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)

                    val cursor = contentResolver.query(selectedVideo!!, filePathColumn, null, null, null)
                    assert(cursor != null)
                    cursor!!.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
                    // Set the Image in ImageView for Previewing the Media
                    show()
                   // previewv.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                    cursor.close()

                    postPath = mediaPath
                }
            } else if (requestCode == EnvoyerVideo.CAMERA_PIC_REQUEST) {
                if (Build.VERSION.SDK_INT > 21) {

                    Glide.with(this).load(mImageFileLocation).into(preview)
                    postPath = mImageFileLocation

                } else {
                    Glide.with(this).load(fileUri).into(preview)
                    postPath = fileUri!!.path
                }
            }

        } else if (resultCode != Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Sorry, there was an error!", Toast.LENGTH_LONG).show()
        }
    }
    protected fun initDialog() {
        pDialog = ProgressDialog(this)
        pDialog.setMessage(getString(R.string.msg_loading))
        pDialog.setCancelable(true)
    }


    protected fun showpDialog() {
        if (!pDialog.isShowing) pDialog.show()
    }

    protected fun hidepDialog() {
        if (pDialog.isShowing) pDialog.dismiss()
    }

    fun show(){
        previewv.visibility = View.VISIBLE
        exitv.visibility = View.VISIBLE
        pickImagev.visibility= View.GONE
        txtv.visibility=View.GONE
    }

    fun hide(){
        previewv.visibility = View.GONE
        exitv.visibility = View.GONE
        pickImagev.visibility= View.VISIBLE
        txtv.visibility=View.VISIBLE
    }


    fun PostVideoToApi(a: RequestBody, b: MultipartBody.Part){

        showpDialog()
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
        val call = service.uploadVideoTest(a,b)


        call!!.enqueue(object : Callback<ServerResponse?> {
            override fun onFailure(call: Call<ServerResponse?>, t: Throwable) {
                Log.e("hhh", "Unable to submit post to API." + t.message)//To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<ServerResponse?>, response: Response<ServerResponse?>) {
                if (response.isSuccessful()) {
                    hidepDialog()
                    println(response.body()!!.toString())
                    var file =response.body()!!.resource_type+"/"+response.body()!!.type+"/v"+
                            response.body()!!.version+"/"+response.body()!!.public_id +"." +response.body()!!.format

                    println("file_url est : " + file)
                    val message = editTextDesc!!.text.toString()
                    val titre = editTextTitre!!.text.toString()
                        if(message.isEmpty() ){
                            editTextDesc!!.setError("من فضلك أدخل تعليقك")

                        }else{
                            if(title.isEmpty()){
                                editTextTitre!!.setError("من فضلك أدخل تعليقك")
                            }else{
                                uploadVideoFeeds(file,titre,message)
                            }
                        }

                    Log.i( "hhh","post submitted to API." + response.body()!!.toString())
                }
            }
        })

    }
    fun uploadVideoFeeds(pathVideo:String,title:String,descption:String){


        val sharedPrefIdUser: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val iduser = sharedPrefIdUser!!.getInt("id_user",0)

        var attach = Attachment("video","corona.png","video",pathVideo,"2020-06-19T13:16:59.155162Z")
        var video_cas = videoFeed(attach,"2020-06-19T13:20:20.155162Z",false,false,title,descption,iduser)


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
        val call = service.uploadVideoToApi(video_cas)


        call!!.enqueue(object : Callback<videoFeed?> {
            override fun onFailure(call: Call<videoFeed?>, t: Throwable) {
                Log.e("hhh", "Unable to submit video feeds to API." + t.message)
                hidepDialog()
                Toast.makeText(this@EnvoyerVideo,"تم رفض هذه العملية ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()

            }

            override fun onResponse(call: Call<videoFeed?>, response: Response<videoFeed?>) {
                if (response.isSuccessful()) {
                    println("le body est : "+ response.body()!!.toString())
                    hidepDialog()
                    Toast.makeText(this@EnvoyerVideo,"لقد تم تنزيل هذا الفيديو بنجاح ، سوف سيتم نشره بعد تأكيده",Toast.LENGTH_LONG).show()
                    hide()
                }else{
                    println("not success prblm is"+response.body().toString())
                }
            }
        })
    }

    fun getPath(uri: Uri): String? {
        val projection = arrayOf(
            MediaStore.Video.Media.DATA
        )
        val cursor: Cursor? =
            getContentResolver().query(uri, projection, null, null, null)
        if (cursor != null) {

            val column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(column_index)
        } else {
            println("Path nul")
            return null
        }
    }
    companion object {
        private val REQUEST_TAKE_VIDEO = 0
        private val REQUEST_PICK_VIDEO = 2
        private val CAMERA_PIC_REQUEST = 1111 }

}