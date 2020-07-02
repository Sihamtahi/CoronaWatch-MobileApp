package com.example.coronawatch


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import com.example.article.R
import com.example.articles.model.ArticleItem
import com.example.coronawatch.model.Video
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import java.text.SimpleDateFormat
import java.util.*


class RecyclerAdapter(val video : List<Video>) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        var likeImage: Button
        var likeRedImage: Button
        var pubVideo: YouTubePlayerView
        var name: TextView
        var time: TextView
        var nb_like: TextView
        var nb_comm: TextView


        init {

            likeImage = itemView.findViewById(R.id.like)
            likeRedImage = itemView.findViewById(R.id.likeRed)
            pubVideo =  itemView.findViewById(R.id.pub_img)
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
        val output: String = formatter.format(parser.parse(item.date))
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
      //  viewHolder.likeImage.setBackgroundResource(R.mipmap.path)
        //viewHolder.likeRedImage.setBackgroundResource(R.mipmap.lik)

    }

    override fun getItemCount(): Int {
        return video.size
    }



}