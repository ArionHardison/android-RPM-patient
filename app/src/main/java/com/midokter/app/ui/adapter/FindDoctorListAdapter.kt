package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.ui.activity.findDoctors.FindDoctorBookingActivity
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*
import kotlinx.android.synthetic.main.find_doctor_list_item.view.*
import kotlinx.android.synthetic.main.finddoctor_categories_list_item.view.*

class FindDoctorListAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<FindDoctorViewHolder>() {

    override fun onBindViewHolder(holder: FindDoctorViewHolder, position: Int) {
        holder?.tvDoctorName?.text = items.get(position)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, FindDoctorBookingActivity::class.java)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindDoctorViewHolder {
        return FindDoctorViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.find_doctor_list_item,
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

class FindDoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvDoctorName = view.textView47
}