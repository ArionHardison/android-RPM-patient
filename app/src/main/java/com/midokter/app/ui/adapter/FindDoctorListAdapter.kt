package com.midokter.app.ui.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.getValue
import com.midokter.app.databinding.FindDoctorListItemBinding
import com.midokter.app.repositary.model.DoctorListResponse
import com.midokter.app.utils.ViewUtils

class FindDoctorListAdapter(val items:  MutableList<DoctorListResponse.specialities.DoctorProfile>, val context: Context, val listener:IDoctorListener)  :
    RecyclerView.Adapter<FindDoctorViewHolder>() , Filterable {
    private var SearchList: MutableList<DoctorListResponse.specialities.DoctorProfile>? = null
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    init {
        this.SearchList = items
    }


    override fun onBindViewHolder(holder: FindDoctorViewHolder, position: Int) {
        val item=SearchList!![position]
        holder.itemBinding.textView47?.text = item.hospital[0].first_name.plus(" ").plus(item.hospital[0].last_name)
        ViewUtils.setImageViewGlide(context,  holder.itemBinding.imageView18,BuildConfig.BASE_IMAGE_URL.plus(item.profile_pic))
        holder.itemBinding.textView52?.text = item.experience?.plus(" ").plus(context.getString(R.string.years_of_exp))
        holder.itemBinding.textView52?.text = item.hospital[0].clinic.name.plus(" , ").plus(item.hospital[0].clinic.address)
        holder.itemBinding.textView46?.text =  item.hospital[0].feedback_percentage
        holder.itemBinding.textView50?.text = String.format(context.getString(R.string.years_of_exp),item.experience)
            holder.itemBinding.textView54?.text = preferenceHelper.getValue(PreferenceKey.CURRENCY,"$").toString().plus(item.fees)

        holder.itemBinding.button15.setOnClickListener(View.OnClickListener {
            listener.oncallclick(item.hospital[0].mobile)
        })
        holder.itemBinding.button16.setOnClickListener(View.OnClickListener {
            listener.onbookclick(item)
        })

        holder.itemView.setOnClickListener {

            listener.onitemclick(item)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FindDoctorViewHolder {

        val inflate = DataBindingUtil.inflate<FindDoctorListItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.find_doctor_list_item, parent, false)
        return FindDoctorViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return SearchList!!.size
    }
    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    SearchList = items
                    notifyDataSetChanged()
                } else {
                    val filteredList = ArrayList<DoctorListResponse.specialities.DoctorProfile>()
                    for (row in items) {
                        if (row.hospital[0].email!!.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    SearchList = filteredList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = SearchList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                SearchList = results?.values as MutableList<DoctorListResponse.specialities.DoctorProfile>
                notifyDataSetChanged()
            }

        }
    }
}
interface IDoctorListener{
    fun onbookclick(selectedItem:DoctorListResponse.specialities.DoctorProfile)
    fun onitemclick(selectedItem:DoctorListResponse.specialities.DoctorProfile)
    fun oncallclick(phone:String)
}
class FindDoctorViewHolder(view: FindDoctorListItemBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view
}