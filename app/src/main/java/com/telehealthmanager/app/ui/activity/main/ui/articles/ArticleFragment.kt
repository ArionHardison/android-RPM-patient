package com.telehealthmanager.app.ui.activity.main.ui.articles


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.databinding.FragmentArticleBinding
import com.telehealthmanager.app.repositary.model.ArticleResponse
import com.telehealthmanager.app.ui.activity.healthfeeddetails.HealthFeedDetailsActivity
import com.telehealthmanager.app.ui.adapter.HealthFeedAdapter
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

/**
 * A simple [Fragment] subclass.
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding>(), ArticleNavigator,
    HealthFeedAdapter.OnHealthFeedClickListener {

    private var mHeathFeedAdapter: HealthFeedAdapter? = null
    lateinit var mDataBinding: FragmentArticleBinding
    val mViewModel = ArticleViewModel()
    override fun getLayoutId(): Int = R.layout.fragment_article

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentArticleBinding
        mViewModel.navigator = this
        mDataBinding.viewmodel = mViewModel
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
            ViewUtils.showToast(requireActivity(), message, false)
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
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mHeathFeedAdapter = HealthFeedAdapter(requireActivity(), mViewModel.mAllArticles!!, this)
            mDataBinding.healthFeedAdapter = mHeathFeedAdapter
            mHeathFeedAdapter!!.notifyDataSetChanged()
            mViewModel.loadingProgress.value = false
        })
    }

    private fun initAdapter() {
        mHeathFeedAdapter = HealthFeedAdapter(requireActivity(), mViewModel.mAllArticles!!, this)
        mDataBinding.healthFeedAdapter = mHeathFeedAdapter
        mDataBinding.rvHealthFeed.layoutManager = LinearLayoutManager(requireActivity().applicationContext)
        mHeathFeedAdapter!!.notifyDataSetChanged()
    }

    private fun initApiCall() {
        mViewModel.getArticles()
    }

    override fun onHealthFeedClicked(item: ArticleResponse.Article) {
        startActivity(Intent(context, HealthFeedDetailsActivity::class.java).putExtra("Article", item as Serializable))
    }

}
