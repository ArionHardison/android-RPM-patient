package com.telehealthmanager.app.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.databinding.SearchDoctorListItemBinding
import com.telehealthmanager.app.repositary.model.Hospital
import com.telehealthmanager.app.utils.ViewUtils


class DoctorsAdapter(val iDoctorsClick: IDoctorsClick) :
    PagingDataAdapter<Hospital, DoctorsAdapter.SearchViewHolder>(PassengersComparator) {

    private var position: Int = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        return SearchViewHolder(
            SearchDoctorListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bindPassenger(it, position) }
    }

    inner class SearchViewHolder(private val binding: SearchDoctorListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindPassenger(item: Hospital, position: Int) = with(binding) {
            textView90.text = item.first_name.plus(" ").plus(item.last_name)
            if (item.doctor_profile?.speciality?.name != null) {
                textView92.text = item.doctor_profile?.speciality?.name
                textView92.visibility = View.VISIBLE
            } else
                textView92.visibility = View.GONE

            textView91.text = item.doctor_profile?.certification
            ViewUtils.setDocViewGlide(
                itemView.context,
                imageView23,
                BuildConfig.BASE_IMAGE_URL.plus(item.doctor_profile?.profile_pic)
            )
            itemView.setOnClickListener {
                Companion.setPosition(this@DoctorsAdapter, position)
                iDoctorsClick.onItemClicked(item)
            }
        }
    }

    object PassengersComparator : DiffUtil.ItemCallback<Hospital>() {
        override fun areItemsTheSame(oldItem: Hospital, newItem: Hospital): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Hospital, newItem: Hospital): Boolean {
            return oldItem == newItem
        }
    }

    fun changeFavState(isFavorite: String) {
        getItem(getPosition())?.let {
            it.is_favourite = isFavorite
        }
        notifyItemChanged(getPosition())
    }

    private fun getPosition(): Int {
        return position
    }

    companion object {
        fun setPosition(doctorsAdapter: DoctorsAdapter, position: Int) {
            doctorsAdapter.position = position
        }
    }
}

interface IDoctorsClick {
    fun onItemClicked(item: Hospital)
}