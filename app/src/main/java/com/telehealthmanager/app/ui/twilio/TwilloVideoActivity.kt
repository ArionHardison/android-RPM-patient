package com.telehealthmanager.app.ui.twilio

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.telehealthmanager.app.BaseApplication
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.data.PreferenceHelper
import com.telehealthmanager.app.repositary.ApiInterface
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.VideoCallCancelResponse
import com.twilio.audioswitch.selection.AudioDevice
import com.twilio.audioswitch.selection.AudioDeviceSelector
import com.twilio.video.*
import kotlinx.android.synthetic.main.twillio_content_video.*
import kotlinx.android.synthetic.main.twillioi_video_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates


class TwilloVideoActivity : AppCompatActivity(), View.OnClickListener, Room.Listener, LocalTrackListener {
    private val LOCAL_AUDIO_TRACK_NAME = "mic"
    private val LOCAL_VIDEO_TRACK_NAME = "camera"
    private val CAMERA_MIC_PERMISSION_REQUEST_CODE = 1
    private val TAG = "FragmentActivity"

    /**
     * DRAGGABLE USER VIDEO VIEW HANDLE
     */
    private var dX = 0f
    private var dY = 0f
    private var screenWidth = 0
    private var screenHeight = 0


    private var accessToken: String? = null
    private var room: Room? = null
    private var savedVolumeControlStream by Delegates.notNull<Int>()
    private var localParticipant: LocalParticipant? = null
    private var localVideoView: VideoRenderer? = null
    private var audioManager: AudioManager? = null
    private var localAudioTrack: LocalAudioTrack? = null
    private var localVideoTrack: LocalVideoTrack? = null
    private var remoteVideoTrack: RemoteVideoTrack? = null
    private var primaryVideoTrack: VideoTrack? = null
    private var remoteParticipantIdentity: String? = null
    private var previousAudioMode = 0
    private var previousMicrophoneMute = false

    private var call_type = ""
    private var chatPath = ""
    private var id: String? = null
    private var isVideoCall = "1"
    private var mPlayer: MediaPlayer? = null
    private var preferenceHelper = PreferenceHelper(BaseApplication.baseApplication)
    private val appRepository = AppRepository()

    private lateinit var cameraCaptureCompat: CameraCaptureCompat
    private lateinit var audioDeviceSelector: AudioDeviceSelector
    private val videoConstraints = VideoConstraints.Builder()
        .maxFps(24)
        .maxVideoDimensions(VideoDimensions.VGA_VIDEO_DIMENSIONS)
        .build()

