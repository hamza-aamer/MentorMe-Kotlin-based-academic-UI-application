package com.example.a1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a1.databinding.ActivityForgotPassScreenBinding
import com.example.a1.databinding.ActivityResetPassScreenBinding

class ResetPassScreen : AppCompatActivity() {
    private lateinit var binding: ActivityResetPassScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPassScreenBinding.inflate(layoutInflater)
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