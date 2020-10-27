package com.telehealthmanager.doctor.ui.activity.healthfeeddetails

import android.os.Build
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import androidx.databinding.ViewDataBinding
import com.bumptech.glide.Glide
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityHealthFeedDetailsBinding
import com.telehealthmanager.app.repositary.model.ArticleResponse
import com.telehealthmanager.app.utils.ViewUtils


class HealthFeedDetailsActivity : BaseActivity<ActivityHealthFeedDetailsBinding>(),
    HealthFeedDetailsNavigator {

    lateinit var mViewDataBinding: ActivityHealthFeedDetailsBinding
    val mViewModel = HealthFeedDetailsViewModel()
    override fun getLayoutId(): Int = R.layout.activity_health_feed_details

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as ActivityHealthFeedDetailsBinding
        mViewModel.navigator = this
        mViewDataBinding.viewmodel = mViewModel
        mViewDataBinding.toolbarBackImg.setOnClickListener {
            onBackPressed()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mViewDataBinding.tvFeedDescription.justificationMode = JUSTIFICATION_MODE_INTER_WORD
        }
        initIntentData()
    }

    private fun initIntentData() {
        val article =
            intent.getSerializableExtra("Article") as ArticleResponse.Article
        mViewModel.mArticleResponse.value = article
        mViewModel.title.set(article.name)
        if (article.coverPhoto != null && article.coverPhoto != "") {

            Glide.with(this)
                .load(BuildConfig.BASE_IMAGE_URL + article.coverPhoto)
                .placeholder(R.drawable.leaderboard)
                .error(R.drawable.leaderboard)
                .fallback(R.drawable.leaderboard)
                .into(  mViewDataBinding.imgFeed)
        }
        mViewModel.description.set(article.description)
        mViewModel.date.set(ViewUtils.getTimeAgoFormat(article.updatedAt))
    }
}

