package com.example.a1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class VidCallScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vid_call_screen)

        val close : ImageView =findViewById(R.id.closecall)
        close.setOnClickListener {
            val intent= Intent(this,BottomNavigationBar::class.java)
            startActivity(intent)
            finish()

        }

    }
}