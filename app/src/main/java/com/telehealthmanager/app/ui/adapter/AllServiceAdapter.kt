package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.ItemServiceListBinding
import com.telehealthmanager.app.repositary.model.Hospital

class AllServiceAdapter(val mContext: Context, val list: MutableList<Hospital.DoctorService>) :
    RecyclerView.Adapter<AllServiceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = DataBindingUtil.inflate<ItemServiceListBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_service_list, parent, false
        )
        return ViewHolder(inflate)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mList = list[position]
        initUI(holder, mList)
    }

    private fun initUI(holder: ViewHolder, item: Hospital.DoctorService) {
        holder.mHealthFeedBinding.tvServiceName.text = item.service.name

    }

    class ViewHolder(view: ItemServiceListBinding) : RecyclerView.ViewHolder(view.root) {
        val mHealthFeedBinding = view
    }

}