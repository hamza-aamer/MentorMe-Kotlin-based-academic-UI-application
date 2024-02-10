package com.example.a1

import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a1.databinding.ActivityBottomNavigationBarBinding

class BottomNavigationBar : AppCompatActivity() {
    private lateinit var binding : ActivityBottomNavigationBarBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBarBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replacefrag(HomeFragment())



        binding.fab.setOnClickListener { replacefrag(AddMentorFragment()) }


        var ret: Boolean = true
        binding.bottomNavigationView.setOnItemSelectedListener {
            ret=true
            when(it.itemId){

                R.id.home-> replacefrag(HomeFragment())
                R.id.search-> replacefrag(SearchFragment())
                R.id.chat-> replacefrag(ChatFragment())
                R.id.profile-> replacefrag(ProfileFragment())
                R.id.placeholder->  ret=false
                else->{

                }

            }
            ret
        }

    }

    private fun replacefrag(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.FrameLayout, fragment)
        fragmentTransaction.commit()
    }

}