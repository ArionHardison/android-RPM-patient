package com.telehealthmanager.app.ui.twilio

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CallRequest(
    var hospital_id: String = "",
    var patient_id: String = "",
    var room_id: String = "",
    var is_video: String = ""
) : Parcelable
