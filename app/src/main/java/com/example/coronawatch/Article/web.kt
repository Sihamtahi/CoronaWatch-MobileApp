package com.example.coronawatch.Article


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fb.*
import com.example.article.R
import com.example.articles.model.Article
import com.example.articles.model.ArticleItem
import com.google.gson.GsonBuilder
import okhttp3.*
import java.io.IOException


class web : Fragment() {

    var layoutManager: RecyclerView.LayoutManager? = null
    private var progress_bar: ProgressBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.article_adapter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(activity!!)
        progress_bar = view.findViewById(R.id.progress_bar)
        recycler_view.layoutManager = layoutManager
        fetchJson()
    }

    fun fetchJson() {

        val url = "http://corona-watch-api.herokuapp.com/corona-watch-api/v1/feeds/articles"
        val request = Request.Builder().url(url)
            .header(getString(R.string.auth),getString(R.string.token)).build()
        val client = OkHttpClient()

        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response?.body?.string()

                val gson = GsonBuilder().create()
                val homeFeed: Article = gson.fromJson(body, Article::class.java)
                var filterList: List<ArticleItem> = homeFeed.filter { s -> s.isValidated == true }

                getActivity()?.runOnUiThread {
                    progress_bar?.visibility = View.GONE
                    if(!filterList.isEmpty()){
                    recycler_view.adapter = ArticleRecyclerAdapter(filterList)}
                }
            }
            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }

}
