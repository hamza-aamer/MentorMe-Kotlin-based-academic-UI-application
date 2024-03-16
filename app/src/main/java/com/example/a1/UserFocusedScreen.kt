package com.example.a1

import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.example.a1.databinding.ActivityUserFocusedScreenBinding
import java.net.HttpURLConnection
import java.net.URL

class UserFocusedScreen : AppCompatActivity() {
    private lateinit var binding : ActivityUserFocusedScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityUserFocusedScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.Title2.setText(MentorManager.focusedMentor!!.name)
        binding.Title3.setText(MentorManager.focusedMentor!!.role)
        binding.aboutme.setText(MentorManager.focusedMentor!!.mentorDesc)

        downloadAndSetImage(binding.pic,MentorManager.focusedMentor!!.profileImageUrl)
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
    fun downloadAndSetImage(imageView: ImageView, imageUrl: String) {
        Thread {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)
                val roundedBitmapWrapper: RoundedBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(Resources.getSystem(), bitmap)
                roundedBitmapWrapper.isCircular = true




                // Use a Handler to post the result back to the main thread
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageDrawable(roundedBitmapWrapper)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle exceptions or errors as necessary
            }
        }.start()
    }
}