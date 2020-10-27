package com.telehealthmanager.app.ui.activity.allservice

import com.telehealthmanager.app.base.BaseViewModel
import com.telehealthmanager.app.repositary.model.Hospital

class AllServiceViewModel :BaseViewModel<AllServiceNavigator>() {

    var mServiceList: MutableList<Hospital.DoctorService>? = arrayListOf()

}