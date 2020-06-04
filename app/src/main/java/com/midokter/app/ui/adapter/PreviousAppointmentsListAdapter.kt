package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.databinding.VisitedDoctorsListItemBinding
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*
import kotlinx.android.synthetic.main.list_item_finddoctor_categories.view.*
import kotlinx.android.synthetic.main.visited_doctors_list_item.view.*

class PreviousAppointmentsListAdapter(val items: MutableList<AppointmentResponse.Previous.Appointment>, val context: Context) :
    RecyclerView.Adapter<PreviousAppointmentsViewHolder>() {

    override fun onBindViewHolder(holder: PreviousAppointmentsViewHolder, position: Int) {
        val item=items!![position]
        holder.itemBinding.textView24?.text = ViewUtils.getDayFormat(item.scheduled_at)
        holder.itemBinding.textView25?.text =  ViewUtils.getTimeFormat(item.scheduled_at)
        holder.itemBinding.textView26?.text = item.hospital!!.first_name
        holder.itemBinding.textView28?.text = item.status
        holder.itemView.setOnClickListener {
            val intent = Intent(context, FindDoctorsListActivity::class.java)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreviousAppointmentsViewHolder {
        val inflate = DataBindingUtil.inflate<VisitedDoctorsListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.visited_doctors_list_item, parent, false)
        return PreviousAppointmentsViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class PreviousAppointmentsViewHolder(view: VisitedDoctorsListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}