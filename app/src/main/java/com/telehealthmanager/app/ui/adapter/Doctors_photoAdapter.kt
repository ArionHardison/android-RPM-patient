package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.ListItemPhotosBinding
import com.telehealthmanager.app.databinding.VisitedDoctorsListItemBinding
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.utils.ViewUtils

class Doctors_photoAdapter(val items: MutableList<Hospital.Clinic.photos>, val context: Context) :
    RecyclerView.Adapter<PhotosViewHolder>() {

    override fun onBindViewHolder(holder: PhotosViewHolder, position: Int) {
        val item=items!![position]

             ViewUtils.setImageViewGlide(context,  holder.itemBinding.imageView28, BuildConfig.BASE_IMAGE_URL.plus(item?.image!!))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val inflate = DataBindingUtil.inflate<ListItemPhotosBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_photos, parent, false)
        return PhotosViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

class PhotosViewHolder(view: ListItemPhotosBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}