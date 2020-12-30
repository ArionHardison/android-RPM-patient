package com.telehealthmanager.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.databinding.ItemHealthFeedBinding
import com.telehealthmanager.app.repositary.model.ArticleResponse
import com.telehealthmanager.app.utils.CustomClickListener
import com.telehealthmanager.app.utils.ViewUtils


public class HealthFeedAdapter (val mContext : Context, val list: MutableList<ArticleResponse.Article>, val listener:OnHealthFeedClickListener) :
    RecyclerView.Adapter<HealthFeedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate = DataBindingUtil.inflate<ItemHealthFeedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_health_feed, parent, false)
        return ViewHolder(inflate)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mList: ArticleResponse.Article = list[position]
        initUI(holder,mList)
    }

    private fun initUI(holder: ViewHolder, item: ArticleResponse.Article) {
        holder.mHealthFeedBinding.itemClickListener = object : CustomClickListener {
            override fun onItemClickListener() {
                listener.onHealthFeedClicked(item)
            }
        }

        holder.mHealthFeedBinding.tvFeedTitle.text=item.name
        holder.mHealthFeedBinding.tvFeedDescription.text=item.description
        holder.mHealthFeedBinding.tvFeedDay.text=ViewUtils.getTimeAgoFormat(item.updatedAt)
        if (item.coverPhoto != null && item.coverPhoto != "") {
            Glide.with(mContext)
                .load(BuildConfig.BASE_IMAGE_URL + item.coverPhoto)
                .placeholder(R.drawable.app_logo)
                .error(R.drawable.app_logo)
                .fallback(R.drawable.app_logo)
                .into(holder.mHealthFeedBinding.imgFeed)
        }
    }

    class ViewHolder(view : ItemHealthFeedBinding) : RecyclerView.ViewHolder(view.root) {
        val mHealthFeedBinding = view
    }

    interface OnHealthFeedClickListener{
        fun onHealthFeedClicked(item:ArticleResponse.Article)
    }
}