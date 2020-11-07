package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.VisitedDoctorsListItemBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.AppointmentResponse
import com.telehealthmanager.app.ui.activity.visitedDoctor.VisitedDoctorsDetailActivity
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class PreviousAppointmentsListAdapter(
    val items: MutableList<AppointmentResponse.Previous.Appointment>,
    val context: Context,
    val listener: IAppointmentListener
) :
    RecyclerView.Adapter<PreviousAppointmentsViewHolder>() {

    override fun onBindViewHolder(holder: PreviousAppointmentsViewHolder, position: Int) {
        val item = items!![position]
        holder.itemBinding.upcomingDate?.text = ViewUtils.getDayFormat(item.scheduled_at)
        holder.itemBinding.upcomingTime?.text = ViewUtils.getTimeFormat(item.scheduled_at)
        if (item.hospital != null) {
            holder.itemBinding.upcomingDoctorName?.text =
                item.hospital.first_name.plus(" ").plus(item.hospital.last_name)
            holder.itemBinding.upcomingHospitalName?.text =
                item.hospital.clinic?.name.plus(",").plus(item.hospital.clinic?.address)
        }
        holder.itemBinding.textView28?.text = item.status?.toLowerCase().capitalize()
        if (item.status.equals("CANCELLED", true)) {
            holder.itemBinding.textView28?.setTextColor(context.resources.getColor(R.color.colorRed))
            holder.itemBinding.textView28?.setBackgroundColor(context.resources.getColor(R.color.colorLiteRed))


        }
        holder.itemView.setOnClickListener {
            listener.onitemclick(item)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PreviousAppointmentsViewHolder {
        val inflate = DataBindingUtil.inflate<VisitedDoctorsListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.visited_doctors_list_item, parent, false
        )
        return PreviousAppointmentsViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    interface IAppointmentListener {
        fun onitemclick(selectedItem: AppointmentResponse.Previous.Appointment)
    }
}


class PreviousAppointmentsViewHolder(view: VisitedDoctorsListItemBinding) :
    RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}