package com.telehealthmanager.app.ui.twilio

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import com.telehealthmanager.app.ui.twilio.listener.CallHangUpListener
import com.telehealthmanager.app.ui.twilio.listener.LocalTrackListener
import com.telehealthmanager.app.ui.twilio.model.ConnectedStatus
import com.twilio.video.*
import java.util.*


object RoomManager {

    private val TAG = "RoomManager======>"

    private val LOCAL_AUDIO_TRACK_NAME = "mic"

    private var roomEventListener: Room.Listener? = null
    private var hangUpListener: CallHangUpListener? = null
    private var localTrackListener: LocalTrackListener? = null

    private var localParticipant: LocalParticipant? = null
    private var localAudioTrack: LocalAudioTrack? = null
    private var room: Room? = null

    var callDuration: String = "00:00"
    private var countDownTimer: CountDownTimer? = null

    fun connectRoom(
        context: Context,
        roomName: ConnectedStatus,
        localVideoTrack: LocalVideoTrack?
    ) {

        configureAudioTracks(context)

        val connectOptionsBuilder =
            ConnectOptions.Builder(roomName.accessToken).roomName(roomName.room_name)
        localAudioTrack?.let { connectOptionsBuilder.audioTracks(listOf(it)) }
        localVideoTrack?.let { connectOptionsBuilder.videoTracks(listOf(it)) }
        val configuration = NetworkQualityConfiguration(
            NetworkQualityVerbosity.NETWORK_QUALITY_VERBOSITY_MINIMAL,
            NetworkQualityVerbosity.NETWORK_QUALITY_VERBOSITY_MINIMAL
        )


        // DEFAULT AUDIO CODEC
        connectOptionsBuilder.preferAudioCodecs(listOf(OpusCodec()))
        // DEFAULT VIDEO CODEC
        connectOptionsBuilder.preferVideoCodecs(listOf(Vp8Codec()))
        // DEFAULT ENCODE
        connectOptionsBuilder.encodingParameters(EncodingParameters(0, 0))
        connectOptionsBuilder.enableAutomaticSubscription(true)
        connectOptionsBuilder.networkQualityConfiguration(configuration)

        room = Video.connect(context, connectOptionsBuilder.build(), object : Room.Listener {
            override fun onParticipantDisconnected(
                room: Room,
                remoteParticipant: RemoteParticipant
            ) {
                Log.d(TAG, "onParticipantDisconnected: ")
                roomEventListener?.onParticipantDisconnected(room, remoteParticipant)
                if (room.remoteParticipants.size == 0) {
                    hangUpListener?.onCallEnd(callDuration)
                    resetRoomManager()
                }
            }

            override fun onConnectFailure(room: Room, twilioException: TwilioException) {
                roomEventListener?.onConnectFailure(room, twilioException)
                resetRoomManager()
            }

            override fun onReconnected(room: Room) {
                roomEventListener?.onReconnected(room)
            }

            override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
                Log.d(TAG, "onParticipantConnected: ")
                roomEventListener?.onParticipantConnected(room, remoteParticipant)
            }

            override fun onConnected(room: Room) {
                Log.d(TAG, "onConnected: ")
                localParticipant = room.localParticipant
                roomEventListener?.onConnected(room)
               // startNotificationService(context, roomName)
            }

            override fun onDisconnected(room: Room, twilioException: TwilioException?) {
                Log.d(TAG, "onDisconnected: ")
                roomEventListener?.onDisconnected(room, twilioException)
                hangUpListener?.onCallEnd(callDuration)
                resetRoomManager()
            }

            override fun onReconnecting(room: Room, twilioException: TwilioException) {
                roomEventListener?.onReconnecting(room, twilioException)
            }

            override fun onRecordingStarted(room: Room) {

            }

            override fun onRecordingStopped(room: Room) {

            }
        })
        localTrackListener?.onRoom(room!!)
    }

    fun setLocalTrackListener(localTrackListener: LocalTrackListener) {
        RoomManager.localTrackListener = localTrackListener
    }

    fun setRoomEventListener(roomEventListener: Room.Listener) {
        RoomManager.roomEventListener = roomEventListener
    }

    fun setHangUpListener(hangUpListener: CallHangUpListener) {
        RoomManager.hangUpListener = hangUpListener
    }

    fun getRoom(): Room? {
        return room
    }

    private fun configureAudioTracks(context: Context) {
        localAudioTrack = LocalAudioTrack.create(context, true, LOCAL_AUDIO_TRACK_NAME)
        localTrackListener?.onLocalAudioTrack(localAudioTrack!!)
    }

    fun getLocalAudioTrack(): LocalAudioTrack {
        return localAudioTrack!!
    }

    fun startNotificationService(context: Context, callConnected: ConnectedStatus) {
        val intent = Intent(context, TwilioCallService::class.java)
        intent.putExtra(TwilloVideoActivity.CALL_CONNECTED_STATUS, callConnected)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun resetRoomManager() {
        localAudioTrack?.release()
        roomEventListener = null
        hangUpListener = null
        localTrackListener = null
        countDownTimer?.cancel()
        countDownTimer = null
        room?.disconnect()
        room = null
    }

    fun startCallDurationTimer() {
        if (countDownTimer == null) {
            val oneDay = (24 * 60 * 60 * 1000).toLong()
            countDownTimer = object : CountDownTimer(oneDay, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    val duration = oneDay.minus(millisUntilFinished)
                    val seconds = (duration / 1000).toInt() % 60
                    val minutes = (duration / (1000 * 60)).toInt() % 60
                    val hours = (duration / (1000 * 60 * 60)).toInt() % 24

                    callDuration = if (hours == 0) {
                        String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds)
                    } else {
                        String.format(
                            Locale.getDefault(),
                            "%02d:%02d:%02d",
                            hours,
                            minutes,
                            seconds
                        )
                    }
                    localTrackListener?.onCallDurationUpdate(callDuration)
                }

                override fun onFinish() {

                }
            }
            countDownTimer!!.start()
        }
    }


}