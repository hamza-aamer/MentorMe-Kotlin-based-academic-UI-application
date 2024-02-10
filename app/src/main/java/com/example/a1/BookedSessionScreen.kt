package com.example.a1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class BookedSessionScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booked_session_screen)

        val back=findViewById<TextView>(R.id.BackArrow)

        back.setOnClickListener {
            val intent = Intent(this, BottomNavigationBar::class.java)
            startActivity(intent)
            finish()
        }
    }
}