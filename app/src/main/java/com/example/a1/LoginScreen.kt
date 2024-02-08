package com.example.a1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class LoginScreen : AppCompatActivity() {
    @SuppressLint("WrongViewCast")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
        val Register: TextView = findViewById(R.id.RegisterRedirect)
        Register.setOnClickListener {
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity(intent)
            finish()
        }
    }
}