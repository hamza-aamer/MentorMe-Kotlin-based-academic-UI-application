package com.example.a1

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EditProfileScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile_screen)


        val back=findViewById<TextView>(R.id.BackArrow)

        back.setOnClickListener {
            val intent = Intent(this, BottomNavigationBar::class.java)
            startActivity(intent)
            finish()
        }

        val spinner: Spinner = findViewById(R.id.CountryList)
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.Countries,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner.adapter = adapter
        }


        val spinner2: Spinner = findViewById(R.id.CityList)
        // Create an ArrayAdapter using the string array and a default spinner layout.
        ArrayAdapter.createFromResource(
            this,
            R.array.PakistaniCities,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            spinner2.adapter = adapter
        }


        val btn = findViewById<TextView>(R.id.editprofile)


        btn.setOnClickListener {

            DataManager.currentUser?.email = findViewById<EditText>(R.id.EmailText).text.toString()

            DataManager.currentUser?.name = findViewById<EditText>(R.id.NameText).text.toString()

            DataManager.currentUser?.contactNumber = findViewById<EditText>(R.id.NumberText).text.toString()
            val country = findViewById<Spinner>(R.id.CountryList).selectedItem.toString()
            val city = findViewById<Spinner>(R.id.CityList).selectedItem.toString()

            DataManager.currentUser?.location = "$city, $country"

            DataManager.updateUser(DataManager.currentUser!!)

            DataManager.currentFragment=ProfileFragment()

            val intent = Intent(this, BottomNavigationBar::class.java)
            startActivity(intent)
            finish()
        }




    }
}