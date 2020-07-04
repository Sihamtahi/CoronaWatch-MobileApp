package com.example.coronawatch


import android.net.Uri
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.os.Build
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.article.R
import com.example.coronawatch.model.videoFeed
import java.text.SimpleDateFormat
import android.content.Context
import java.util.*
import android.widget.MediaController



class RecyclerAdapterVideoFeed(val video: List<videoFeed>) : RecyclerView.Adapter<RecyclerAdapterVideoFeed.ViewHolder>() {

    private val context: Context? = null
    private var mCurrentPosition = 0
    private var videoView:VideoView ?= null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var pubVideo: VideoView
        var name: TextView
        init {

            pubVideo =  itemView.findViewById(R.id.videoView)
            videoView = itemView.findViewById(R.id.videoView)
            name =  itemView.findViewById(R.id.name)

            var card: CardView = itemView.findViewById(R.id.store_card)
            card.setElevation(0.0F)


        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_youtube, viewGroup, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val item = video.get(i)

        var local=  Locale( "ar" , "TN" )
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",local)
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm",local)
        //val output: String = formatter.format(parser.parse(item.attachment.date))

        if(item.is_validated){
        val mediaController = MediaController(viewHolder.pubVideo.context)
        viewHolder.name.text = item.title
        viewHolder. pubVideo.setVideoURI(Uri.parse(item.attachment.file_url))
        viewHolder. pubVideo.seekTo( 1 )
        viewHolder.pubVideo.setMediaController(mediaController)
        viewHolder.pubVideo.requestFocus()

        }
    }

    override fun getItemCount(): Int {
        return video.size
    }

}