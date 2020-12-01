package com.telehealthmanager.app.ui.twilio.model

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class ConnectedStatus(
    var room_name: String = "",
    var accessToken: String = "",
    var isConnected: Boolean = false
) : Parcelable