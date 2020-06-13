package com.midokter.app.ui.activity.main.ui.articles


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentArticleBinding
import com.midokter.app.repositary.model.ArticleResponse
import com.midokter.app.ui.adapter.HealthFeedAdapter
import com.midokter.app.utils.ViewUtils
import com.midokter.doctor.ui.activity.healthfeeddetails.HealthFeedDetailsActivity
import java.io.Serializable

/**
 * A simple [Fragment] subclass.
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding>() , ArticleNavigator,
    HealthFeedAdapter.OnHealthFeedClickListener {

    private var mHeathFeedAdapter: HealthFeedAdapter? = null
    lateinit var mViewDataBinding: FragmentArticleBinding
    val mViewModel = ArticleViewModel()
    override fun getLayoutId(): Int = R.layout.fragment_article



    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        this.mViewDataBinding = mViewDataBinding as FragmentArticleBinding
        mViewModel.navigator = this
        mViewDataBinding.viewmodel = mViewModel
        initAdapter()
        observeSuccessResponse()
        observeErrorResponse()
        observeShowLoading()
        initApiCall()

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear();
    }
    private fun observeErrorResponse() {
        mViewModel.getErrorObservable().observe(this, Observer<String> { message ->
            mViewModel.loadingProgress.value = false
            ViewUtils.showToast(context!!, message, false)
        })
    }

    private fun observeShowLoading() {
        mViewModel.loadingProgress.observe(this, Observer {
            if (!it) {
                hideLoading()
            } else {
                showLoading()
            }
        })
    }

    private fun observeSuccessResponse() {
        mViewModel.mArticleResponse.observe(this, Observer {
            mViewModel.mAllArticles = it.article as MutableList<ArticleResponse.Article>?
            if (mViewModel.mAllArticles!!.size > 0) {
                mViewDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mViewDataBinding.tvNotFound.visibility = View.VISIBLE
            }

            mHeathFeedAdapter = HealthFeedAdapter(context!!, mViewModel.mAllArticles!!,this)
            mViewDataBinding.healthFeedAdapter = mHeathFeedAdapter
            mHeathFeedAdapter!!.notifyDataSetChanged()
            mViewModel.loadingProgress.value = false
        })
    }


    private fun initAdapter() {
        mHeathFeedAdapter = HealthFeedAdapter(context!!, mViewModel.mAllArticles!!, this)
        mViewDataBinding.healthFeedAdapter = mHeathFeedAdapter
        mViewDataBinding.rvHealthFeed.layoutManager = LinearLayoutManager(context!!.applicationContext)
        mHeathFeedAdapter!!.notifyDataSetChanged()
    }

    private fun initApiCall() {
        mViewModel.getArticles()
    }

    override fun onHealthFeedClicked(item: ArticleResponse.Article) {
        startActivity(Intent(context, HealthFeedDetailsActivity::class.java).putExtra("Article",item as Serializable))
    }

}
