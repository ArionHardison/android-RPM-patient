package com.telehealthmanager.app.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.FindDoctorListItemBinding
import com.telehealthmanager.app.repositary.model.DoctorListResponse
import com.telehealthmanager.app.utils.ViewCallBack
import com.telehealthmanager.app.utils.ViewUtils

class FindDoctorsAdapter(val iDoctorsClick: ViewCallBack.IDoctorListener) :
    PagingDataAdapter<DoctorListResponse.specialities.DoctorProfile, FindDoctorsAdapter.SearchViewHolder>(PassengersComparator) {

    private var position: Int = 0

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SearchViewHolder {
        return SearchViewHolder(
            FindDoctorListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bindPassenger(it, position) }
    }

    inner class SearchViewHolder(private val binding: FindDoctorListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindPassenger(item: DoctorListResponse.specialities.DoctorProfile, position: Int) = with(binding) {
            if (!item.hospital.isNullOrEmpty()) {
                textView47.text = (item.hospital[0].first_name ?: "").plus(" ").plus(item.hospital[0].last_name ?: "")

                item.hospital[0].clinic?.let { clinic ->
                    if (clinic.name != null && clinic.address != null) {
                        textView52.text = (item.hospital[0].clinic?.name ?: "No Clinic").plus(" , ").plus(item.hospital[0].clinic?.address ?: "No address")
                    } else {
                        textView52.text = "No Address"
                        if (clinic.name != null) {
                            textView52.text = clinic.name
                        }

                        if (clinic.address != null) {
                            textView52.text = clinic.address
                        }
                    }
                }

                textView46.text = (item.hospital[0].feedback_percentage ?: "0").plus("%")
                if (item.hospital[0].availability != null) {
                    when (item.hospital[0].availability) {
                        "today" -> {
                            textView51.visibility = View.VISIBLE
                            textView51.text = itemView.context.getString(R.string.avaliable_today)
                            textView51.setTextColor(Color.parseColor("#5AB357"))
                            textView51.setBackgroundResource(R.drawable.available_today)
                        }
                        "tommorrow" -> {
                            textView51.visibility = View.VISIBLE
                            textView51.text = itemView.context.getString(R.string.avaliable_tomorrow)
                            textView51.setTextColor(Color.parseColor("#DC852E"))
                            textView51.setBackgroundResource(R.drawable.available_tomorrow)
                        }
                        else -> {
                            textView51.visibility = View.GONE;
                        }
                    }
                }

                button15.setOnClickListener {
                    iDoctorsClick.onCallClick(item.hospital[0].mobile)
                }
            }
            ViewUtils.setDocViewGlide(itemView.context, imageView18, BuildConfig.BASE_IMAGE_URL.plus(item.profile_pic))
            textView50.text = item.experience ?: "0".plus(" ").plus(itemView.context.getString(R.string.years_of_exp))
            textView48.text = item.speciality?.name
            textView54.text = preferenceHelper.getValue(PreferenceKey.CURRENCY, "$").toString().plus(item.fees ?: "0")

            button16.setOnClickListener {
                iDoctorsClick.onBookClick(item)
            }

            itemView.setOnClickListener {
                iDoctorsClick.onItemClick(item)
            }

            itemView.setOnClickListener {
                setPosition(this@FindDoctorsAdapter, position)
                iDoctorsClick.onItemClick(item)
            }
        }
    }

    object PassengersComparator : DiffUtil.ItemCallback<DoctorListResponse.specialities.DoctorProfile>() {
        override fun areItemsTheSame(oldItem: DoctorListResponse.specialities.DoctorProfile, newItem: DoctorListResponse.specialities.DoctorProfile): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: DoctorListResponse.specialities.DoctorProfile, newItem: DoctorListResponse.specialities.DoctorProfile): Boolean {
            return oldItem == newItem
        }
    }

    fun changeFavState(isFavorite: String) {
        getItem(getPosition())?.let {
            it.hospital[0].is_favourite = isFavorite
        }
        notifyItemChanged(getPosition())
    }

    private fun getPosition(): Int {
        return position
    }

    companion object {
        fun setPosition(doctorsAdapter: FindDoctorsAdapter, position: Int) {
            doctorsAdapter.position = position
        }
    }
}

