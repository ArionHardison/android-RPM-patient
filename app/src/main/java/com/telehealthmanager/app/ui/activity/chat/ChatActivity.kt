package com.telehealthmanager.app.ui.activity.chat

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseActivity
import com.telehealthmanager.app.data.Constant
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.data.PreferenceKey
import com.telehealthmanager.app.data.getValue
import com.telehealthmanager.app.databinding.ActivityChatBinding
import com.telehealthmanager.app.repositary.model.CategoryResponse
import com.telehealthmanager.app.ui.activity.findDoctors.FindDoctorsViewModel
import com.telehealthmanager.app.ui.adapter.ChatCategoryAdapter
import com.telehealthmanager.app.ui.adapter.IChatCategoryListener
import com.telehealthmanager.app.utils.ViewUtils
import kotlinx.android.synthetic.main.content_chat.*
import kotlinx.android.synthetic.main.content_chat.view.*

class ChatActivity : BaseActivity<ActivityChatBinding>(), ChatNavigator, IChatCategoryListener {
    override fun getLayoutId(): Int = R.layout.activity_chat
    private lateinit var viewModel: ChatViewModel
    private lateinit var viewModelFindDoctor: FindDoctorsViewModel
    private lateinit var mDataBinding: ActivityChatBinding
    private var mCategoriesAdapter: ChatCategoryAdapter? = null
    var category: CategoryResponse.Category? = null
    var seeAllSelectedCategory: CategoryResponse.Category? = null
    private val preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)

    override fun initView(mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as ActivityChatBinding
        viewModel = ViewModelProviders.of(this).get(ChatViewModel::class.java)
        mDataBinding.viewmodel = viewModel
        viewModelFindDoctor = ViewModelProviders.of(this).get(FindDoctorsViewModel::class.java)
        mDataBinding.viewModelFindDoctor = viewModelFindDoctor
        viewModel.navigator = this

        observeResponse()
        initApiCal()
        initAdapter()

        mDataBinding.contentChat.see_all_specialist.setOnClickListener {
            val intent = Intent(applicationContext, ChatProblemAreaActivity::class.java)
            startActivityForResult(intent, Constant.CATEGORY_REQUEST_CODE)
        }
        mDataBinding.buttonContinue.setOnClickListener {
            if (mDataBinding.contentChat.etNotes.text.toString().equals("")) {
                ViewUtils.showToast(this@ChatActivity, getString(R.string.please_enter_symptoms_health_problem), false)
            } else if (category == null) {
                ViewUtils.showToast(this@ChatActivity, getString(R.string.please_choose_speciality), false)
            } else {
                val intent = Intent(applicationContext, ChatSummaryActivity::class.java)
                intent.putExtra("category", category)
                intent.putExtra("notes", etNotes.text.toString())
                startActivity(intent)
            }
        }
        mDataBinding.contentChat.llSelectedCategory.setOnClickListener {
            if (seeAllSelectedCategory != null) {
                category = seeAllSelectedCategory
                mDataBinding.contentChat.llSelectedCategory.background =
                    ContextCompat.getDrawable(this, R.drawable.bg_color_primary_border)
                mDataBinding.contentChat.tvSelectedPrice.setTextColor(ContextCompat.getColor(this, R.color.colorButton))
                mDataBinding.contentChat.tvSelectedName.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorButton
                    )
                )
                mDataBinding.contentChat.tvSelectedStrikePrice.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.colorButton
                    )
                )
                initAdapter()
            }
        }
    }

    override fun onChatCategoryClicked(category: CategoryResponse.Category) {
        this.category = category
        mDataBinding.contentChat.tvSelectedName
        mDataBinding.contentChat.llSelectedCategory.background =
            ContextCompat.getDrawable(this, R.drawable.bg_color_grey_border)
        mDataBinding.contentChat.tvSelectedPrice.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.colorBlack
            )
        )
        mDataBinding.contentChat.tvSelectedName.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.colorBlack
            )
        )
        mDataBinding.contentChat.tvSelectedStrikePrice.setTextColor(
            ContextCompat.getColor(
                this,
                R.color.colorBlack
            )
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                Constant.CATEGORY_REQUEST_CODE -> {
                    // To retrieve object in second Activity
                    if (data != null) {
                        category = data.getSerializableExtra("selectedCategory") as CategoryResponse.Category
                        seeAllSelectedCategory = category
                    }
                    for (i: Int in mCategoriesAdapter!!.items.indices) {
                        if (mCategoriesAdapter!!.items[i].id == seeAllSelectedCategory!!.id) {
                            mCategoriesAdapter!!.setSelectedDoc(i)
                            mDataBinding.contentChat.llSelectedCategory.visibility = View.GONE
                            return
                        }
                    }
                    mDataBinding.contentChat.llSelectedCategory.visibility = View.VISIBLE
                    mDataBinding.contentChat.tvSelectedName.text = category?.name
                    mDataBinding.contentChat.tvSelectedStrikePrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG)
                    if (category?.offer_fees == 0.00) {
                        mDataBinding.contentChat.tvSelectedPrice.text = String.format(
                            "%s %s",
                            preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"),
                            category?.fees.toString()
                        )
                        mDataBinding.contentChat.tvSelectedStrikePrice.visibility = View.GONE
                    } else {
                        mDataBinding.contentChat.tvSelectedStrikePrice.visibility = View.VISIBLE
                        mDataBinding.contentChat.tvSelectedPrice.text = String.format(
                            "%s %s",
                            preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"),
                            category?.offer_fees.toString()
                        )
                        mDataBinding.contentChat.tvSelectedStrikePrice.text = String.format(
                            "%s %s",
                            preferenceHelper.getValue(PreferenceKey.CURRENCY, "$"),
                            category?.fees.toString()
                        )
                    }
                    mDataBinding.contentChat.llSelectedCategory.background =
                        ContextCompat.getDrawable(this, R.drawable.bg_color_primary_border)
                    mDataBinding.contentChat.tvSelectedPrice.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorButton
                        )
                    )
                    mDataBinding.contentChat.tvSelectedName.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorButton
                        )
                    )
                    mDataBinding.contentChat.tvSelectedStrikePrice.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.colorButton
                        )
                    )
                    initAdapter()
                }
            }
        }
    }

    private fun initApiCal() {
        loadingObservable.value = true
        viewModelFindDoctor.getCategorys()
    }

    private fun observeResponse() {

        viewModelFindDoctor.mCategoryResponse.observe(this, Observer<CategoryResponse> {
            viewModelFindDoctor.mCategoryList =
                it.category as MutableList<CategoryResponse.Category>?
            viewModelFindDoctor.mFirstCategoryList!!.clear()
            if (viewModelFindDoctor.mCategoryList!!.size > 0) {
                mDataBinding.contentChat.tvNotFound.visibility = View.GONE
                for (i: Int in it.category.indices) {
                    when (i) {
                        0 -> {
                            viewModelFindDoctor.mFirstCategoryList!!.add(it.category[i])
                        }
                        1 -> {
                            viewModelFindDoctor.mFirstCategoryList!!.add(it.category[i])
                        }
                        2 -> {
                            viewModelFindDoctor.mFirstCategoryList!!.add(it.category[i])
                        }
                    }
                }
            } else {
                mDataBinding.contentChat.tvNotFound.visibility = View.VISIBLE
            }
            initAdapter()
            loadingObservable.value = false
        })
        viewModelFindDoctor.getErrorObservable().observe(this, Observer<String> { message ->
            loadingObservable.value = false
            ViewUtils.showToast(this@ChatActivity, message, false)
        })
    }

    private fun initAdapter() {
        mCategoriesAdapter =
            ChatCategoryAdapter(viewModelFindDoctor.mFirstCategoryList!!, this, null, this)
        mDataBinding.contentChat.rv_categories.adapter = mCategoriesAdapter
        mCategoriesAdapter!!.notifyDataSetChanged()
    }
}
