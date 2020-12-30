package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.FavDoctorListItemBinding
import com.telehealthmanager.app.repositary.WebApiConstants
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.repositary.model.MainResponse
import com.telehealthmanager.app.ui.activity.searchDoctor.SearchDoctorDetailActivity
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class FavDoctorListAdapter(val favClick: IFavDoctorsClick) :
    RecyclerView.Adapter<FavDoctorViewHolder>() {

    private val items: MutableList<MainResponse.Doctor> = mutableListOf()
    private var position: Int = 0

    override fun onBindViewHolder(holder: FavDoctorViewHolder, position: Int) {
        val item = items[position]
        if (item.hospital != null) {
            holder.itemBinding.favdoctorName.text = item.hospital.first_name.plus(" ").plus(item.hospital.last_name)
            holder.itemBinding.favDrTypeTxt.text = item.hospital.doctor_profile.speciality?.name
            ViewUtils.setDocViewGlide(holder.itemView.context, holder.itemBinding.imageView8, BuildConfig.BASE_IMAGE_URL.plus(item.hospital?.doctor_profile?.profile_pic))
            holder.itemView.setOnClickListener {
                setPosition(this@FavDoctorListAdapter, position)
                favClick.onItemClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavDoctorViewHolder {
        val inflate = DataBindingUtil.inflate<FavDoctorListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.fav_doctor_list_item, parent, false
        )
        return FavDoctorViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }

    fun addItem(list: MutableList<MainResponse.Doctor>) {
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun removePosition() {
        items.removeAt(getPosition())
        notifyDataSetChanged()
    }

    private fun getPosition(): Int {
        return position
    }

    companion object {
        fun setPosition(doctorsAdapter: FavDoctorListAdapter, position: Int) {
            doctorsAdapter.position = position
        }
    }
}

interface IFavDoctorsClick {
    fun onItemClicked(item: MainResponse.Doctor)
}

class FavDoctorViewHolder(view: FavDoctorListItemBinding) : RecyclerView.ViewHolder(view.root) {
    // Holds the TextView that will add each animal to
    val itemBinding = view
}