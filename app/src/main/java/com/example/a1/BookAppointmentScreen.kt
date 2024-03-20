package com.example.a1

import android.content.Intent
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.example.a1.databinding.ActivityBookAppointmentScreenBinding
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookAppointmentScreen : AppCompatActivity() {
    private var time: String = ""
    private var selectedDate: String = ""

    private lateinit var binding: ActivityBookAppointmentScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookAppointmentScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)





        binding.Title2.text = MentorManager.focusedMentor!!.name
        Glide.with(binding.pic)
            .load(MentorManager.focusedMentor!!.profileImageUrl)
            .circleCrop()
            .into(binding.pic)


        binding.BackArrow.setOnClickListener {
            val intent = Intent(this, BottomNavigationBar::class.java)
            startActivity(intent)
            finish()
        }

        binding.slot1.setOnClickListener {
            time = "10:00 AM"
            updateSlotBackgrounds(binding.slot1)
        }
        binding.slot2.setOnClickListener {
            time = "11:00 AM"
            updateSlotBackgrounds(binding.slot2)
        }
        binding.slot3.setOnClickListener {
            time = "12:00 PM"
            updateSlotBackgrounds(binding.slot3)
        }

        binding.BookApt.setOnClickListener {
            if (selectedDate.isEmpty()) {
                Log.d("BookAppointmentScreen", "Date is empty")
                selectedDate = getSelectedDate()
            }
            val dateTime = "$selectedDate $time"
            val newSession = Session(
                mentorId = MentorManager.focusedMentor!!.mentorId,
                menteeId = DataManager.currentUser!!.userId,
                scheduledTime = dateTime,
                status = "scheduled"
            )

            MentorManager.focusedMentor!!.sessionsBooked.add(newSession)
            DataManager.currentUser!!.sessionsBooked.add(newSession)
            DataManager.currentUser!!.Notifications.add("Thank you for booking a session. Good Luck!")
            DataManager.updateUser(DataManager.currentUser!!)
            MentorManager.updateMentor(MentorManager.focusedMentor!!)
            val intent = Intent(this, BottomNavigationBar::class.java)
            startActivity(intent)
            finish()
        }

        binding.calendar.setOnDateChangeListener { view, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            selectedDate = dateFormat.format(calendar.time).toString()
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
    private fun updateSlotBackgrounds(clickedSlot: TextView) {
        // Update background color of all slots
        binding.slot1.background = getDrawable(R.drawable.lightgreyround)
        binding.slot2.background = getDrawable(R.drawable.lightgreyround)
        binding.slot3.background = getDrawable(R.drawable.lightgreyround)
        // Update background color of clicked slot
        clickedSlot.background = getDrawable(R.drawable.blueround)
    }

    private fun getSelectedDate(): String {
        // Extract selected date from calendar view
        val calendar = binding.calendar.date
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar)
    }
}
