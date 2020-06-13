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
import com.midokter.app.repositary.WebApiConstants
import com.midokter.app.repositary.model.MainResponse
import com.midokter.app.ui.activity.visitedDoctor.VisitedDoctorsDetailActivity
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.visited_doctors_list_item.view.*
import java.io.Serializable

class VisitedDoctorsListAdapter(val items: MutableList<MainResponse.VisitedDoctor>, val context: Context) :
    RecyclerView.Adapter<VisitedDoctorsViewHolder>() {

    override fun onBindViewHolder(holder: VisitedDoctorsViewHolder, position: Int) {
        val item=items!![position]
        holder.itemBinding.textView24?.text = ViewUtils.getDayFormat(item.scheduled_at)
        holder.itemBinding.textView25?.text =  ViewUtils.getTimeFormat(item.scheduled_at)
        holder.itemBinding.textView26?.text = item.hospital!!.first_name.plus(" ").plus(item.hospital!!.last_name)
        holder.itemBinding.textView27?.text = item.hospital!!.clinic!!.name.plus(",").plus(item.hospital!!.clinic!!.address)
        holder.itemBinding.textView28?.text = item.status
        if (item.status.equals("CANCELLED",true)){
            holder.itemBinding.textView28?.setTextColor(context.resources.getColor(R.color.colorRed))
            holder.itemBinding.textView28?.setBackgroundColor(context.resources.getColor(R.color.colorLiteRed))


        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, VisitedDoctorsDetailActivity::class.java)
            intent.putExtra(WebApiConstants.IntentPass.iscancel,false)
            intent.putExtra(WebApiConstants.IntentPass.VisitedDoctor,item as Serializable)
            context.startActivity(intent);
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VisitedDoctorsViewHolder {

        val inflate = DataBindingUtil.inflate<VisitedDoctorsListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.visited_doctors_list_item, parent, false)
        return VisitedDoctorsViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class VisitedDoctorsViewHolder(view: VisitedDoctorsListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}