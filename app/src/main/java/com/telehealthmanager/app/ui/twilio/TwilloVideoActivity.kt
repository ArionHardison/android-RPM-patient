package com.telehealthmanager.app.ui.twilio

import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.PermissionChecker
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.telehealthmanager.app.BuildConfig
import com.telehealthmanager.app.R
import com.telehealthmanager.app.fcm.ReceiverPushResponse
import com.telehealthmanager.app.repositary.ApiInterface
import com.telehealthmanager.app.repositary.AppRepository
import com.telehealthmanager.app.repositary.model.VideoCallCancelResponse
import com.twilio.audioswitch.selection.AudioDevice
import com.twilio.audioswitch.selection.AudioDeviceSelector
import com.twilio.video.*
import kotlinx.android.synthetic.main.twillio_content_video.*
import kotlinx.android.synthetic.main.twillio_incoming_view.*
import kotlinx.android.synthetic.main.twillioi_video_activity.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.properties.Delegates


class TwilloVideoActivity : AppCompatActivity(), View.OnClickListener, Room.Listener, LocalTrackListener {
    private val LOCAL_AUDIO_TRACK_NAME = "mic"
    private val LOCAL_VIDEO_TRACK_NAME = "camera"
    private val CAMERA_MIC_PERMISSION_REQUEST_CODE = 1
    private val TAG = "FragmentActivity"

    companion object {
        const val CALL_REQUEST = "CALL_REQUEST"
        const val CALL_CONNECTED_STATUS = "CALL_STATUS"
        const val RECEIVE_PUSH_INFO = "RECEIVE_PUSH_INFO"
        const val ROOM_INFO = "ROOM_INFO"
        private const val PERMISSIONS_REQUEST_CODE = 35
        const val MISSED_CALL = "MISSED_CALL"
        const val END_CALL = "END_CALL"
        const val DECLINE = "DECLINE"
        const val ACTION_CALL_OTHER_END_STATUS = "ACTION_CALL_OTHER_END_STATUS"
        const val ACTION_CALL_ROOM_NAME = "ACTION_CALL_ROOM_NAME"
        const val ACTION_CALL_STATUS = "ACTION_CALL_STATUS"
    }

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
    private var callerName = ""
    private var id: String? = null
    private var hospital_id: String? = null
    private var isCallAccepted = false
    private var mPlayer: MediaPlayer? = null
    private var pushRequest: ReceiverPushResponse? = null
    private var callConnectedStatus: ConnectedStatus? = null
    private var callRequest: CallRequest? = null

    private val appRepository = AppRepository()

    private lateinit var cameraCaptureCompat: CameraCaptureCompat
    private lateinit var audioDeviceSelector: AudioDeviceSelector
    private val videoConstraints = VideoConstraints.Builder()
        .maxFps(24)
        .maxVideoDimensions(VideoDimensions.VGA_VIDEO_DIMENSIONS)
        .build()


    private fun getAvailableCameraSource(): CameraCapturer.CameraSource {
        return if (CameraCapturer.isSourceAvailable(CameraCapturer.CameraSource.FRONT_CAMERA))
            CameraCapturer.CameraSource.FRONT_CAMERA
        else
            CameraCapturer.CameraSource.BACK_CAMERA
    }


    private fun isPermissionEnabled(): Boolean {
        return (PermissionChecker.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
                == PermissionChecker.PERMISSION_GRANTED) &&
                (PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PermissionChecker.PERMISSION_GRANTED)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermissionForCameraAndMicrophone() {
        requestPermissions(
            arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA
            ), PERMISSIONS_REQUEST_CODE
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size == 2) {
            createAudioAndVideoTracks()
            initDisplay()
        } else {
            requestPermissionForCameraAndMicrophone()
        }
    }


    private fun createAudioAndVideoTracks() {
        if (localVideoTrack == null) {
            localVideoTrack = LocalVideoTrack.create(
                this,
                true,
                cameraCaptureCompat.videoCapturer,
                videoConstraints
            )
        }

        savedVolumeControlStream = volumeControlStream
        volumeControlStream = AudioManager.STREAM_VOICE_CALL
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        audioManager!!.isSpeakerphoneOn = true

        audioDeviceSelector = AudioDeviceSelector(applicationContext)
        audioDeviceSelector.start { audioDevices, audioDevice ->
            updateAudioDeviceIcon(audioDevice)
        }
        audioDeviceSelector.activate()
        audioDeviceSelector.availableAudioDevices.find { it is AudioDevice.Earpiece }?.let { audioDeviceSelector.selectDevice(it) }
        setPrimaryVideoTrack(localVideoTrack)
        localVideoTrack?.enable(true)
        audioDeviceSelector.availableAudioDevices.find { it is AudioDevice.Speakerphone }?.let { audioDeviceSelector.selectDevice(it) }

        localAudioTrack = LocalAudioTrack.create(this, true, LOCAL_AUDIO_TRACK_NAME)
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController!!.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON)

