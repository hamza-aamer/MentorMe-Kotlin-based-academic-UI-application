package com.example.a1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig
import io.agora.rtc2.video.VideoCanvas
import io.agora.rtc2.video.VideoEncoderConfiguration

class VidCallScreen : AppCompatActivity() {

    private var agoraEngine: RtcEngine? = null
    private val TAG = "VIDCALL_SCREEN"

    private val PERMISSION_REQ_ID = 22
    private val PERMISSIONS = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vid_call_screen)

        if (checkSelfPermission(PERMISSIONS, PERMISSION_REQ_ID)) {
            initializeAgoraEngine()
        }

        val close: ImageView = findViewById(R.id.closecall)
        close.setOnClickListener {
            leaveChannel()
            startActivity(Intent(this, BottomNavigationBar::class.java))
            finish()
        }
    }

    private fun initializeAgoraEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = applicationContext
            config.mAppId = "1940e52806894d419cd9e5c8ddd5bf69" // Replace with your actual App ID
            config.mEventHandler = object : IRtcEngineEventHandler() {
                // Implement callback methods of IRtcEngineEventHandler here
            }
            agoraEngine = RtcEngine.create(config)
            setupVideoConfig()
            setupLocalVideo()
            joinChannel()
        } catch (e: Exception) {
            Log.e(TAG, "Exception: ${e.message}")
        }
    }

    private fun setupVideoConfig() {
        // This is just sample video configuration. Adjust it as needed.
        agoraEngine?.enableVideo()
        agoraEngine?.setVideoEncoderConfiguration(
            VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_1280x720,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
            )
        )
    }

    private fun setupLocalVideo() {
        val localVideoContainer = findViewById<FrameLayout>(R.id.local_video_view_container)
        // This creates a VideoCanvas instance for the local video feed
        val localVideoCanvas = VideoCanvas(null, VideoCanvas.RENDER_MODE_HIDDEN, 0)
        agoraEngine?.setupLocalVideo(localVideoCanvas)
        localVideoContainer.addView(localVideoCanvas.view)
    }

    private fun joinChannel() {
        // Replace the token and channel name with your values
        val token: String? = null
        val channelName = "GlobalChannel"
        agoraEngine?.joinChannel(token, channelName, "", 0) // if you do not specify the uid, we will generate the uid for you
    }

    private fun leaveChannel() {
        agoraEngine?.leaveChannel()
    }

    private fun checkSelfPermission(permissions: Array<String>, requestCode: Int): Boolean {
        if (permissions.any {
                ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
            }) {
            ActivityCompat.requestPermissions(this, permissions, requestCode)
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQ_ID && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
            initializeAgoraEngine()
        } else {
            Toast.makeText(this, "Permissions needed for video call", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        leaveChannel()
        agoraEngine?.let {
            RtcEngine.destroy()
            agoraEngine = null
        }
    }
}
