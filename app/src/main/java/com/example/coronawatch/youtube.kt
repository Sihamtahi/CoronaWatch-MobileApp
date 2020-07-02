package com.example.coronawatch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.*
import com.example.article.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coronawatch.model.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fb.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException


class youtube : Fragment() {


    private var progress_bar: ProgressBar? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fb, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        layoutManager = LinearLayoutManager(activity!!)
        progress_bar = view.findViewById(R.id.progress_bar)
        recycler_view.layoutManager = layoutManager

         GetVideos()


    }

    fun GetVideos () {
        var videos = ArrayList<Video>()
        val API_LINK_VIDEOS ="http://corona-watch-api.herokuapp.com/corona-watch-api/v1/scrapers/youtube/?validated=true"
        val API_HEADER_KEY= getString(R.string.auth)
        val API_HEADRER_VALUE=getString(R.string.token)

        val request = Request.Builder()
            .url(API_LINK_VIDEOS)
            .header(API_HEADER_KEY, API_HEADRER_VALUE)
            .build()



        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {

                getActivity()?.runOnUiThread {
                    Toast.makeText(
                        context,
                        "خطأ أثناء تحميل البيانات من واجهة برمجة التطبيقات ، سرعة الاتصال الخاصة بك غير كافية لتحميل البيانات",
                        Toast.LENGTH_LONG
                    ).show()
                    progress_bar?.visibility = View.GONE
                }

            }

            override fun onResponse(call: Call, response: Response) {

                response.use {
                    if (!response.isSuccessful)
                    {

                        getActivity()?.runOnUiThread {
                            Toast.makeText(
                                context,
                                "خطأ أثناء تحميل البيانات من واجهة برمجة التطبيقات ، سرعة الاتصال الخاصة بك غير كافية لتحميل البيانات",
                                Toast.LENGTH_LONG
                            ).show()
                            progress_bar?.visibility = View.GONE
                        }

                    }
                    else {
                        var str_response = response.body!!.string()
                        val gson = Gson()
                        val listVideos = object : TypeToken<List<Video>>() {}.type
                        videos = gson.fromJson(str_response, listVideos)

                        getActivity()?.runOnUiThread {
                            progress_bar?.visibility = View.GONE
                            adapter = RecyclerAdapter(videos)
                            recycler_view.adapter = adapter
                        }
                    }
                }
            }
        })

    }
}
