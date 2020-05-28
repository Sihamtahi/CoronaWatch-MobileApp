package com.example.coronawatch

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.example.article.R
import com.example.coronawatch.ArticleRecyclerAdapter.Companion.IMAGE
import com.example.coronawatch.ArticleRecyclerAdapter.Companion.TEXT
import com.example.coronawatch.ArticleRecyclerAdapter.Companion.TITLE
import com.example.model.Comments
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.article_activity.*
import okhttp3.*
import java.io.IOException


class Article : AppCompatActivity() {

    lateinit var adapter:ArrayAdapter<String>
    lateinit var itemlist:ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.article_activity)
        itemlist= arrayListOf()
         adapter = ArrayAdapter(this, R.layout.aligned_right, itemlist)

        val intent = intent
        val articleItem:String = intent.getStringExtra(TITLE)
        text_pub.text=articleItem
        text.setMovementMethod(ScrollingMovementMethod())
        supportActionBar?.title= articleItem

        Picasso.with(applicationContext).load(intent.getStringExtra(IMAGE)).resize(600*2,278*2).into(pub_img)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text.setText(Html.fromHtml(intent.getStringExtra(TEXT), Html.FROM_HTML_MODE_COMPACT))
        } else {
            text.setText(Html.fromHtml(intent.getStringExtra(TEXT)))
        }

        fetchJson()
        send.setOnClickListener{
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

    fun fetchJson() {
        println("Attempting to Fetch JSON")

        var artcileid:String = intent.getIntExtra("id",-1).toString()
        val url = "http://corona-watch-api.herokuapp.com/corona-watch-api/v1/feeds/"+artcileid+"/comments/"

         println(url)

        val request = Request.Builder().url(url)
            .header("Authorization","Basic YWRtaW46YWRtaW4=").build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()
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
}
