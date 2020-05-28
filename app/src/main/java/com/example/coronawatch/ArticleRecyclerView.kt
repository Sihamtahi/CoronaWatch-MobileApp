package com.example.coronawatch

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.example.articles.model.ArticleItem
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.R
import android.graphics.*
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.graphics.RectF
import android.graphics.Shader
import android.graphics.BitmapShader
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable

import android.content.Context
import java.util.*


class ArticleRecyclerAdapter (val article : List<ArticleItem>): RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder>() {

    companion object{
        val TITLE="title"
        val TEXT="texte"
        val ID="id"
        val IMAGE="image"
    }


//

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var shareImage: Button
        var likeImage: Button
        var likeRedImage: Button
        var commentImage: Button
        var hideImage:  Button
        //var avatarImage: ImageView
        var pubImage: ImageView
        var name: TextView
        var time: TextView
       // var contenu: TextView
        var nb_like: TextView
        var nb_comm: TextView


        init {
            shareImage = itemView.findViewById(com.example.article.R.id.share)
            likeImage = itemView.findViewById(com.example.article.R.id.like)
            likeRedImage = itemView.findViewById(com.example.article.R.id.likeRed)
            commentImage = itemView.findViewById(com.example.article.R.id.comment)
            hideImage = itemView.findViewById(com.example.article.R.id.hide)
            pubImage =  itemView.findViewById(com.example.article.R.id.pub_img)
            name =  itemView.findViewById(com.example.article.R.id.name)
            time =  itemView.findViewById(com.example.article.R.id.time)
            nb_like =  itemView.findViewById(com.example.article.R.id.nb_like)
            nb_comm =  itemView.findViewById(com.example.article.R.id.nb_comm)



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

            var cardview: CardView = itemView.findViewById(com.example.article.R.id.store_card)
            cardview.setElevation(0.0F)
            hideImage.setOnClickListener{
                // Build an AlertDialog
                val builder = AlertDialog.Builder(hideImage.context)
                // Set a title for alert dialog
                builder.setTitle(com.example.article.R.string.hide_publication)
                // Ask the final question
                builder.setMessage(com.example.article.R.string.hide_pub)
                // Set click listener for alert dialog buttons
                val dialogClickListener = DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        DialogInterface.BUTTON_POSITIVE -> cardview.visibility = View.INVISIBLE
                        // User clicked the Yes button
                        //tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35)
                        DialogInterface.BUTTON_NEGATIVE -> cardview.visibility = View.VISIBLE
                    }// User clicked the No button
                }
                // Set the alert dialog yes button click listener
                builder.setPositiveButton(com.example.article.R.string.oui, dialogClickListener)

                // Set the alert dialog no button click listener
                builder.setNegativeButton(com.example.article.R.string.non, dialogClickListener)

                val dialog = builder.create()
                // Display the alert dialog on interface
                dialog.show()

            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(com.example.article.R.layout.fragment_article, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ArticleRecyclerAdapter.ViewHolder, position: Int) {
        val item = article.get(position)
        holder.nb_comm.text = "1"
        holder.nb_like.text = "2"
        var local=  Locale( "ar" , "TN" )
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",local)
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm",local)
        val output: String = formatter.format(parser.parse(item.publicationDate))

        val dates = SimpleDateFormat("MM/dd/yyyy")
        val CurrentDate = " 23/05/2020"
        val CurrentDate2 = "24/05/2020"
        val date2 = dates.parse(CurrentDate)
        var difference = Math.abs(date2.getTime() - dates.parse(CurrentDate2).getTime())
        var differenceDates = difference / (24 * 60 * 60 * 1000)

        val numOfDays =  (difference / (1000 * 60 * 60 * 24)).toInt()
        val hours =  (difference / (1000 * 60 * 60)).toInt()
        val minutes = (difference / (1000 * 60)).toInt()
        val seconds = (difference / (1000)).toInt()
        println("date est "+numOfDays.toString()+" "+ hours+ " " +minutes+" "+ seconds )

        holder.time.text =output
        holder.name.text = item.title
        holder.shareImage.setBackgroundResource(com.example.article.R.mipmap.share)
        holder.shareImage.setClipToOutline(true)
        val ImageView = holder.pubImage

        Picasso.with(holder.itemView.context).load(item.attachment.file_url).resize(185*2,278*2).into(ImageView)



        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, Article::class.java)
            intent.putExtra(TITLE,item.title)
            intent.putExtra(TEXT,item.content)
            intent.putExtra("id",item.id)
            intent.putExtra(IMAGE,item.attachment.file_url)
            holder.itemView.context.startActivity(intent)
             }
        val screenshot = ViewShot(holder.itemView)
        holder.shareImage.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.ACTION_VIEW,screenshot)
            holder.shareImage.context.startActivity(Intent.createChooser(intent, "Share to : "))

        }
    }
    override fun getItemCount(): Int {
        return article.size
    }

    fun ViewShot(v: View): Bitmap {
        val height = v.height
        val width = v.width
        val b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(0, 0, v.layoutParams.width, v.layoutParams.height)
        v.draw(c)
        return b
    }
}