    private val availableCameraSource: CameraCapturer.CameraSource
        get() = if (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA)) CameraCapturer.CameraSource.FRONT_CAMERA else CameraCapturer.CameraSource.BACK_CAMERA


    private fun checkPermissionForCameraAndMicrophone(): Boolean {
        val resultCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val resultMic = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
        return resultCamera == PackageManager.PERMISSION_GRANTED && resultMic == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissionForCameraAndMicrophone() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
            || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECORD_AUDIO)
        ) {
            Toast.makeText(this, R.string.permissions_needed, Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO),
                CAMERA_MIC_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == CAMERA_MIC_PERMISSION_REQUEST_CODE) {
            var cameraAndMicPermissionGranted = true
            for (grantResult in grantResults) {
                cameraAndMicPermissionGranted = cameraAndMicPermissionGranted and (grantResult == PackageManager.PERMISSION_GRANTED)
            }
            if (cameraAndMicPermissionGranted) {
                createAudioAndVideoTracks()
                retrieveAccessTokenFromServer()
            } else {
                Toast.makeText(this, R.string.permissions_needed, Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun createAudioAndVideoTracks() {
        localAudioTrack = LocalAudioTrack.create(this, true, LOCAL_AUDIO_TRACK_NAME)
        cameraCaptureCompat = CameraCaptureCompat(this, availableCameraSource)
        localVideoTrack = LocalVideoTrack.create(
            this,
            true,
            cameraCaptureCompat!!.videoCapturer,
            LOCAL_VIDEO_TRACK_NAME
        )
        primaryVideoView!!.mirror = true
        localVideoTrack!!.addRenderer(primaryVideoView!!)
        localVideoView = primaryVideoView
        if (localParticipant != null) {
            localParticipant!!.publishTrack(localVideoTrack!!)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.twillioi_video_activity)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels
        chatPath = intent.getStringExtra("chat_path") as String
        id = intent.getStringExtra("sender") as String
        isVideoCall = intent.getStringExtra("is_video") as String

        initDisplay()
        cameraCaptureCompat = CameraCaptureCompat(this, availableCameraSource)
        savedVolumeControlStream = volumeControlStream
        volumeControlStream = AudioManager.STREAM_VOICE_CALL
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager!!.isSpeakerphoneOn = true

        if (localVideoTrack == null) {
            localVideoTrack = LocalVideoTrack.create(
                this,
                true,
                cameraCaptureCompat.videoCapturer,
                videoConstraints
            )
        }
      //  playRingTone()
        if (!checkPermissionForCameraAndMicrophone()) {
            requestPermissionForCameraAndMicrophone()
        } else {
            createAudioAndVideoTracks()
            retrieveAccessTokenFromServer()
        }

    }

    private fun initDisplay() {
        audioDeviceSelector = AudioDeviceSelector(applicationContext)
        audioDeviceSelector.start { audioDevices, audioDevice ->
            updateAudioDeviceIcon(audioDevice)
        }
        audioDeviceSelector.activate()
        audioDeviceSelector.availableAudioDevices.find { it is AudioDevice.Earpiece }?.let { audioDeviceSelector.selectDevice(it) }
        setPrimaryVideoTrack(localVideoTrack)
        localVideoTrack?.enable(true)
        audioDeviceSelector.availableAudioDevices.find { it is AudioDevice.Speakerphone }?.let { audioDeviceSelector.selectDevice(it) }

        faHangUp.setOnClickListener(this)
        secondaryVideoView.setOnClickListener(this)
        fabSwitchCamera.setOnClickListener(this)
        fabSpeaker.setOnClickListener(this)
        fabVideo.setOnClickListener(this)
        fabMic.setOnClickListener(this)
    }

    private fun updateAudioDeviceIcon(selectedAudioDevice: AudioDevice?) {
        val audioIcon = when (selectedAudioDevice) {
            is AudioDevice.BluetoothHeadset -> R.drawable.ic_phone_bluetooth_speaker
            is AudioDevice.WiredHeadset -> R.drawable.ic_headset
            is AudioDevice.Speakerphone -> R.drawable.ic_volume_up
            else -> R.drawable.ic_volume_mute
        }
        flipAnimateSetImageToFabButton(fabSpeaker!!, audioIcon)
    }

    private fun flipAnimateSetImageToFabButton(view: View, resImage: Int) {
        val objectAnimator1 = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
        val objectAnimator2 = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        objectAnimator1.duration = 250
        objectAnimator2.duration = 250
        objectAnimator1.interpolator = DecelerateInterpolator()
        objectAnimator2.interpolator = AccelerateDecelerateInterpolator()
        objectAnimator1.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                super.onAnimationEnd(animation)
                (view as FloatingActionButton).setImageResource(resImage)
                objectAnimator2.start()
            }
        })
        objectAnimator1.start()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.faHangUp -> {
                // stopCallReceiveService()
                stopRingTone()
                declinedCall()
                // stopNotificationService()
            }

            R.id.fabSwitchCamera -> {
                val cameraSource = cameraCaptureCompat.cameraSource
                cameraCaptureCompat.switchCamera()
                if (secondaryVideoView!!.visibility == View.VISIBLE) {
                    secondaryVideoView!!.mirror = cameraSource == CameraCapturer.CameraSource.BACK_CAMERA
                } else {
                    primaryVideoView!!.mirror = cameraSource == CameraCapturer.CameraSource.BACK_CAMERA
                }
            }
            R.id.fabSpeaker -> {
                var device: AudioDevice = audioDeviceSelector.selectedAudioDevice!!
                if (audioDeviceSelector.selectedAudioDevice is AudioDevice.Speakerphone) {
                    audioDeviceSelector.availableAudioDevices.find {
                        it is AudioDevice.Earpiece
                    }?.let {
                        audioDeviceSelector.selectDevice(it)
                        device = it
                    }
                } else {
                    audioDeviceSelector.availableAudioDevices.find {
                        it is AudioDevice.Speakerphone
                    }?.let {
                        audioDeviceSelector.selectDevice(it)
                        device = it
                    }
                }
                updateAudioDeviceIcon(device)
            }
            R.id.fabVideo -> {
                localVideoTrack?.let {
                    val enable = !it.isEnabled
                    it.enable(enable)
                    if (enable) {
                        flipAnimateSetImageToFabButton(view, R.drawable.ic_videocam_on)
                    } else {
                        flipAnimateSetImageToFabButton(view, R.drawable.ic_videocam_off)
                    }
                }
            }
            R.id.fabMic -> {
                localAudioTrack?.let {
                    val enable = !it.isEnabled
                    it.enable(enable)
                    if (enable)
                        flipAnimateSetImageToFabButton(view, R.drawable.ic_mic_black)
                    else
                        flipAnimateSetImageToFabButton(view, R.drawable.ic_mic_off_black)
                }
            }
        }
    }


    private fun playRingTone() {
        mPlayer = MediaPlayer.create(this, R.raw.phone_ringing)
        mPlayer!!.start()
        mPlayer!!.setOnCompletionListener {
            mPlayer!!.release()
            if (room == null) {
                resetRoomManager()
            } else if ((room != null && room!!.state == Room.State.CONNECTING) ||
                (room != null && room!!.state == Room.State.CONNECTED && room!!.remoteParticipants.size == 0)
            ) {
                RoomManager.resetRoomManager()
                resetRoomManager()
            }
        }
    }

    private fun stopRingTone() {
        try {
            if (mPlayer != null && mPlayer!!.isPlaying) {
                mPlayer!!.stop()
            }
        } catch (e: IllegalStateException) {
            e.printStackTrace();
        }
    }

    fun resetRoomManager() {
        stopRingTone()
        declinedCall()
    }


    private fun setPrimaryVideoTrack(videoTrack: VideoTrack?) {
        primaryVideoTrack?.removeRenderer(primaryVideoView)
        if (videoTrack != null) {
            primaryVideoTrack = videoTrack
            primaryVideoTrack!!.addRenderer(primaryVideoView)
            localVideoView = primaryVideoView
        }
    }

    private fun retrieveAccessTokenFromServer() {
        val call: Call<AccessToken> = appRepository.createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java).getTwilloVideoToken(chatPath)
        call.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    this@TwilloVideoActivity.accessToken = response.body()!!.accessToken
                    // TwilloVideoActivity.this.accessToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiIsImN0eSI6InR3aWxpby1mcGE7dj0xIn0.eyJqdGkiOiJTS2Y1YmQxZGM1NjQ5MTc0MjA4YWRlODI3MmRmNTdlN2NlLTE1NzExMzMxNDIiLCJpc3MiOiJTS2Y1YmQxZGM1NjQ5MTc0MjA4YWRlODI3MmRmNTdlN2NlIiwic3ViIjoiQUNmNDE3YmZkMmY0NGY1ZWJlNjMwOGUzNmIzNGRjNWY5MiIsImV4cCI6MTU3MTEzNjc0MiwiZ3JhbnRzIjp7ImlkZW50aXR5IjoidXNlcl9TYW50aG9zaCIsInZpZGVvIjp7InJvb20iOiIyX2NoYXRzXzEyIn19fQ.jI1mCwN3WvkIX2sF-yuWAnbLqZq15ogZJAoG5U4jJ8s";
                    Log.d(TAG, "token: " + this@TwilloVideoActivity.accessToken)
                    connectToRoom(this@TwilloVideoActivity.accessToken)
                } else {
                    Toast.makeText(
                        this@TwilloVideoActivity,
                        R.string.error_retrieving_access_token, Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {

            }
        })
    }

    private fun declinedCall() {
        val call = appRepository.createApiClient(
            BuildConfig.BASE_URL,
            ApiInterface::class.java
        ).cancelVideoCall(chatPath)
        call!!.enqueue(
            object : Callback<VideoCallCancelResponse?> {
                override fun onResponse(call: Call<VideoCallCancelResponse?>, response: Response<VideoCallCancelResponse?>) {
                    if (room != null) {
                        room!!.disconnect()
                        audioDeviceSelector.stop()
                        volumeControlStream = savedVolumeControlStream
                        localAudioTrack?.release()
                        localVideoTrack?.release()
                    }
                    finish()
                }

                override fun onFailure(call: Call<VideoCallCancelResponse?>, t: Throwable) {}
            })
    }


    private fun connectToRoom(accessToken: String?) {
        configureAudio(true)
        RoomManager.setRoomEventListener(this)
        RoomManager.setLocalTrackListener(this)
        val connectedStatus = accessToken?.let { ConnectedStatus(chatPath, it) }
        RoomManager.connectRoom(this, connectedStatus!!, localVideoTrack)
        val cameraSource = cameraCaptureCompat.cameraSource
        primaryVideoView.mirror = cameraSource == CameraCapturer.CameraSource.BACK_CAMERA
    }

    private fun configureAudio(enable: Boolean) {
        if (enable) {
            previousAudioMode = audioManager!!.mode
            requestAudioFocus()
            audioManager!!.mode = AudioManager.MODE_IN_COMMUNICATION
            previousMicrophoneMute = audioManager!!.isMicrophoneMute
            audioManager!!.isMicrophoneMute = false
        } else {
            audioManager!!.mode = previousAudioMode
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                audioManager!!.abandonAudioFocus(null)
            }
            audioManager!!.isMicrophoneMute = previousMicrophoneMute
        }
    }

    private fun requestAudioFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val playbackAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_VOICE_COMMUNICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            val focusRequest = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN_TRANSIENT)
                .setAudioAttributes(playbackAttributes)
                .setAcceptsDelayedFocusGain(true)
                .setOnAudioFocusChangeListener { i: Int -> }.build()
            audioManager!!.requestAudioFocus(focusRequest)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                audioManager!!.requestAudioFocus(
                    null,
                    AudioManager.STREAM_VOICE_CALL,
                    AudioManager.AUDIOFOCUS_GAIN_TRANSIENT
                )
            }
        }
    }

    private fun remoteParticipantListener(): RemoteParticipant.Listener {
        return object : RemoteParticipant.Listener {
            override fun onAudioTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                videoStatusTextView!!.text = "onAudioTrackPublished"
                Log.e(TAG, "onAudioTrackPublished")
            }

            override fun onAudioTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                videoStatusTextView!!.text = "onAudioTrackUnpublished"
            }

            override fun onDataTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication
            ) {
                videoStatusTextView!!.text = "onDataTrackPublished"
            }

            override fun onDataTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication
            ) {
                videoStatusTextView!!.text = "onDataTrackUnpublished"
            }

            override fun onVideoTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
            }

            override fun onVideoTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                videoStatusTextView!!.text = "onVideoTrackUnpublished"
            }

            override fun onAudioTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                remoteAudioTrack: RemoteAudioTrack
            ) {
                videoStatusTextView!!.text = "onAudioTrackSubscribed"
            }

            override fun onAudioTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                remoteAudioTrack: RemoteAudioTrack
            ) {
                videoStatusTextView!!.text = "onAudioTrackUnsubscribed"
            }

            override fun onAudioTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                twilioException: TwilioException
            ) {
                videoStatusTextView!!.text = "onAudioTrackSubscriptionFailed"
            }

            override fun onDataTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                remoteDataTrack: RemoteDataTrack
            ) {
                videoStatusTextView!!.text = "onDataTrackSubscribed"
            }

            override fun onDataTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                remoteDataTrack: RemoteDataTrack
            ) {
                videoStatusTextView!!.text = "onDataTrackUnsubscribed"
            }

            override fun onDataTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                twilioException: TwilioException
            ) {
                videoStatusTextView!!.text = "onDataTrackSubscriptionFailed"
            }

            override fun onVideoTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                videoStatusTextView!!.text = "onVideoTrackSubscribed"
                addRemoteParticipantVideo(remoteVideoTrack)
            }

            override fun onVideoTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                videoStatusTextView!!.text = "onVideoTrackUnsubscribed"
                removeRemoteParticipant(remoteParticipant)
            }

            override fun onVideoTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                twilioException: TwilioException
            ) {
                videoStatusTextView!!.text = "onVideoTrackSubscriptionFailed"
                // Snackbar.make(fabDisconnectCall!!, String.format("Failed to subscribe to %s video track", remoteParticipant.identity), Snackbar.LENGTH_LONG).show()
            }

            override fun onAudioTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {

            }

            override fun onAudioTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {

            }

            override fun onVideoTrackEnabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.e(TAG, "onVideoTrackEnabled")
                remoteVideoTrack = remoteVideoTrackPublication.remoteVideoTrack
                //  trackEnableDisable()
            }

            override fun onVideoTrackDisabled(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication
            ) {
                Log.e(TAG, "onVideoTrackDisabled")
                remoteVideoTrack = remoteVideoTrackPublication.remoteVideoTrack
            }
        }
    }

    private fun addRemoteParticipantVideo(videoTrack: VideoTrack?) {
        moveLocalVideoToThumbnailView()
        primaryVideoView!!.mirror = false
        videoTrack!!.addRenderer(primaryVideoView!!)
    }

    private fun moveLocalVideoToThumbnailView() {
        if (secondaryVideoView.visibility == View.GONE) {
            secondaryVideoView!!.visibility = View.VISIBLE
            with(localVideoTrack) {
                this?.removeRenderer(primaryVideoView)
                this?.addRenderer(secondaryVideoView)
            }
            localVideoView = secondaryVideoView
            secondaryVideoView!!.mirror =
                cameraCaptureCompat.cameraSource == CameraCapturer.CameraSource.FRONT_CAMERA
        }
    }

    private fun removeRemoteParticipant(remoteParticipant: RemoteParticipant) {
        videoStatusTextView!!.text = "RemoteParticipant " + remoteParticipant.identity + " left."
        finish()
        if (remoteParticipant.identity != remoteParticipantIdentity) {
            return
        }
        if (!remoteParticipant.remoteVideoTracks.isEmpty()) {
            val remoteVideoTrackPublication = remoteParticipant.remoteVideoTracks[0]
            if (remoteVideoTrackPublication.isTrackSubscribed) {
                removeParticipantVideo(remoteVideoTrackPublication.remoteVideoTrack)
            }
        }
    }

    private fun removeParticipantVideo(videoTrack: VideoTrack?) {
        videoTrack!!.removeRenderer(primaryVideoView!!)
    }

    private fun reconnectRemoteParticpents(room: Room) {
        for (remoteParticipant in room.remoteParticipants) {
            addRemoteParticipant(remoteParticipant)
            break
        }
    }

    private fun addRemoteParticipant(remoteParticipant: RemoteParticipant) {
        remoteParticipantIdentity = remoteParticipant.identity
        videoStatusTextView!!.text = "RemoteParticipant $remoteParticipantIdentity joined"
        if (remoteParticipant.remoteVideoTracks.size > 0) {
            val remoteVideoTrackPublication = remoteParticipant.remoteVideoTracks[0]
            remoteVideoTrack = remoteVideoTrackPublication.remoteVideoTrack
            if (remoteVideoTrackPublication.isTrackSubscribed) {
                addRemoteParticipantVideo(remoteVideoTrackPublication.remoteVideoTrack)
            }
        }
        remoteParticipant.setListener(remoteParticipantListener())
    }

    override fun onResume() {
        super.onResume()
        localVideoTrack =
            LocalVideoTrack.create(this, true, cameraCaptureCompat.videoCapturer, videoConstraints)
        room = RoomManager.getRoom()
        if (room != null) {
            localParticipant = room!!.localParticipant
            localParticipant?.setEncodingParameters(EncodingParameters(0, 0))
        }
    }

    override fun onDestroy() {
        audioDeviceSelector.deactivate()
        audioDeviceSelector.stop()
        volumeControlStream = savedVolumeControlStream
        super.onDestroy()
    }

    override fun onRoom(room: Room) {
        this.room = room
    }

    override fun onLocalAudioTrack(localAudioTrack: LocalAudioTrack) {
        this.localAudioTrack = localAudioTrack
    }

    override fun onCallDurationUpdate(duration: String) {
        videoStatusTextView.text = duration
        videoStatusTextView.visibility = View.GONE
        viewConnectedCall.visibility = View.VISIBLE
    }

    override fun onRecordingStopped(room: Room) {

    }

    override fun onParticipantDisconnected(room: Room, remoteParticipant: RemoteParticipant) {
        finish()
    }

    override fun onRecordingStarted(room: Room) {

    }

    override fun onConnectFailure(room: Room, twilioException: TwilioException) {
        showToast("Connection failure! ${twilioException.message}")
        finish()
    }

    override fun onReconnected(room: Room) {
        reconnectRemoteParticpents(room)
    }

    override fun onParticipantConnected(room: Room, remoteParticipant: RemoteParticipant) {
        stopRingTone()
        addRemoteParticipant(remoteParticipant)
        RoomManager.startCallDurationTimer()
    }


    override fun onConnected(room: Room) {
        localParticipant = room.localParticipant

        for (remoteParticipant in room.remoteParticipants) {
            addRemoteParticipant(remoteParticipant)
            break
        }

        // WHEN AUDIO CALL DISABLE VIDEO TRACK
        if (call_type == "audio_call") {
            localVideoTrack?.let {
                if (it.isEnabled) {
                    it.enable(false)
                }
            }
        } else {
            localVideoTrack?.let {
                if (it.isEnabled) {
                    it.enable(true)
                }
            }
        }

        if (room.remoteParticipants.size == 0)
            videoStatusTextView.text = "Connecting..."
        else {
            RoomManager.startCallDurationTimer()
        }
    }

    override fun onDisconnected(room: Room, twilioException: TwilioException?) {
        localParticipant = null
        videoStatusTextView!!.text = "Disconnected from " + room.name
        Log.e(TAG, "onDisconnected")
        finish()
    }

    override fun onReconnecting(room: Room, twilioException: TwilioException) {
        showToast("Reconnecting...")
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


}