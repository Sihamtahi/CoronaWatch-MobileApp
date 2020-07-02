package com.example.coronawatch.Article

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.article.R
import com.example.coronawatch.Article.ArticleRecyclerAdapter.Companion.IMAGE
import com.example.coronawatch.Article.ArticleRecyclerAdapter.Companion.TEXT
import com.example.coronawatch.Article.ArticleRecyclerAdapter.Companion.TITLE
import com.example.coronawatch.Login.AuthUser
import com.example.model.Comments
import com.example.model.CommentsItem
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_activity.*
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


class Article : AppCompatActivity() {

    lateinit var adapter:ArrayAdapter<String>
    lateinit var itemlist:ArrayList<String>
    private var PRIVATE_MODE = 0
    private var PREF_NAME ="coronawatch"
    var sendBtn: Button ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_activity)
        itemlist= arrayListOf()
         adapter = ArrayAdapter(this, R.layout.aligned_right, itemlist)

        // ToolBar
        //getting the toolbar
        var toolbar = findViewById<Toolbar>(R.id.toolbar)
        //setting the title
        toolbar.setTitle( getString(R.string.signaler_cas) )


        //placing toolbar in place of actionbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)

        val intent = intent
        val id_article = intent.getIntExtra("id",1)

        val articleItem:String = intent.getStringExtra(TITLE)
        text_pub.text=articleItem
        text.setMovementMethod(ScrollingMovementMethod())
        supportActionBar?.title= articleItem

        sendBtn = findViewById<Button>(R.id.send)
       // Picasso.with(applicationContext).load(intent.getStringExtra(IMAGE)).resize(600*2,278*2).into(pub_img)

        Picasso.get().load(intent.getStringExtra(IMAGE)).resize(600*2,278*2).into(pub_img)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text.setText(Html.fromHtml(intent.getStringExtra(TEXT), Html.FROM_HTML_MODE_COMPACT))
        } else {
            text.setText(Html.fromHtml(intent.getStringExtra(TEXT)))
        }
        // get comments from API
        fetchJson(id_article)
        val sharedPrefIdUser: SharedPreferences = getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        var Islogin = sharedPrefIdUser!!.getBoolean("login_google",false)
        var id_user = sharedPrefIdUser!!.getInt("id_user",1)


        var editText = findViewById<EditText>(R.id.com)

            send.setOnClickListener {
                if(Islogin){
                    val message = editText.text.toString()
                    if(message.isEmpty()){
                        editText.setError("من فضلك أدخل تعليقك")
                        //textview.requestFocus()
                    }else{
                        //textview.setText(message)
                        itemlist.add(message)
                        editText.setText("")
                        adapter.notifyDataSetChanged()

                        //Envoyer le commentaire à la bdd
                        var comm =CommentsItem("",message,1,false,id_article,id_user)
                        EnvoyerCommentaire(comm,id_article)

                    }
                }else{
                    // Build an AlertDialog
                    val builder = AlertDialog.Builder(send.context)
                    // Set a title for alert dialog
                    builder.setTitle(R.string.auto_cmp)
                    // Ask the final question
                    builder.setMessage(R.string.err)
                    val dialog = builder.create()
                    // Display the alert dialog on interface
                    dialog.show()
                }
            }
        like.setOnClickListener {
            Red.visibility = View.VISIBLE
            like.visibility = View.INVISIBLE
            nb_like.text = ( nb_like.text.toString().toInt() + 1).toString()
        }
        Red.setOnClickListener {

            Red.visibility = View.INVISIBLE
            like.visibility = View.VISIBLE
            nb_like.text = ( nb_like.text.toString().toInt() - 1).toString()
        }
        share.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "Here is the share content body")
            intent.type = "text/plain"
            share.context.startActivity(Intent.createChooser(intent, "Share to : "))

        }
    }

    fun fetchJson(id:Int) {

        val url = "http://corona-watch-api.herokuapp.com/corona-watch-api/v1/feeds/"+id+"/comments/"
         println(url)
        val API_HEADER_KEY= getString(R.string.auth)
        val API_HEADRER_VALUE=getString(R.string.token)
        val request = Request.Builder().url(url)
            .header(API_HEADER_KEY,API_HEADRER_VALUE).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body!!.string()
                print("le comm est : "+body)
                val gson = GsonBuilder().create()
                var comments: Comments = gson.fromJson(body, Comments::class.java)
               for (item in comments){
                   itemlist.add(item.content)

               }
                runOnUiThread {
                listCom.adapter =  adapter
                adapter.notifyDataSetChanged()
                 nb_comm.text= comments.size.toString()
                }

            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }
    //getting back to listview
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    fun EnvoyerCommentaire(commentaire: CommentsItem,id_article:Int){
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
        val call = service.sendComment(commentaire,id_article)


        call!!.enqueue(object : retrofit2.Callback<CommentsItem?> {
            override fun onFailure(call: retrofit2.Call<CommentsItem?>, t: Throwable) {
                Toast.makeText(this@Article,"تم رفض هذه العملية ، يرجى التحقق من اتصالك بالإنترنت", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: retrofit2.Call<CommentsItem?>, response: retrofit2.Response<CommentsItem?>) {
                if (response.isSuccessful()) {
                    println(response.body()!!.toString())

                }else{
                    Toast.makeText(this@Article,"تم رفض هذه العملية ، يرجى التحقق من اتصالك بالإنترنت", Toast.LENGTH_LONG).show()

                }
            }
        })
    }
}
