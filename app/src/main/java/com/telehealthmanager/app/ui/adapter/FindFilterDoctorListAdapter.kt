package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
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

class FindFilterDoctorListAdapter(
    val context: Context,
    val listener: ViewCallBack.IDoctorListener
) :
    RecyclerView.Adapter<FindDoctorViewHolder>() {

    var items: MutableList<DoctorListResponse.specialities.DoctorProfile> = mutableListOf()

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    private var position: Int = 0

    override fun onBindViewHolder(holder: FindDoctorViewHolder, position: Int) {
        val item: DoctorListResponse.specialities.DoctorProfile = items[position]
        if (!item.hospital.isNullOrEmpty()) {
            holder.itemBinding.textView47.text = (item.hospital[0].first_name ?: "").plus(" ").plus(item.hospital[0].last_name ?: "")

            item.hospital[0].clinic?.let { clinic ->
                if (clinic.name != null && clinic.address != null) {
                    holder.itemBinding.textView52.text = (item.hospital[0].clinic?.name ?: "No Clinic").plus(" , ").plus(item.hospital[0].clinic?.address ?: "No address")
                } else {
                    holder.itemBinding.textView52.text = "No Address"
                    if (clinic.name != null) {
                        holder.itemBinding.textView52.text = clinic.name
                    }

                    if (clinic.address != null) {
                        holder.itemBinding.textView52.text = clinic.address
                    }
                }
            }

            holder.itemBinding.textView46.text = (item.hospital[0].feedback_percentage ?: "0").plus("%")
            if (item.hospital[0].availability != null) {
                when (item.hospital[0].availability) {
                    "today" -> {
                        holder.itemBinding.textView51.visibility = View.VISIBLE
                        holder.itemBinding.textView51.text = context.getString(R.string.avaliable_today)
                        holder.itemBinding.textView51.setTextColor(Color.parseColor("#5AB357"))
                        holder.itemBinding.textView51.setBackgroundResource(R.drawable.available_today)
                    }
                    "tommorrow" -> {
                        holder.itemBinding.textView51.visibility = View.VISIBLE
                        holder.itemBinding.textView51.text = context.getString(R.string.avaliable_tomorrow)
                        holder.itemBinding.textView51.setTextColor(Color.parseColor("#DC852E"))
                        holder.itemBinding.textView51.setBackgroundResource(R.drawable.available_tomorrow)
                    }
                    else -> {
                        holder.itemBinding.textView51.visibility = View.GONE;
                    }
                }
            }

            holder.itemBinding.button15.setOnClickListener {
                listener.onCallClick(item.hospital[0].mobile)
            }
        }
        ViewUtils.setDocViewGlide(context, holder.itemBinding.imageView18, BuildConfig.BASE_IMAGE_URL.plus(item.profile_pic))
        holder.itemBinding.textView50.text = item.experience ?: "0".plus(" ").plus(context.getString(R.string.years_of_exp))
        holder.itemBinding.textView48.text = item.speciality?.name
        holder.itemBinding.textView54.text = preferenceHelper.getValue(PreferenceKey.CURRENCY, "$").toString().plus(item.fees ?: "0")

        holder.itemBinding.button16.setOnClickListener {
            listener.onBookClick(item)
        }

        holder.itemView.setOnClickListener {
            setPosition(this@FindFilterDoctorListAdapter, position)
            listener.onItemClick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindDoctorViewHolder {
        val inflate = DataBindingUtil.inflate<FindDoctorListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.find_doctor_list_item, parent, false
        )
        return FindDoctorViewHolder(inflate)
    }

    fun addItems(list: MutableList<DoctorListResponse.specialities.DoctorProfile>) {
        items=list
        notifyDataSetChanged()
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }


    fun changeFavState(isFavorite: String) {
        items[position].let {
            it.hospital[0].is_favourite = isFavorite
        }
        notifyItemChanged(getPosition())
    }

    private fun getPosition(): Int {
        return position
    }

    companion object {
        fun setPosition(doctorsAdapter: FindFilterDoctorListAdapter, position: Int) {
            doctorsAdapter.position = position
        }
    }


}

class FindDoctorViewHolder(view: FindDoctorListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}