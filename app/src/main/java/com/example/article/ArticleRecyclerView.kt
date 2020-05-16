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
import androidx.recyclerview.widget.RecyclerView
import com.example.article.R
import com.example.articles.model.ArticleItem
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat


class ArticleRecyclerAdapter (val article : List<ArticleItem>): RecyclerView.Adapter<ArticleRecyclerAdapter.ViewHolder>() {

    companion object{
        val TITLE="title"
        val TEXT="texte"
        val ID="id"
        val IMAGE="image"
    }
    private val names = arrayOf("يوبا دوايدي", "يوبا دوايدي", "يوبا دوايدي", "يوبا دوايدي", "يوبا دوايدي")
//
//    private val date_pub = arrayOf("8 نوفمبر", "8 نوفمبر", "8 نوفمبر")
//
    private val nbLike = arrayOf("256", "256", "256", "256", "256","256", "256", "256", "256", "256","256","256")
//
    private val nbComment = arrayOf("24", "24", "24","24","24","24", "24", "24","24","24","24", "24", "24","24","24")
//
//    private val contenu_pub = arrayOf(" تسجيل 119 حالة جديدة مصابة بفيروس كورونا المستجد", " تسجيل 119 حالة جديدة مصابة بفيروس كورونا المستجد",
//        " تسجيل 119 حالة جديدة مصابة بفيروس كورونا المستجد ")
//
    private val images_avatar = intArrayOf(R.mipmap.avatar, R.mipmap.avatar,R.mipmap.avatar,R.mipmap.avatar,R.mipmap.avatar)

    private val images_pub = intArrayOf(R.mipmap.img, R.mipmap.img,R.mipmap.img,R.mipmap.img,R.mipmap.img, R.mipmap.img,R.mipmap.img,R.mipmap.img,R.mipmap.img, R.mipmap.img,R.mipmap.img,R.mipmap.img,R.mipmap.img)

    private val images_likes = intArrayOf(R.mipmap.path, R.mipmap.path, R.mipmap.path, R.mipmap.path,R.mipmap.path,R.mipmap.path, R.mipmap.path, R.mipmap.path, R.mipmap.path,R.mipmap.path,R.mipmap.path, R.mipmap.path, R.mipmap.path, R.mipmap.path,R.mipmap.path)
    private val images_likes_red = intArrayOf(R.mipmap.lik, R.mipmap.lik, R.mipmap.lik, R.mipmap.lik,R.mipmap.lik, R.mipmap.lik, R.mipmap.lik, R.mipmap.lik,R.mipmap.lik, R.mipmap.lik, R.mipmap.lik, R.mipmap.lik,R.mipmap.lik)

    private val images_comments = intArrayOf(R.mipmap.com, R.mipmap.com, R.mipmap.com, R.mipmap.com,R.mipmap.com, R.mipmap.com, R.mipmap.com, R.mipmap.com,R.mipmap.com, R.mipmap.com, R.mipmap.com, R.mipmap.com,R.mipmap.com)

    private val images_share = intArrayOf(R.mipmap.share, R.mipmap.share, R.mipmap.share, R.mipmap.share,R.mipmap.share, R.mipmap.share, R.mipmap.share, R.mipmap.share,R.mipmap.share, R.mipmap.share, R.mipmap.share, R.mipmap.share,R.mipmap.share)

    private val images_supp = intArrayOf(R.mipmap.inf, R.mipmap.inf, R.mipmap.inf, R.mipmap.inf,R.mipmap.inf, R.mipmap.inf, R.mipmap.inf, R.mipmap.inf,R.mipmap.inf, R.mipmap.inf, R.mipmap.inf, R.mipmap.inf,R.mipmap.inf)
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
            shareImage = itemView.findViewById(R.id.share)
            likeImage = itemView.findViewById(R.id.like)
            likeRedImage = itemView.findViewById(R.id.likeRed)
            commentImage = itemView.findViewById(R.id.comment)
            hideImage = itemView.findViewById(R.id.hide)
            //avatarImage = itemView.findViewById(R.id.avatar)
            pubImage =  itemView.findViewById(R.id.pub_img)
            name =  itemView.findViewById(R.id.name)
            time =  itemView.findViewById(R.id.time)
            //contenu =  itemView.findViewById(R.id.text_pub)
            nb_like =  itemView.findViewById(R.id.nb_like)
            nb_comm =  itemView.findViewById(R.id.nb_comm)

            shareImage.setOnClickListener{
                val intent = Intent()
                intent.action = Intent.ACTION_SEND
                intent.putExtra(Intent.EXTRA_TEXT, "Here is the share content body")
                intent.type = "text/plain"
                shareImage.context.startActivity(Intent.createChooser(intent, "Share to : "))

            }

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
            hideImage.setOnClickListener{
                // Build an AlertDialog
                val builder = AlertDialog.Builder(hideImage.context)
                // Set a title for alert dialog
                builder.setTitle(R.string.hide_publication)
                // Ask the final question
                builder.setMessage(R.string.hide_pub)
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
                builder.setPositiveButton(R.string.oui, dialogClickListener)

                // Set the alert dialog no button click listener
                builder.setNegativeButton(R.string.non, dialogClickListener)

                val dialog = builder.create()
                // Display the alert dialog on interface
                dialog.show()

            }
            //Commneter une publication
            var commentaire: EditText = itemView.findViewById(R.id.ComText)
            commentImage.setOnClickListener{

                val intent = Intent(commentImage.context,ArticlePage::class.java)
                intent.putExtra("nb_Comment",nb_comm.text)
                intent.putExtra("nb_Like",nb_like.text)
                //intent.putExtra("contenu",contenu.text)
                intent.putExtra("name",name.text)
                intent.putExtra("time",time.text)
                intent.putExtra("pubImage",R.mipmap.img)
                intent.putExtra("avatarImage",R.mipmap.avatar)
                intent.putExtra("hideImage",R.mipmap.inf)
                intent.putExtra("commentImage",R.mipmap.com)
                intent.putExtra("likeImage",R.mipmap.path)
                intent.putExtra("shareImage",R.mipmap.share)
                commentImage.context.startActivity(intent)

            }
        }
    }

    /*override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.article, viewGroup, false)
        return ViewHolder(v)
    }*/
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_article, parent, false)
        return ViewHolder(v)
    }


    override fun onBindViewHolder(holder: ArticleRecyclerAdapter.ViewHolder, position: Int) {
        val item = article.get(position)
        //holder?.view?.textView_video_title?.text = item.title

        holder.nb_comm.text = "24"
        holder.nb_like.text = "256"
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm")
        val output: String = formatter.format(parser.parse(item.publicationDate))
        holder.time.text =output
        holder.name.text = item.title
        holder.shareImage.setBackgroundResource(R.mipmap.share)
        val ImageView = holder.pubImage

        Picasso.with(holder.itemView.context).load(item.attachment.file).resize(185*2,278*2).into(ImageView)


        holder.likeImage.setBackgroundResource(R.mipmap.path)
        holder.commentImage.setBackgroundResource(R.mipmap.com)
        holder.hideImage.setBackgroundResource(images_supp[position])
        holder.likeRedImage.setBackgroundResource(R.mipmap.lik)

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, com.example.coronawatch.Article::class.java)
            intent.putExtra(TITLE,item.title)
            intent.putExtra(TEXT,item.content)
            intent.putExtra("id",item.id)
            intent.putExtra(IMAGE,item.attachment.file)
            holder.itemView.context.startActivity(intent)
             }

    }

    override fun getItemCount(): Int {
        return article.size
    }


}