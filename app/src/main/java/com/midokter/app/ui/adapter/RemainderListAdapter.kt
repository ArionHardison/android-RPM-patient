package com.midokter.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*
import kotlinx.android.synthetic.main.remainder_list_item.view.*

class RemainderListAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<RemainderViewHolder>() {

    override fun onBindViewHolder(holder: RemainderViewHolder, position: Int) {
        holder?.remainderName?.text = items.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemainderViewHolder {
        return RemainderViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.remainder_list_item,
                parent,
                false
            )
        )
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class RemainderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val remainderName = view.remainderName
}