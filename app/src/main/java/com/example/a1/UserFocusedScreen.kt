package com.example.a1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.a1.databinding.ActivityUserFocusedScreenBinding

class UserFocusedScreen : AppCompatActivity() {
    private lateinit var binding : ActivityUserFocusedScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserFocusedScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.Title2.setText(MentorManager.focusedMentor!!.name)
        binding.Title3.setText(MentorManager.focusedMentor!!.role)
        binding.aboutme.setText(MentorManager.focusedMentor!!.mentorDesc)

        Glide.with(binding.pic)
            .load(MentorManager.focusedMentor!!.profileImageUrl)
            .circleCrop()
            .into(binding.pic)
        binding.BackArrow.setOnClickListener {

            val intent = Intent(this,BottomNavigationBar::class.java)
            startActivity(intent)
            finish()

        }

        binding.addreview.setOnClickListener {
            val intent = Intent(this,AddReviewScreen::class.java)
            startActivity(intent)
            finish()

        }

        binding.booksession.setOnClickListener {

            val intent = Intent(this,BookAppointmentScreen::class.java)
            startActivity(intent)
            finish()


        }



    }

}