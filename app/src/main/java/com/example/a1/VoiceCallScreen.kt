package com.example.a1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.agora.rtc2.IRtcEngineEventHandler
import io.agora.rtc2.RtcEngine
import io.agora.rtc2.RtcEngineConfig

class VoiceCallScreen : AppCompatActivity() {

    private var agoraEngine: RtcEngine? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voice_call_screen)

        val close: ImageView = findViewById(R.id.closecall)
        close.setOnClickListener {
            leaveChannel()
            startActivity(Intent(this, BottomNavigationBar::class.java))
            finish()
        }

        if (!checkSelfPermission()) {
            requestPermissions()
        } else {
            initializeAgoraEngine()
        }
    }

    private fun initializeAgoraEngine() {
        try {
            val config = RtcEngineConfig()
            config.mContext = applicationContext
            config.mAppId = "1940e52806894d419cd9e5c8ddd5bf69" // Replace with your actual App ID
            config.mEventHandler = object : IRtcEngineEventHandler() {}
            agoraEngine = RtcEngine.create(config)
            joinChannel()
        } catch (e: Exception) {
            Log.e("AGORA_INIT_ERROR", "Failed to initialize Agora RTC engine: ${e.message}")
        }
    }

    private fun joinChannel() {
        val channelName = "GlobalChannel"
        // The token is set to null or an empty string if you're using APP ID only for testing.
        // Remember to switch to dynamic token generation for production use!
        val token: String? = null // or "" if that suits your setup better
        val optionalInfo: String? = "" // Optional information can be an empty string
        val userId = 0 // Let Agora SDK assign a user ID automatically

        // Correctly calling joinChannel without ChannelMediaOptions
        agoraEngine?.joinChannel(token, channelName, optionalInfo, userId)
    }


    private fun leaveChannel() {
        agoraEngine?.leaveChannel()
    }

    private fun checkSelfPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.RECORD_AUDIO), PERMISSION_REQ_ID)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQ_ID -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initializeAgoraEngine()
                } else {
                    Toast.makeText(this, "Permissions needed for audio recording", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        private const val PERMISSION_REQ_ID = 22
    }

    override fun onDestroy() {
        super.onDestroy()
        agoraEngine?.let {
            it.leaveChannel()
            RtcEngine.destroy()
            agoraEngine = null
        }
    }
}
