package com.telehealthmanager.app.fcm

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class ReceiverPushResponse(
    var receiver_id: String? = "",
    var device: String? = "",
    var accesstoken: String? = "",
    var name: String? = "",
    var type: String? = "",
    var sender_id: String? = "",
    var video: String? = "",
    var receiver_image: String? = "",
    var message: String? = "",
    var room_id: String? = "",
    var connectedCall: Boolean = false,
    var connectedToken: String? = ""
) : Parcelable
