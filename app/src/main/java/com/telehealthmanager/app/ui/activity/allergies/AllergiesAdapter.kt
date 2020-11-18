package com.telehealthmanager.app.ui.activity.allergies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import kotlinx.android.synthetic.main.item_view_allergies.view.*

class AllergiesAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view_allergies, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: String = items.get(position)
        holder.allergiesText?.text = data
    }

    override fun getItemCount(): Int {
        return items.size
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val allergiesText = view.allergiesText
}