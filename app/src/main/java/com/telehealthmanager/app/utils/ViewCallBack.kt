package com.telehealthmanager.app.utils

import android.content.DialogInterface
import android.widget.ImageView
import com.telehealthmanager.app.repositary.model.DoctorListResponse

interface ViewCallBack {

    interface Alert {
        fun onPositiveButtonClick(dialog: DialogInterface)
        fun onNegativeButtonClick(dialog: DialogInterface)
    }

    interface IDoctorListener {
        fun onBookClick(selectedItem: DoctorListResponse.specialities.DoctorProfile, profileImage: ImageView)
        fun onItemClick(selectedItem: DoctorListResponse.specialities.DoctorProfile, profileImage: ImageView)
        fun onCallClick(phone: String)
    }

    interface IItemClick {
        fun alertItemClick(strItem: String)
    }
}