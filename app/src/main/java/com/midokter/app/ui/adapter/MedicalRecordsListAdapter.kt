package com.midokter.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import kotlinx.android.synthetic.main.home_list_item.view.*
import kotlinx.android.synthetic.main.medical_records_list_item.view.*
import kotlinx.android.synthetic.main.online_appointments_list_item.view.*

class MedicalRecordsListAdapter(val items: ArrayList<String>, val context: Context) :
    RecyclerView.Adapter<MedicalRecordsViewHolder>() {

    override fun onBindViewHolder(holder: MedicalRecordsViewHolder, position: Int) {
        holder?.tvMrDoctorName?.text = items.get(position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicalRecordsViewHolder {
        return MedicalRecordsViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.medical_records_list_item,
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

class MedicalRecordsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    // Holds the TextView that will add each animal to
    val tvMrDoctorName = view.mrDoctorName
}