package com.example.a1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class VideoCameraScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_camera_screen)


        val close : ImageView =findViewById(R.id.close)
        close.setOnClickListener {
            val intent= Intent(this,BottomNavigationBar::class.java)
            startActivity(intent)
            finish()

        }

        val photo : TextView =findViewById(R.id.Square)
        photo.setOnClickListener {
            val intent= Intent(this,PhotoCameraScreen::class.java)
            startActivity(intent)
            finish()

        }

    }
}