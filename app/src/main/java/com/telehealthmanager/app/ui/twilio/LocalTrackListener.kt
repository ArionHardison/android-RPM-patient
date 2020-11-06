package com.telehealthmanager.app.ui.twilio

import com.twilio.video.LocalAudioTrack
import com.twilio.video.LocalVideoTrack
import com.twilio.video.Room

interface LocalTrackListener {
    fun onRoom(room: Room)
    fun onLocalAudioTrack(localAudioTrack: LocalAudioTrack)
    fun onCallDurationUpdate(duration: String)
}