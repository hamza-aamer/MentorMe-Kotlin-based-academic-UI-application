package com.example.a1

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a1.databinding.ActivityAddReviewScreenBinding

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
        binding.SubmitFeedback.setOnClickListener {
            val newreview = Review(
                userId = DataManager.currentUser!!.userId,
                targetUserName = MentorManager.focusedMentor!!.name,
                rating = binding.ratingBar.rating,
                comment = binding.experiencetext.text.toString()
            )
            DataManager.currentUser!!.reviewsGiven.add(newreview)
            DataManager.updateUser(DataManager.currentUser!!)
            val intent = Intent(this,UserFocusedScreen::class.java)
            startActivity(intent)
            finish()
        }
    }
}