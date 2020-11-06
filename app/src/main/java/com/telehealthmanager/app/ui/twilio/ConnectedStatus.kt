package com.telehealthmanager.app.ui.twilio

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize

@Parcelize
class ConnectedStatus(
    var room_name: String = "",
    var accessToken: String = ""
) : Parcelable