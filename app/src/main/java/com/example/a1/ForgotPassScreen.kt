package com.example.a1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.a1.databinding.ActivityForgotPassScreenBinding

class ForgotPassScreen : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPassScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPassScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.BackArrow.setOnClickListener{
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }


        binding.LoginRedirect.setOnClickListener {
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }

    }
}