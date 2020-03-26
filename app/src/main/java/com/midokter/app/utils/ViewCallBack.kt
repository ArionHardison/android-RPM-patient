package com.midokter.app.utils

import android.content.DialogInterface

interface ViewCallBack {

    interface Alert {
        fun onPositiveButtonClick(dialog: DialogInterface)
        fun onNegativeButtonClick(dialog: DialogInterface)
    }
}