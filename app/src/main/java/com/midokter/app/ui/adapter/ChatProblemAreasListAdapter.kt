package com.midokter.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.BaseApplication
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.data.PreferenceHelper
import com.midokter.app.data.PreferenceKey
import com.midokter.app.data.getValue
import com.midokter.app.databinding.ListItemCategoryBinding
import com.midokter.app.databinding.ListItemCategoryWithImageBinding
import com.midokter.app.repositary.model.CategoryResponse
import com.midokter.app.ui.activity.chat.ChatActivity
import com.midokter.app.ui.activity.findDoctors.FindDoctorsListActivity
import com.midokter.app.utils.ViewUtils
import kotlinx.android.synthetic.main.chat_problem_list_item.view.*
import kotlinx.android.synthetic.main.fav_doctor_list_item.view.*
import kotlinx.android.synthetic.main.list_item_finddoctor_categories.view.*

class ChatProblemAreasListAdapter(val items: MutableList<CategoryResponse.Category>, val context: Context,val listener:IChatProblemAreaListener) :
    RecyclerView.Adapter<ChatProblemsViewHolder>() {
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun onBindViewHolder(holder: ChatProblemsViewHolder, position: Int) {

        val item=items[position]
        holder.itemBinding.tvName.text = item.name
        holder.itemBinding.tvStrikePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
        if(item.offer_fees==0.00){
            holder.itemBinding.tvPrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY,"$"),item.fees.toString())
            holder.itemBinding.tvStrikePrice.visibility=View.GONE
        }
        else{
            holder.itemBinding.tvStrikePrice.visibility=View.VISIBLE
            holder.itemBinding.tvPrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY,"$"),item.offer_fees.toString())
            holder.itemBinding.tvStrikePrice.text = String.format("%s %s", preferenceHelper.getValue(PreferenceKey.CURRENCY,"$"),item.fees.toString())
        }
        if(item.image!=null)
            ViewUtils.setImageViewGlide(context,  holder.itemBinding.imageView22, BuildConfig.BASE_IMAGE_URL.plus(item.image))
        holder.itemBinding.itemView.setOnClickListener {
            listener.onChatProblemAreaClicked(item)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatProblemsViewHolder {
        val inflate = DataBindingUtil.inflate<ListItemCategoryWithImageBinding>(
            LayoutInflater.from(parent.context),
            R.layout.list_item_category_with_image, parent, false
        )
        return ChatProblemsViewHolder(inflate)
    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return items.size
    }
}

interface IChatProblemAreaListener{
   fun onChatProblemAreaClicked(category:CategoryResponse.Category)
}

class ChatProblemsViewHolder(view: ListItemCategoryWithImageBinding) : RecyclerView.ViewHolder(view.root) {
    // Holds the TextView that will add each animal to
    val itemBinding = view
}