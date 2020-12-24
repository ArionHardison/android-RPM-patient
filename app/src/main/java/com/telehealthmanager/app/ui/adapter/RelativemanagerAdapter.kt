package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.ListItemRelativeMangBinding
import com.telehealthmanager.app.repositary.model.RelativeList
import com.telehealthmanager.app.utils.ViewUtils

class RelativemanagerAdapter(val items: MutableList<RelativeList>, val context: Context, val listener: IRelativeListener) :
    RecyclerView.Adapter<RelativemanagerViewHolder>() {

    override fun onBindViewHolder(holder: RelativemanagerViewHolder, position: Int) {
        val item = items!![position]
        holder.itemBinding.name?.text = item.first_name.plus(" ").plus(item.last_name)
        holder.itemBinding.age?.text = item.profile?.age.plus(" years old")
        ViewUtils.setDocViewGlide(context, holder.itemBinding.imageView8, BuildConfig.BASE_IMAGE_URL.plus(item.profile?.profile_pic))
        holder.itemView.setOnClickListener {
            listener.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelativemanagerViewHolder {

        val inflate = DataBindingUtil.inflate<ListItemRelativeMangBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_relative_mang, parent, false
        )
        return RelativemanagerViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    interface IRelativeListener {
        fun onItemClick(selectedItem: RelativeList)
    }
}

class RelativemanagerViewHolder(view: ListItemRelativeMangBinding) : RecyclerView.ViewHolder(view.root) {
    // Holds the TextView that will add each animal to
    val itemBinding = view
}