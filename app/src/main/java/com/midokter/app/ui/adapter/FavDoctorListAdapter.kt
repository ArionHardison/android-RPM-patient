package com.midokter.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.R
import com.midokter.app.databinding.FavDoctorListItemBinding
import com.midokter.app.repositary.model.MainResponse

class FavDoctorListAdapter(val items: MutableList<MainResponse.Doctor>, val context: Context) :
    RecyclerView.Adapter<FavDoctorViewHolder>() {

    override fun onBindViewHolder(holder: FavDoctorViewHolder, position: Int) {
        val item=items!![position]
        holder.itemBinding.favdoctorName?.text = item.first_name.plus(" ").plus(item.last_name)
        holder.itemBinding.favDrTypeTxt?.text = item.specialities_name!!as String
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavDoctorViewHolder {

        val inflate = DataBindingUtil.inflate<FavDoctorListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.fav_doctor_list_item, parent, false)
        return FavDoctorViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class FavDoctorViewHolder(view: FavDoctorListItemBinding) : RecyclerView.ViewHolder(view.root) {
    // Holds the TextView that will add each animal to
    val itemBinding = view
}