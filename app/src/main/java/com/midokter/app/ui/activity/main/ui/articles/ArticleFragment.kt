package com.midokter.app.ui.activity.main.ui.articles


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentArticleBinding

/**
 * A simple [Fragment] subclass.
 */
class ArticleFragment : BaseFragment<FragmentArticleBinding>() {

    override fun getLayoutId(): Int = R.layout.fragment_article

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
    }

}
