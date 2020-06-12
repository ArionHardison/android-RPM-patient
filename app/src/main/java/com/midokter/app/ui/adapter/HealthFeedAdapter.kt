package com.midokter.app.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.midokter.app.BuildConfig
import com.midokter.app.R
import com.midokter.app.databinding.ItemHealthFeedBinding
import com.midokter.app.repositary.model.ArticleResponse
import com.midokter.app.utils.CustomClickListener
import com.midokter.app.utils.ViewUtils


public class HealthFeedAdapter (val mContext : Context, val list: MutableList<ArticleResponse.Article>, val listener:OnHealthFeedClickListener) :
    RecyclerView.Adapter<HealthFeedAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HealthFeedAdapter.ViewHolder {
        val inflate = DataBindingUtil.inflate<ItemHealthFeedBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_health_feed, parent, false)
        return HealthFeedAdapter.ViewHolder(inflate)
    }

    override fun getItemCount(): Int = list.size


    override fun onBindViewHolder(holder: HealthFeedAdapter.ViewHolder, position: Int) {
        val mList = list[position]
        initUI(holder,mList)
    }

    private fun initUI(holder: HealthFeedAdapter.ViewHolder, item: ArticleResponse.Article) {
        holder.mHealthFeedBinding.itemClickListener = object : CustomClickListener {
            override fun onItemClickListener() {
                listener.onHealthFeedClicked(item)
            }
        }

        holder.mHealthFeedBinding.tvFeedTitle.text=item.name
        holder.mHealthFeedBinding.tvFeedDescription.text=item.description
        holder.mHealthFeedBinding.tvFeedDay.text=ViewUtils.getTimeAgoFormat(item.updatedAt)
        if (item.coverPhoto != null && item.coverPhoto != "") {
            ViewUtils.setImageViewGlide(mContext,
                holder.mHealthFeedBinding.imgFeed, BuildConfig.BASE_IMAGE_URL+item.coverPhoto)
        }
    }

    class ViewHolder(view : ItemHealthFeedBinding) : RecyclerView.ViewHolder(view.root) {
        val mHealthFeedBinding = view
    }

    interface OnHealthFeedClickListener{
        fun onHealthFeedClicked(item:ArticleResponse.Article)
    }
}