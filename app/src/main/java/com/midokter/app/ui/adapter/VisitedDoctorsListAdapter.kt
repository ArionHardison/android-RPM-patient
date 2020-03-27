package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.ui.activity.visitedDoctor.VisitedDoctorsDetailActivity
import kotlinx.android.synthetic.main.visited_doctors_list_item.view.*

class VisitedDoctorsListAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<VisitedDoctorsViewHolder>() {

    override fun onBindViewHolder(holder: VisitedDoctorsViewHolder, position: Int) {
        holder?.tvDoctorName?.text = items.get(position)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, VisitedDoctorsDetailActivity::class.java)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitedDoctorsViewHolder {
        return VisitedDoctorsViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.visited_doctors_list_item,
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

class VisitedDoctorsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvDoctorName = view.textView26
}