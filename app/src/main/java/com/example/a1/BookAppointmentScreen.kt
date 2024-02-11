package com.example.a1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a1.databinding.ActivityBookAppointmentScreenBinding
import com.example.a1.databinding.ActivityUserFocusedScreenBinding

class BookAppointmentScreen : AppCompatActivity() {
    private lateinit var binding : ActivityBookAppointmentScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityBookAppointmentScreenBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.BackArrow.setOnClickListener {

            val intent = Intent(this,BottomNavigationBar::class.java)
            startActivity(intent)
            finish()

        }

        binding.BookApt.setOnClickListener {

            val intent = Intent(this,BottomNavigationBar::class.java)
            startActivity(intent)
            finish()


        }




    }
}