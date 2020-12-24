package com.telehealthmanager.app.ui.activity.chat

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.databinding.ActivityChatProblemAreaBinding
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorsViewModel
import com.telehealthmanager.app.ui.adapter.ChatProblemAreasListAdapter
import com.telehealthmanager.app.ui.adapter.IChatProblemAreaListener
import com.telehealthmanager.app.utils.ViewUtils
import java.io.Serializable

class ChatProblemAreaActivity : BaseActivity<ActivityChatProblemAreaBinding>(), ChatNavigator,
    IChatProblemAreaListener {

    private lateinit var viewModel: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityChatProblemAreaBinding
    private var mCategoriesAdapter: ChatProblemAreasListAdapter? = null

    override fun getLayoutId(): Int = R.layout.activity_chat_problem_area

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityChatProblemAreaBinding
        viewModel = ViewModelProvider(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewModel = viewModel
        initApiCal()
        initAdapter()
        observeResponse()
        mDataBinding.textViewBack.setOnClickListener {
            finish()
        }
    }

    override fun onChatProblemAreaClicked(category: CategoryResponse.Category) {
        val intent = Intent()
        intent.putExtra("selectedCategory", category as Serializable)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    private fun initApiCal() {
        loadingObservable.value = true
        viewModel.getCategorys()
    }

    private fun observeResponse() {

        viewModel.mCategoryResponse.observe(this, Observer<CategoryResponse> {
            viewModel.mCategoryList =
                it.category as MutableList<CategoryResponse.Category>?
            if (viewModel.mCategoryList!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mCategoriesAdapter =
                ChatProblemAreasListAdapter(viewModel.mCategoryList!!, this,this)
            mDataBinding.rvCategories.adapter = mCategoriesAdapter
            mDataBinding.rvCategories.layoutManager = GridLayoutManager(applicationContext, 2)
            mCategoriesAdapter!!.notifyDataSetChanged()
            loadingObservable.value = false
        })

        viewModel.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ChatProblemAreaActivity, message, false)
        })
    }

    private fun initAdapter() {
        mCategoriesAdapter = ChatProblemAreasListAdapter(viewModel.mCategoryList!!, this,this)
        mDataBinding.rvCategories.adapter = mCategoriesAdapter
        mDataBinding.rvCategories.layoutManager = GridLayoutManager(applicationContext, 2)
        mCategoriesAdapter!!.notifyDataSetChanged()
    }
}
