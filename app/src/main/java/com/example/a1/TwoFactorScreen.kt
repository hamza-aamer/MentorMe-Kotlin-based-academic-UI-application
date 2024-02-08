package com.example.a1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView

class TwoFactorScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_two_factor_screen)


        val btn2 : TextView = findViewById(R.id.BackArrow)
        btn2.setOnClickListener{
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity(intent)
            finish()
        }
    }
}