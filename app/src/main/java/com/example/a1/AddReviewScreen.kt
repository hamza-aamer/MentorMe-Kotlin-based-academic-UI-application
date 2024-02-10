package com.example.a1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a1.databinding.ActivityAddReviewScreenBinding
import com.example.a1.databinding.ActivityUserFocusedScreenBinding

class AddReviewScreen : AppCompatActivity() {
    private lateinit var binding : ActivityAddReviewScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddReviewScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.BackArrow.setOnClickListener {

            val intent = Intent(this,UserFocusedScreen::class.java)
            startActivity(intent)
            finish()

        }
    }
}