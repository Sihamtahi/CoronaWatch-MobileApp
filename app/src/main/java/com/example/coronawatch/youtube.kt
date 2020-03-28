package com.example.coronawatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.fragment_facebook.*
import androidx.recyclerview.*
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fb.*

class youtube : Fragment() {

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
        recycler_view.layoutManager = layoutManager

        adapter = RecyclerAdapter()
        recycler_view.adapter = adapter
    }




    }
