package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.RemainderListItemBinding
import com.telehealthmanager.app.repositary.model.ReminderResponse
import com.telehealthmanager.app.utils.CustomClickListener
import com.telehealthmanager.app.utils.ViewUtils

class RemainderListAdapter(val mContext : Context, val list: MutableList<ReminderResponse.Reminder>, val listener: OnReminderClickListener) :
    RecyclerView.Adapter<RemainderViewHolder>() {

    override fun onBindViewHolder(holder: RemainderViewHolder, position: Int) {
        val item = list.get(position)
        holder.mReminderBinding.itemClickListener = object : CustomClickListener {
            override fun onItemClickListener() {
                listener.onReminderClicked(item)
            }
        }

        holder.mReminderBinding.remainderName.text=item.name
        holder.mReminderBinding.dateTxt.text= ViewUtils.getDayAndTimeFormat(String.format("%s %s",item.date,item.time))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RemainderViewHolder {
        val inflate = DataBindingUtil.inflate<RemainderListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.remainder_list_item, parent, false)
        return RemainderViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return list.size
    }
}

interface OnReminderClickListener{
    fun onReminderClicked(item:ReminderResponse.Reminder)
}


class RemainderViewHolder(view : RemainderListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val mReminderBinding = view
}