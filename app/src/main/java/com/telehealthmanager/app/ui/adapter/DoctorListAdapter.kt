package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.ItemDoctorListBinding
import com.telehealthmanager.app.repositary.model.ResponseDoctors
import com.telehealthmanager.app.repositary.model.chatmodel.Chat

class DoctorListAdapter(val list: List<ResponseDoctors.AllDoctors>, val context: Context, val listener: IDocClickListener) :
    RecyclerView.Adapter<DoctorListViewHolder>() {

    override fun onBindViewHolder(holder: DoctorListViewHolder, position: Int) {
        val item = list[position];
        holder.listDoc.docName.text = item.first_name + " " + item.last_name
        holder.itemView.setOnClickListener {
            listener.onDocClicked(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorListViewHolder {
        val inflate = DataBindingUtil.inflate<ItemDoctorListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_doctor_list, parent, false
        )
        return DoctorListViewHolder(inflate)
    }


    override fun getItemCount(): Int {
        return list.size
    }
}

interface IDocClickListener {
    fun onDocClicked(item: ResponseDoctors.AllDoctors)
}

class DoctorListViewHolder(view: ItemDoctorListBinding) : RecyclerView.ViewHolder(view.root) {
    val listDoc = view
}