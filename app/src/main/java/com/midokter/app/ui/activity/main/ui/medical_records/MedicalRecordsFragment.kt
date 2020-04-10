package com.midokter.app.ui.activity.main.ui.medical_records

import android.view.View
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.midokter.app.R
import com.midokter.app.base.BaseFragment
import com.midokter.app.databinding.FragmentMedicalRecordsBinding
import com.midokter.app.ui.adapter.OnlineAppointmentListAdapter
import kotlinx.android.synthetic.main.fragment_medical_records.*
import kotlinx.android.synthetic.main.fragment_online_consultation.*

class MedicalRecordsFragment : BaseFragment<FragmentMedicalRecordsBinding>() {

    val medicalRecords: ArrayList<String> = ArrayList()

    override fun getLayoutId(): Int = R.layout.fragment_medical_records

    override fun initView(mRootView: View?, mViewDataBinding: ViewDataBinding?) {
        addMedicalRecords()
        // Creates a vertical Layout Manager
        rv_medical_records.layoutManager = LinearLayoutManager(context)

        // You can use GridLayoutManager if you want multiple columns. Enter the number of columns as a parameter.
//        rv_animal_list.layoutManager = GridLayoutManager(this, 2)

        // Access the RecyclerView Adapter and load the data into it
        rv_medical_records.addItemDecoration(
            DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL)
        )
        rv_medical_records.adapter = context?.let { OnlineAppointmentListAdapter(medicalRecords, it) }
    }

    private fun addMedicalRecords() {
        medicalRecords.add("Dr.Bretto")
        medicalRecords.add("Dr.John")
        medicalRecords.add("Dr.Jenifer")
        medicalRecords.add("Dr.Virundha")
    }
}
