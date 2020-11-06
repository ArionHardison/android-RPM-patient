package com.telehealthmanager.app.ui.twilio

interface CallHangUpListener {
    fun onCallEnd(callDuration:String)
}