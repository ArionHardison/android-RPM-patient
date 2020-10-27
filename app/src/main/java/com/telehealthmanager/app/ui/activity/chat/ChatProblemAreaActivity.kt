package com.telehealthmanager.app.ui.activity.chat

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
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
    override fun getLayoutId(): Int = R.layout.activity_chat_problem_area
    private lateinit var viewModel: ChatViewModel
    private lateinit var viewModelFindDoctor: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityChatProblemAreaBinding
    private var mCategoriesAdapter: ChatProblemAreasListAdapter? = null


    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityChatProblemAreaBinding
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModelFindDoctor = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewModelDoctor = viewModelFindDoctor
        viewModel.navigator = this

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
        viewModelFindDoctor.getCategorys()

    }

    private fun observeResponse() {

        viewModelFindDoctor.mCategoryResponse.observe(this, Observer<CategoryResponse> {

            viewModelFindDoctor.mCategoryslist =
                it.category as MutableList<CategoryResponse.Category>?
            if (viewModelFindDoctor.mCategoryslist!!.size > 0) {
                mDataBinding.tvNotFound.visibility = View.GONE
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
            mCategoriesAdapter =
                ChatProblemAreasListAdapter(viewModelFindDoctor.mCategoryslist!!, this,this)
            mDataBinding.rvCategories.adapter = mCategoriesAdapter
            mDataBinding.rvCategories.layoutManager = GridLayoutManager(applicationContext, 2)
            mCategoriesAdapter!!.notifyDataSetChanged()
            loadingObservable.value = false


        })
        viewModelFindDoctor.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ChatProblemAreaActivity, message, false)
        })
    }

    private fun initAdapter() {
        mCategoriesAdapter = ChatProblemAreasListAdapter(viewModelFindDoctor.mCategoryslist!!, this,this)
        mDataBinding.rvCategories.adapter = mCategoriesAdapter
        mDataBinding.rvCategories.layoutManager = GridLayoutManager(applicationContext, 2)
        mCategoriesAdapter!!.notifyDataSetChanged()
    }
}
