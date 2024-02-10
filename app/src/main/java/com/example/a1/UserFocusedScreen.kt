package com.example.a1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a1.databinding.ActivityUserFocusedScreenBinding

class UserFocusedScreen : AppCompatActivity() {
    private lateinit var binding : ActivityUserFocusedScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserFocusedScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.BackArrow.setOnClickListener {

            val intent = Intent(this,BottomNavigationBar::class.java)
            startActivity(intent)
            finish()

        }

        binding.showBookedSessions.setOnClickListener {
            val intent = Intent(this,AddReviewScreen::class.java)
            startActivity(intent)
            finish()

        }



    }
}