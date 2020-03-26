package com.midokter.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*

class FavDoctorListAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<FavDoctorViewHolder>() {

    override fun onBindViewHolder(holder: FavDoctorViewHolder, position: Int) {
        holder?.tvFavDoctorName?.text = items.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavDoctorViewHolder {
        return FavDoctorViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.fav_doctor_list_item,
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

class FavDoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvFavDoctorName = view.favdoctorName
}