package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.databinding.ListItemPhotosBinding
import com.midokter.app.databinding.ServiceListItemBinding
import com.midokter.app.databinding.VisitedDoctorsListItemBinding
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import com.midokter.app.ui.activity.visitedDoctor.VisitedDoctorsDetailActivity
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*
import kotlinx.android.synthetic.main.list_item_finddoctor_categories.view.*
import kotlinx.android.synthetic.main.visited_doctors_list_item.view.*
import java.io.Serializable

class Doctors_serviceAdapter(val items: MutableList<DoctorListResponse.specialities.DoctorProfile.Hospital.DoctorService>, val context: Context) :
    RecyclerView.Adapter<ServiceViewHolder>() {
    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val item=items!![position]

        holder.itemBinding.textView109?.text = item.service?.name
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val inflate = DataBindingUtil.inflate<ServiceListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.service_list_item, parent, false)
        return ServiceViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class ServiceViewHolder(view: ServiceListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}