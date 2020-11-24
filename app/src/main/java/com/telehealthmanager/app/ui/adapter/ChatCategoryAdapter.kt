package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.ListItemCategoryBinding
import com.telehealthmanager.app.repositary.model.CategoryResponse

class ChatCategoryAdapter(val items: MutableList<CategoryResponse.Category>, val context: Context, var selectedIndex: Int?, val listener: IChatCategoryListener) :
    RecyclerView.Adapter<ChatCategoriesViewHolder>(), Filterable {

    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    private var SearchList: MutableList<CategoryResponse.Category>? = null

    init {
        this.SearchList = items
    }

    override fun onBindViewHolder(holder: ChatCategoriesViewHolder, position: Int) {

        val item = SearchList!![position]
        holder.itemBinding.tvName.text = item.name
        holder.itemBinding.tvStrikePrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        if (item.discount == 0.00) {
            holder.itemBinding.tvPrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), item.fees.toString())
            holder.itemBinding.tvStrikePrice.visibility = View.GONE
        } else {
            holder.itemBinding.tvStrikePrice.visibility = View.VISIBLE
            holder.itemBinding.tvPrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), item.offer_fees.toString())
            holder.itemBinding.tvStrikePrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"), item.fees.toString())
        }
        // ViewUtils.setImageViewGlide(context,  holder.itemBinding.imageView16, BuildConfig.BASE_IMAGE_URL.plus(item?.image!!))

        holder.itemBinding.itemView.setOnClickListener {
            listener.onChatCategoryClicked(item)
            selectedIndex = position
            notifyDataSetChanged()
        }

        if (selectedIndex != null && position == selectedIndex) {
            holder.itemView.background = ContextCompat.getDrawable(context, R.drawable.bg_color_primary_border)
            holder.itemBinding.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.colorButton))
            holder.itemBinding.tvName.setTextColor(ContextCompat.getColor(context, R.color.colorButton))
            holder.itemBinding.tvStrikePrice.setTextColor(ContextCompat.getColor(context, R.color.colorButton))
        } else {
            holder.itemView.background = ContextCompat.getDrawable(context, R.drawable.bg_color_grey_border)
            holder.itemBinding.tvPrice.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            holder.itemBinding.tvName.setTextColor(ContextCompat.getColor(context, R.color.colorBlack))
            holder.itemBinding.tvStrikePrice.setTextColor(ContextCompat.getColor(context, R.color.colorDarkGrey))
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatCategoriesViewHolder {
        val inflate = DataBindingUtil.inflate<ListItemCategoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_category, parent, false
        )
        return ChatCategoriesViewHolder(inflate)

    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return if (SearchList!!.size < 3) {
            SearchList!!.size
        } else {
            3
        }
    }

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    SearchList = items
                    notifyDataSetChanged()
                } else {
                    val filteredList = ArrayList<CategoryResponse.Category>()
                    for (row in items) {
                        if (row.name!!.toLowerCase().contains(charString.toLowerCase())) {
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

                SearchList = results?.values as MutableList<CategoryResponse.Category>
                notifyDataSetChanged()
            }

        }
    }

    fun setSelectedDoc(position: Int?) {
        this.selectedIndex = position
        notifyDataSetChanged()
    }
}

interface IChatCategoryListener {
    fun onChatCategoryClicked(category: CategoryResponse.Category)
}

class ChatCategoriesViewHolder(view: ListItemCategoryBinding) : RecyclerView.ViewHolder(view.root) {
    val itemBinding = view

}