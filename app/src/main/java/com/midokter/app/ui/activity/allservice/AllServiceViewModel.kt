package com.midokter.app.ui.activity.allservice

import com.midokter.app.base.BaseViewModel
import com.midokter.app.repositary.model.Hospital

class AllServiceViewModel :BaseViewModel<AllServiceNavigator>() {

    var mServiceList: MutableList<Hospital.DoctorService>? = arrayListOf()

}