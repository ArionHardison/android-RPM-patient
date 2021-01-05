package com.telehealthmanager.app.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.SearchDoctorListItemBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class SearchDoctorsListAdapter(val iDoctorsSelectListener: IDoctorsSelectListener) :
    RecyclerView.Adapter<SearchDoctorsViewHolder>() {

    private val items: MutableList<Hospital> = mutableListOf()

    override fun onBindViewHolder(holder: SearchDoctorsViewHolder, position: Int) {
        val item = items[position]
        holder.itemBinding.textView90.text = item.first_name.plus(" ").plus(item.last_name)
        holder.itemBinding.textView91.text = item.doctor_profile.certification

        if (item.doctor_profile.speciality?.name != null) {
            holder.itemBinding.textView92.text = item.doctor_profile.speciality.name
            holder.itemBinding.textView92.visibility = View.VISIBLE
        } else
            holder.itemBinding.textView92.visibility = View.GONE

        ViewUtils.setDocViewGlide(
            holder.itemView.context,
            holder.itemBinding.imageView23,
            BuildConfig.BASE_IMAGE_URL.plus(item.doctor_profile.profile_pic)
        )

        holder.itemView.setOnClickListener {
            iDoctorsSelectListener.onItemSelect(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchDoctorsViewHolder {
        val inflate = DataBindingUtil.inflate<SearchDoctorListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.search_doctor_list_item, parent, false
        )
        return SearchDoctorsViewHolder(inflate)
    }

    fun addItems(list: MutableList<Hospital>) {
        items.addAll(list)
        notifyItemRangeInserted(items.size - 1, list.size)
    }


    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

interface IDoctorsSelectListener {
    fun onItemSelect(item: Hospital)
}

class SearchDoctorsViewHolder(view: SearchDoctorListItemBinding) :
    RecyclerView.ViewHolder(view.root) {
    // Holds the TextView that will add each animal to
    val itemBinding = view
}