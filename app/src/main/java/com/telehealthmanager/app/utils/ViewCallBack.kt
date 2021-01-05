package com.telehealthmanager.app.utils

import android.content.DialogInterface
import com.telehealthmanager.app.repositary.model.DoctorListResponse

interface ViewCallBack {

    interface Alert {
        fun onPositiveButtonClick(dialog: DialogInterface)
        fun onNegativeButtonClick(dialog: DialogInterface)
    }

    interface IDoctorListener {
        fun onBookClick(selectedItem: DoctorListResponse.specialities.DoctorProfile)
        fun onItemClick(selectedItem: DoctorListResponse.specialities.DoctorProfile)
        fun onCallClick(phone: String)
    }
}