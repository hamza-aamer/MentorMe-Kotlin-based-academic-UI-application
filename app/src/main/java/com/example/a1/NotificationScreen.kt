package com.example.a1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class NotificationScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_screen)

        val button: TextView = findViewById<TextView>(R.id.BackArrow)
        button.setOnClickListener{
            val intent = Intent(this, BottomNavigationBar::class.java)
            startActivity(intent)
            finish()
        }
    }
}