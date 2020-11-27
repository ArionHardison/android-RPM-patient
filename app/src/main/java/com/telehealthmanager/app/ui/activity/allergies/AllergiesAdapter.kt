package com.telehealthmanager.app.ui.activity.allergies

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.R
import com.telehealthmanager.app.repositary.model.ProfileResponse
import kotlinx.android.synthetic.main.item_view_allergies.view.*

class AllergiesAdapter(var items: MutableList<ProfileResponse.Allergies>, val context: Context, val listener: IAllergiesListener) :
    RecyclerView.Adapter<ViewHolder>()/*, Filterable*/ {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view_allergies, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data: ProfileResponse.Allergies = items.get(position)
        holder.allergiesText?.text = data.name
        holder.itemView.setOnClickListener {
            listener.onIemClick(data)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface IAllergiesListener {
        fun onIemClick(allergies: ProfileResponse.Allergies)
    }

    /*override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    searchList = items
                    notifyDataSetChanged()
                } else {
                    val filteredList = ArrayList<ProfileResponse.Allergies>()
                    for (row in items) {
                        if (row.name!!.toString().toLowerCase().contains(charString.toLowerCase())) {
                            Log.e("filteredList==>", "" + row.name)
                            filteredList.add(row)
                        }
                    }
                    searchList = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = searchList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                searchList = results?.values as MutableList<ProfileResponse.Allergies>
                notifyDataSetChanged()
            }
        }
    }*/

    fun setList(filteredList: MutableList<ProfileResponse.Allergies>) {
        items = filteredList
        notifyDataSetChanged()
    }
}

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val allergiesText = view.allergiesText
}