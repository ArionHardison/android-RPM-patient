package com.telehealthmanager.app.ui.twilio.listener

interface CallHangUpListener {
    fun onCallEnd(callDuration:String)
}