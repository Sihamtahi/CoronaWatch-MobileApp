package com.example.coronawatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.core.app.ComponentActivity
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.View
import kotlinx.android.synthetic.main.comments.*
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.R.*
import android.widget.*
import android.widget.RelativeLayout.LayoutParams
import android.widget.EditText
import android.widget.ArrayAdapter
import com.example.article.R
import kotlinx.collections.immutable.*
import java.util.*
import kotlin.collections.ArrayList


class Comment : AppCompatActivity() {
    lateinit var shareImage: Button
    lateinit var likeImage: Button
    lateinit var likeRedImage: Button
    lateinit  var commentImage: Button
    lateinit var hideImage:  Button
    lateinit var avatarImage: ImageView
    lateinit var pubImage: ImageView
    lateinit var name: TextView
    lateinit var time: TextView
    lateinit var contenu: TextView
    lateinit var nb_like: TextView
    lateinit var nb_comm: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.comments)
        val actionbar = supportActionBar
        val titleActivit: String = getString(R.string.commentaire)

        nb_comm = findViewById(R.id.nb_comm)
        shareImage = findViewById(R.id.share)
        likeImage = findViewById(R.id.like)
        likeRedImage = findViewById(R.id.likeRed)
        commentImage = findViewById(R.id.comment)
        hideImage = findViewById(R.id.hide)
        avatarImage = findViewById(R.id.avatar)
        pubImage =  findViewById(R.id.pub_img)
        name =  findViewById(R.id.name)
        time =  findViewById(R.id.time)
        contenu =  findViewById(R.id.text_pub)
        nb_like =  findViewById(R.id.nb_like)


        nb_comm.setText(intent.getStringExtra("nb_Comment"))
        nb_like.setText(intent.getStringExtra("nb_Like"))
        time.setText(intent.getStringExtra("time"))
        contenu.setText(intent.getStringExtra("contenu"))
        pubImage.setImageResource(intent.getIntExtra("pubImage",0))
        hideImage.setBackgroundResource(intent.getIntExtra("hideImage",0))
        likeImage.setBackgroundResource(intent.getIntExtra("hideImage",0))
        shareImage.setBackgroundResource(intent.getIntExtra("shareImage",0))
        likeImage.setBackgroundResource(intent.getIntExtra("likeImage",0))
        commentImage.setBackgroundResource(intent.getIntExtra("commentImage",0))
        avatarImage.setImageResource(intent.getIntExtra("avatarImage",0))
       // pubImage.setI


        actionbar!!.title = titleActivit
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        var listview: ListView  = findViewById(R.id.listCom)
       // var ListElements = arrayOf("kkk")
        var ListElementsArrayList = LinkedList<String>()
        val editText = findViewById(R.id.com) as EditText
        val btnSend = findViewById(R.id.send) as Button



       // var ListElementsArrayList = ArrayList<String>(Arrays.asList(ListElements) as List<String>)
        var adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, ListElementsArrayList)

        listview.setAdapter(adapter)

        btnSend.setOnClickListener {
            val message = editText.text.toString()
            if(message.isEmpty()){
                editText.setError("من فضلك أدخل تعليقك")
                //textview.requestFocus()
            }else{
                //textview.setText(message)
                ListElementsArrayList.add(message)
                editText.setText("")
                adapter.notifyDataSetChanged()
            }
        }
        // Suppression de la tâche
        listview.setOnItemClickListener(object : AdapterView.OnItemClickListener {
            override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                //Récuperer la tache qu'on veut supprimer de la liste
                val selectedItem = parent.getItemAtPosition(position) as String
                //Afficher tache faite ensuite la tache sera supprimée de la liste est insérée dans la liste des taches
                //considérée comme faites
               // Toast.makeText(this, "Tache faite", Toast.LENGTH_LONG).show()
                if (!editText.getText().toString().isEmpty()) {
                    ListElementsArrayList.remove(editText.getText().toString())
                    adapter.notifyDataSetChanged()

                }
            }
        })

    }
}