        cameraCaptureCompat = CameraCaptureCompat(this, getAvailableCameraSource())
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenHeight = displayMetrics.heightPixels
        screenWidth = displayMetrics.widthPixels

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (isPermissionEnabled()) {
                createAudioAndVideoTracks()
                initDisplay()
            } else {
                requestPermissionForCameraAndMicrophone()
            }
        } else {
            createAudioAndVideoTracks()
            initDisplay()
        }
    }

    private fun initDisplay() {
        pushRequest = intent.getParcelableExtra(RECEIVE_PUSH_INFO)
        callConnectedStatus = intent.getParcelableExtra(CALL_CONNECTED_STATUS)
        callRequest = intent.getParcelableExtra(CALL_REQUEST)
        if (callConnectedStatus != null) {
            room = RoomManager.getRoom()
            if (room !== null) {
                incomingAlert.visibility = View.GONE
                RoomManager.setRoomEventListener(this)
                RoomManager.setLocalTrackListener(this)
                localAudioTrack = RoomManager.getLocalAudioTrack()
                localParticipant = room!!.localParticipant
                if (localVideoTrack != null) {
                    localVideoTrack?.let { localParticipant?.unpublishTrack(it) }
                } else {
                    localVideoTrack?.let { localParticipant?.publishTrack(it) }
                }
                localVideoTrack?.enable(true)
            } else {
                prepareRoomManager()
            }
        } else if (pushRequest != null) {
            val isAccept = intent.getBooleanExtra(CallReceiveService.CALL_IS_ACCEPTED, false)
            isCallAccepted = pushRequest!!.connectedCall
            chatPath = pushRequest!!.room_id.toString()
            id = pushRequest!!.sender_id.toString()
            callerName = pushRequest!!.name.toString()
            if (isAccept) {
                onCallPicked()
                updateCall()
            } else {
                incomingAlert.visibility = View.VISIBLE
            }
        } else if (callRequest != null) {
            chatPath = callRequest!!.room_id
            id = callRequest!!.patient_id
            hospital_id = callRequest!!.hospital_id
            incomingAlert.visibility = View.GONE
            playRingTone()
            updateCall()
        }

        setNameAndImage();
        faHangUp.setOnClickListener(this)
        secondaryVideoView.setOnClickListener(this)
        fabSwitchCamera.setOnClickListener(this)
        fabSpeaker.setOnClickListener(this)
        fabVideo.setOnClickListener(this)
        fabMic.setOnClickListener(this)
        imgEndCall.setOnClickListener(this)
        imgAcceptCall.setOnClickListener(this)
    }

    private fun onCallPicked() {
        stopService(Intent(this, CallReceiveService::class.java))
        incomingAlert.visibility = View.GONE
        pushRequest!!.connectedCall = true
        tvCallStatus.text = "Connecting..."
    }


    private fun stopNotificationService() {
        stopService(Intent(this, TwilioCallService::class.java))
    }

    private fun isMyServiceRunning(): Boolean {
        val manager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (CallReceiveService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }

    private fun stopCallReceiveService() {
        if (isMyServiceRunning()) {
            val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            sendBroadcast(it)
            stopService(Intent(this, CallReceiveService::class.java))
        }
    }

    private fun setNameAndImage() {
        lblName.text = callerName
        tvCallStatus.text = getString(R.string.call_connecting)
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
            R.id.imgAcceptCall -> {
                stopCallReceiveService()
                onCallPicked()
                updateCall()
            }

            R.id.imgEndCall -> {
                stopCallReceiveService()
                declinedCall()
            }

            R.id.faHangUp -> {
                stopRingTone()
                declinedCall()
                stopNotificationService()
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

    private fun resetRoomManager() {
        stopNotificationService()
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


    private fun updateCall() {
        val call: Call<AccessToken>
        val hashMap: HashMap<String, Any> = HashMap()
        hashMap["room_id"] = chatPath
        hashMap["video"] = "1"
        if (callRequest != null) {
            hashMap["hospital_id"] = hospital_id.toString()
            hashMap["patient_id"] = id.toString()
            hashMap["push_to"] = "hospital"
            call = appRepository.createApiClient(ApiInterface::class.java).getCallRequest(hashMap)
        } else {
            hashMap["video"] = "1"
            call = appRepository.createApiClient(ApiInterface::class.java).getTwilloVideoToken(hashMap)
        }
        call.enqueue(object : Callback<AccessToken> {
            override fun onResponse(call: Call<AccessToken>, response: Response<AccessToken>) {
                if (response.isSuccessful) {
                    this@TwilloVideoActivity.accessToken = response.body()!!.accessToken
                    Log.d(TAG, "token: " + this@TwilloVideoActivity.accessToken)
                    connectToRoom(this@TwilloVideoActivity.accessToken)
                } else {
                    Toast.makeText(this@TwilloVideoActivity, R.string.error_retrieving_access_token, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<AccessToken>, t: Throwable) {

            }
        })
    }

    private fun declinedCall() {
        val call = appRepository.createApiClient(BuildConfig.BASE_URL, ApiInterface::class.java).cancelVideoCall(chatPath)
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
        val connectedStatus = ConnectedStatus(chatPath, accessToken!!, true)
        RoomManager.connectRoom(this, connectedStatus!!, localVideoTrack)
        val cameraSource = cameraCaptureCompat.cameraSource
        primaryVideoView.mirror = cameraSource == CameraCapturer.CameraSource.BACK_CAMERA
    }


    //WHEN API ROOM RESPONSE AVAILABLE
    private fun prepareRoomManager() {
        if (incomingAlert.visibility == View.VISIBLE) {
            incomingAlert.visibility = View.GONE
        }
        RoomManager.setRoomEventListener(this)
        RoomManager.setLocalTrackListener(this)
        RoomManager.connectRoom(this, callConnectedStatus!!, localVideoTrack)
        val cameraSource = cameraCaptureCompat.cameraSource
        primaryVideoView.mirror = cameraSource == CameraCapturer.CameraSource.BACK_CAMERA
        secondaryVideoView.visibility = View.VISIBLE
        setPrimaryVideoTrack(localVideoTrack)
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
                tvCallStatus!!.text = "onAudioTrackPublished"
                Log.e(TAG, "onAudioTrackPublished")
            }

            override fun onAudioTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication
            ) {
                tvCallStatus!!.text = "onAudioTrackUnpublished"
            }

            override fun onDataTrackPublished(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication
            ) {
                tvCallStatus!!.text = "onDataTrackPublished"
            }

            override fun onDataTrackUnpublished(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication
            ) {
                tvCallStatus!!.text = "onDataTrackUnpublished"
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
                tvCallStatus!!.text = "onVideoTrackUnpublished"
            }

            override fun onAudioTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                remoteAudioTrack: RemoteAudioTrack
            ) {
                tvCallStatus!!.text = "onAudioTrackSubscribed"
            }

            override fun onAudioTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                remoteAudioTrack: RemoteAudioTrack
            ) {
                tvCallStatus!!.text = "onAudioTrackUnsubscribed"
            }

            override fun onAudioTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteAudioTrackPublication: RemoteAudioTrackPublication,
                twilioException: TwilioException
            ) {
                tvCallStatus!!.text = "onAudioTrackSubscriptionFailed"
            }

            override fun onDataTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                remoteDataTrack: RemoteDataTrack
            ) {
                tvCallStatus!!.text = "onDataTrackSubscribed"
            }

            override fun onDataTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                remoteDataTrack: RemoteDataTrack
            ) {
                tvCallStatus!!.text = "onDataTrackUnsubscribed"
            }

            override fun onDataTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteDataTrackPublication: RemoteDataTrackPublication,
                twilioException: TwilioException
            ) {
                tvCallStatus!!.text = "onDataTrackSubscriptionFailed"
            }

            override fun onVideoTrackSubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                tvCallStatus!!.text = "onVideoTrackSubscribed"
                addRemoteParticipantVideo(remoteVideoTrack)
            }

            override fun onVideoTrackUnsubscribed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                remoteVideoTrack: RemoteVideoTrack
            ) {
                tvCallStatus!!.text = "onVideoTrackUnsubscribed"
                removeRemoteParticipant(remoteParticipant)
            }

            override fun onVideoTrackSubscriptionFailed(
                remoteParticipant: RemoteParticipant,
                remoteVideoTrackPublication: RemoteVideoTrackPublication,
                twilioException: TwilioException
            ) {
                tvCallStatus!!.text = "onVideoTrackSubscriptionFailed"
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
        tvCallStatus!!.text = "RemoteParticipant " + remoteParticipant.identity + " left."
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
        tvCallStatus!!.text = "RemoteParticipant $remoteParticipantIdentity joined"
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
        /*localVideoTrack =
            LocalVideoTrack.create(this, true, cameraCaptureCompat.videoCapturer, videoConstraints)*/
        room = RoomManager.getRoom()
        if (room != null) {
            localParticipant = room!!.localParticipant
            localParticipant?.setEncodingParameters(EncodingParameters(0, 0))
            if (localVideoTrack != null) {
                localVideoTrack?.let { localParticipant?.unpublishTrack(it) }
            } else
                localVideoTrack?.let { localParticipant?.publishTrack(it) }
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
        tvCallStatus.text = duration
        tvCallStatus.visibility = View.GONE
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
            tvCallStatus.text = "Connecting..."
        else {
            RoomManager.startCallDurationTimer()
        }
    }

    override fun onDisconnected(room: Room, twilioException: TwilioException?) {
        localParticipant = null
        tvCallStatus!!.text = "Disconnected from " + room.name
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