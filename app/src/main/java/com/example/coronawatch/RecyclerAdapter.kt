package com.example.coronawatch

import android.content.Intent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import android.view.*
import android.widget.Button
import android.widget.TextView
import kotlinx.android.synthetic.main.fragment_facebook.*


class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private val names = arrayOf("يوبا دوايدي", "يوبا دوايدي", "يوبا دوايدي")

    private val date_pub = arrayOf("8 نوفمبر", "8 نوفمبر", "8 نوفمبر")

    private val nbLike = arrayOf("256", "256", "256")

    private val nbComment = arrayOf("24", "24", "24")

    private val contenu_pub = arrayOf(" تسجيل 119 حالة جديدة مصابة بفيروس كورونا المستجد", " تسجيل 119 حالة جديدة مصابة بفيروس كورونا المستجد",
        " تسجيل 119 حالة جديدة مصابة بفيروس كورونا المستجد ")

    private val images_avatar = intArrayOf(R.mipmap.avatar, R.mipmap.avatar,R.mipmap.avatar)

    private val images_pub = intArrayOf(R.mipmap.img, R.mipmap.img,R.mipmap.img)

    private val images_likes = intArrayOf(R.mipmap.path, R.mipmap.path,R.mipmap.path)

    private val images_comments = intArrayOf(R.mipmap.com, R.mipmap.com,R.mipmap.com)

    private val images_share = intArrayOf(R.mipmap.share, R.mipmap.share,R.mipmap.share)

    private val images_supp = intArrayOf(R.mipmap.inf, R.mipmap.inf,R.mipmap.inf)


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var shareImage: Button
        var likeImage: Button
        var commentImage: Button
        var hideImage:  Button
        var avatarImage: ImageView
        var pubImage: ImageView
        var name: TextView
        var time: TextView
        var contenu: TextView
        var nb_like: TextView
        var nb_comm: TextView


        init {
            shareImage = itemView.findViewById(R.id.share)
            likeImage = itemView.findViewById(R.id.like)
            commentImage = itemView.findViewById(R.id.comment)
            hideImage = itemView.findViewById(R.id.hide)
            avatarImage = itemView.findViewById(R.id.avatar)
            pubImage =  itemView.findViewById(R.id.pub_img)
            name =  itemView.findViewById(R.id.name)
            time =  itemView.findViewById(R.id.time)
            contenu =  itemView.findViewById(R.id.text_pub)
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
                likeImage.setBackgroundResource(R.mipmap.lik)
                nb_like.text = ( nb_like.text.toString().toInt() + 1).toString()
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.fragment_facebook, viewGroup, false)
        return ViewHolder(v)
    }
    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.nb_comm.text = nbComment[i]
        viewHolder.nb_like.text = nbLike[i]
        viewHolder.contenu.text = contenu_pub[i]
        viewHolder.time.text = date_pub[i]
        viewHolder.name.text = names[i]
        viewHolder.shareImage.setBackgroundResource(images_share[i])
        viewHolder.pubImage.setImageResource(images_pub[i])
        viewHolder.likeImage.setBackgroundResource(images_likes[i])
        viewHolder.commentImage.setBackgroundResource(images_comments[i])
        viewHolder.hideImage.setBackgroundResource(images_supp[i])
        viewHolder.avatarImage.setImageResource(images_avatar[i])


    }

    override fun getItemCount(): Int {
        return names.size
    }


}