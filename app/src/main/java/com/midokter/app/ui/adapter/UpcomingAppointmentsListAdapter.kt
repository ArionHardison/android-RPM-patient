package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.databinding.UpcomingListItemBinding
import com.midokter.app.repositary.model.AppointmentResponse
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*
import kotlinx.android.synthetic.main.list_item_finddoctor_categories.view.*
import kotlinx.android.synthetic.main.upcoming_list_item.view.*

class UpcomingAppointmentsListAdapter(val items: MutableList<AppointmentResponse.Upcomming.Appointment>, val context: Context,val listener:IAppointmentListener) :
    RecyclerView.Adapter<UpcomingAppointmentsViewHolder>() {

    override fun onBindViewHolder(holder: UpcomingAppointmentsViewHolder, position: Int) {
        val item=items!![position]

        holder.itemBinding.upcomingDate?.text = ViewUtils.getDayFormat(item.scheduled_at)
        holder.itemBinding.upcomingTime?.text =  ViewUtils.getTimeFormat(item.scheduled_at)
        holder.itemBinding.upcomingDoctorName?.text = item.hospital.email
        holder.itemBinding.upcomingHospitalName?.text = item.hospital.first_name.plus(" ").plus(item.hospital.last_name)
holder.itemBinding.upcomingHospitalName?.text = item.hospital.clinic?.name.plus(",").plus(item.hospital.clinic?.address)

        holder.itemView.setOnClickListener {
          listener.onitemclick(item)
        }
        holder.itemBinding.textView28.setOnClickListener {
            listener.onCancelclick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingAppointmentsViewHolder {

        val inflate = DataBindingUtil.inflate<UpcomingListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.upcoming_list_item, parent, false)
        return UpcomingAppointmentsViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}
interface IAppointmentListener{

    fun onitemclick(selectedItem:AppointmentResponse.Upcomming.Appointment)
    fun onCancelclick(selectedItem:AppointmentResponse.Upcomming.Appointment)
}
class UpcomingAppointmentsViewHolder(view: UpcomingListItemBinding) : RecyclerView.ViewHolder(view.root) {

    val itemBinding = view
}