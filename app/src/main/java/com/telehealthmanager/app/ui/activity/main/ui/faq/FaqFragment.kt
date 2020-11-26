package com.telehealthmanager.app.ui.activity.main.ui.faq


import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.telehealthmanager.app.R
import com.telehealthmanager.app.base.BaseFragment
import com.telehealthmanager.app.databinding.FragmentFaqBinding
import com.telehealthmanager.app.ui.adapter.FaqAdapter


/**
 * A simple [Fragment] subclass.
 */
class FaqFragment : BaseFragment<FragmentFaqBinding>(), FaqNavigator {

    lateinit var mDataBinding: FragmentFaqBinding
    val mViewModel = FaqViewModel()

    private var titleList: List<String>? = null

    override fun getLayoutId(): Int = R.layout.fragment_faq

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        mDataBinding = mViewDataBinding as FragmentFaqBinding
        mViewModel.navigator = this
        mDataBinding.viewmodel = mViewModel
        mViewModel.initApi()
        observableDate()
    }

    private fun observableDate() {
        mViewModel.mFaqResponse.observe(this, Observer {
            mViewModel.loadingProgress.value = false
            if (!it.faq.isNullOrEmpty()) {
                val expandableListDetail = HashMap<String, List<String>>()
                for (i in it.faq.indices) {
                    val faq = it.faq[i]
                    val subItem: MutableList<String> = ArrayList()
                    subItem.add(faq.answer.toString())
                    expandableListDetail[faq.question.toString()] = subItem
                }
                titleList = ArrayList(expandableListDetail.keys)
                val mFaqAdapter = FaqAdapter(context!!, titleList as ArrayList<String>, expandableListDetail)
                mDataBinding.expendableList.setAdapter(mFaqAdapter)
            } else {
                mDataBinding.tvNotFound.visibility = View.VISIBLE
            }
        })


        mViewModel.loadingProgress.observe(this, Observer {
            if (it)
                showLoading()
            else
                hideLoading()
        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_faq, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val itemId: Int = item.itemId
        if (itemId == R.id.faqAlert) {
            onOpenFAQAlert()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onOpenFAQAlert() {
        val btnSheet = layoutInflater.inflate(R.layout.alert_faq, null)
        val dialog = BottomSheetDialog(this.requireContext())
        dialog.setContentView(btnSheet)
        btnSheet.findViewById<LinearLayout>(R.id.layoutChat).setOnClickListener {
            dialog.dismiss()
        }
        btnSheet.findViewById<LinearLayout>(R.id.layoutCall).setOnClickListener {
            dialog.dismiss()
        }
        btnSheet.findViewById<LinearLayout>(R.id.layoutWeb).setOnClickListener {
            dialog.dismiss()
        }
        btnSheet.findViewById<TextView>(R.id.tvCancel).setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}


