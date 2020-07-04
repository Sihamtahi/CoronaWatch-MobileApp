package com.example.signaler


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.example.article.R
import com.example.coronawatch.Signaler.APIService
import com.example.coronawatch.Signaler.EnvoyerVideo
import com.example.coronawatch.Signaler.SignalerCasVideo
import com.example.coronawatch.Signaler.reponsecloud
import kotlinx.android.synthetic.main.signaler.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit


class SignalerActivity: AppCompatActivity(), View.OnClickListener {
    var spinner_pos: Int = 0
    var size : Int = 0
    private val mMediaUri: Uri? = null
    var  encodedString :String=""
    private var fileUri: Uri? = null

    private var mediaPath: String? = null

    private val btnCapturePicture: Button? = null

    private var mImageFileLocation = ""
    private lateinit var pDialog: ProgressDialog
    private var postPath: String? = null
    private var PRIVATE_MODE = 0
    private var PREF_NAME ="coronawatch"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signaler)

        //getting the toolbar
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        var video = findViewById<Button>(R.id.uploadvideo)
        //setting the title
        toolbar.setTitle(getString(R.string.signaler_cas))

        video.setOnClickListener{
            var intent = Intent(this, SignalerCasVideo::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        upload.setOnClickListener(this)
        pickImage.setOnClickListener(this)
        exit.setOnClickListener(this)
        initDialog()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onClick(v: View) {
        when (v.id) {
            R.id.pickImage ->
            {
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent, REQUEST_PICK_PHOTO)
            }


            R.id.upload -> {
                spinner_pos = spinner.getSelectedItemPosition()
                val size_values:Array<String> =
                    resources.getStringArray(R.array.wilaya_values)
                size = Integer.valueOf(size_values[spinner_pos])

                if(size==0){
                    val builder = AlertDialog.Builder(upload.context)
                    // Set a title for alert dialog
                    builder.setTitle(R.string.error)
                    // Ask the final question
                    builder.setMessage(R.string.wilaya)

                    val dialog = builder.create()
                    // Display the alert dialog on interface
                    dialog.show()
                }
                else{
                    uploadFile()
                  }
                }
            R.id.exit -> {
                postPath = ""
               hide()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("WrongConstant")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO || requestCode == REQUEST_PICK_PHOTO) {
                if (data != null) {
                    // Get the Image from data
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)

                    val cursor = contentResolver.query(selectedImage!!, filePathColumn, null, null, null)
                    assert(cursor != null)
                    cursor!!.moveToFirst()

                    val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                    mediaPath = cursor.getString(columnIndex)
                    // Set the Image in ImageView for Previewing the Media
                    show()
                    preview.setImageBitmap(BitmapFactory.decodeFile(mediaPath))
                    cursor.close()

                    postPath = mediaPath
                }
            } else if (requestCode == CAMERA_PIC_REQUEST) {
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
        preview.visibility = View.VISIBLE
        exit.visibility = View.VISIBLE
        pickImage.visibility= View.GONE
        txt.visibility=View.GONE
    }

    fun hide(){
        preview.visibility = View.GONE
        exit.visibility = View.GONE
        pickImage.visibility= View.VISIBLE
        txt.visibility=View.VISIBLE
    }

    // Uploading Image/Video
    @RequiresApi(Build.VERSION_CODES.O)
    private fun uploadFile() {
        if (postPath == null || postPath == "") {
            Toast.makeText(this, "please select an image ", Toast.LENGTH_LONG).show()
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


            PostDataToApi(upload_preset,vFile)
        }
    }

    fun PostDataToApi(a: RequestBody, b: MultipartBody.Part){

        showpDialog()
        var user : ArrayList<Int> = ArrayList()
        val sharedPrefIdUser: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        val iduser = sharedPrefIdUser!!.getInt("id_user",0)
        user.add(iduser)

        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient().newBuilder()
            .retryOnConnectionFailure(true)
            .connectTimeout(180, TimeUnit.SECONDS)
            .readTimeout(180, TimeUnit.SECONDS)
            .writeTimeout(180, TimeUnit.SECONDS)

        okHttpClient.addInterceptor(loggingInterceptor).build()
        okHttpClient.retryOnConnectionFailure(true)

        okHttpClient.addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Accept", "application/json")
                .addHeader("Connection", "close")
                .build()
            chain.proceed(request)
        }

        val BaseUrlcc = "https://api.cloudinary.com/"
        val retrofitcc = Retrofit.Builder()
            .baseUrl(BaseUrlcc)
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val servicecc = retrofitcc.create(APIService::class.java)
        val callcc = servicecc.uploadIMAGE(a,b)


        callcc!!.enqueue(object : Callback<reponsecloud?> {
            override fun onFailure(call: Call<reponsecloud?>, t: Throwable) {
                Toast.makeText(this@SignalerActivity,"تم رفض هذه العملية ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<reponsecloud?>, response: Response<reponsecloud?>) {
                if (response.isSuccessful()) {
                    val BaseUrl = "http://corona-watch-api.herokuapp.com/"
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BaseUrl)
                        .client(okHttpClient.build())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                    var attach = Attachment("image","corona.png","image",response.body()!!.url,"2020-06-19T13:16:59.155162Z")
                    var cas_suspect =  suspected(attach,"2020-06-19T13:16:59.155162Z",false,size,user)

                    val service = retrofit.create(APIService::class.java)
                    val call = service.uploadImageToApi(cas_suspect)
                    call!!.enqueue(object : Callback<suspected?> {
                        override fun onFailure(call: Call<suspected?>, t: Throwable) {
                            Log.e("", "Unable to submit post to API." + t.message)
                            hidepDialog()
                            Toast.makeText(this@SignalerActivity,"تم رفض هذه العملية ، يرجى التحقق من اتصالك بالإنترنت",Toast.LENGTH_LONG).show()

                        }
                        override fun onResponse(call: Call<suspected?>, response: Response<suspected?>) {
                            if (response.isSuccessful()) {
                                Log.i( "","post submitted to API." +postPath + response.body()!!.toString())
                                hidepDialog()
                                Toast.makeText(this@SignalerActivity,"لقد تم الإبلاغ عن هذه الحالة، سوف يصلك تنبيه عندما يتم تأكيد الإبلاغ",Toast.LENGTH_LONG).show()
                                hide()
                            }
                        }
                    })
                }
            }
        })
        //hidepDialog()

    }

    companion object {
        //image
        private val REQUEST_TAKE_PHOTO = 0
        private val REQUEST_PICK_PHOTO = 2
        private val CAMERA_PIC_REQUEST = 1111
    }
}