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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.text.SimpleDateFormat
import android.content.Context


class RecyclerAdapterVideoFeed(val video: ArrayList<videoFeed>) : RecyclerView.Adapter<RecyclerAdapterVideoFeed.ViewHolder>() {
    private val context: Context? = null
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var likeImage: Button
        var likeRedImage: Button
        var pubVideo: VideoView
        var name: TextView
        var time: TextView
        var nb_like: TextView
        var nb_comm: TextView


        init {

            likeImage = itemView.findViewById(R.id.like)
            likeRedImage = itemView.findViewById(R.id.likeRed)
            pubVideo =  itemView.findViewById(R.id.videoView)
            name =  itemView.findViewById(R.id.name)
            time =  itemView.findViewById(R.id.time)
            nb_like =  itemView.findViewById(R.id.nb_like)
            nb_comm =  itemView.findViewById(R.id.nb_comm)


            var card: CardView = itemView.findViewById(R.id.store_card)

            card.setElevation(0.0F)

            likeImage.setOnClickListener {

                likeRedImage.visibility = View.VISIBLE
                likeImage.visibility = View.INVISIBLE
                nb_like.text = ( nb_like.text.toString().toInt() + 1).toString()
            }
            likeRedImage.setOnClickListener {

                likeRedImage.visibility = View.INVISIBLE
                likeImage.visibility = View.VISIBLE
                nb_like.text = ( nb_like.text.toString().toInt() - 1).toString()
            }
            var cardview: CardView = itemView.findViewById(R.id.store_card)
            //Commneter une publication
            var commentaire: EditText = itemView.findViewById(R.id.ComText)


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

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        //  val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        //val output: String = formatter.format(parser.parse(item.date))

        val mediaController = MediaController(viewHolder.pubVideo.context)
        viewHolder.time.text = item.publication_date
        viewHolder.name.text = item.title
        viewHolder. pubVideo.setVideoURI(Uri.parse(item.attachment.file_url))
        viewHolder. pubVideo.seekTo( 1 )
        viewHolder.pubVideo.setMediaController(mediaController)
        viewHolder.pubVideo.requestFocus()
        viewHolder.pubVideo.start()

        /******************loader la video*********************/
        //lifecycle.addObserver(youTubePlayerView2)

       /*viewHolder.pubVideo.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer)
            {

                youTubePlayer.cueVideo(item.attachment.file_url, 0f)

            }
        })*/
        //  viewHolder.likeImage.setBackgroundResource(R.mipmap.path)
        //viewHolder.likeRedImage.setBackgroundResource(R.mipmap.lik)

    }

    override fun getItemCount(): Int {
        return video.size
    }



}