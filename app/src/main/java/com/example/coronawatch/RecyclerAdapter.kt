package com.example.coronawatch


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.article.R
import com.example.coronawatch.model.Video
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.text.SimpleDateFormat
import java.util.*


class RecyclerAdapter(val video : List<Video>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        var pubVideo: YouTubePlayerView
        var name: TextView
        var time: TextView



        init {


            pubVideo =  itemView.findViewById(R.id.pub_img)
            name =  itemView.findViewById(R.id.name)
            time =  itemView.findViewById(R.id.time)



            var card: CardView = itemView.findViewById(R.id.store_card)

            card.setElevation(0.0F)


        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_facebook, viewGroup, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {

        val item = video.get(i)

      //  val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        //val output: String = formatter.format(parser.parse(item.date))


        var local=  Locale( "ar" , "TN" )
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",local)
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm",local)
        val output: String = formatter.format(parser.parse(item.published_at))
        viewHolder.time.text = output
        viewHolder.name.text = item.title
        /******************loader la video*********************/
        //lifecycle.addObserver(youTubePlayerView2)

        viewHolder.pubVideo.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer)
            {

                youTubePlayer.cueVideo(item.video_id, 0f)

            }
        })
    }

    override fun getItemCount(): Int {
        return video.size
    }



}