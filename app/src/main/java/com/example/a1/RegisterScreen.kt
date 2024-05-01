package com.example.a1

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.UUID


class RegisterScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_screen)


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


        val btn : ImageButton = findViewById(R.id.signupbtn)
        btn.setOnClickListener{

            val email = findViewById<EditText>(R.id.EmailText).text.toString()
            val password = findViewById<EditText>(R.id.PasswordText).text.toString()
            val name = findViewById<EditText>(R.id.NameText).text.toString()
            val contactNumber = findViewById<EditText>(R.id.NumberText).text.toString()
            val country = findViewById<Spinner>(R.id.CountryList).selectedItem.toString()
            val city = findViewById<Spinner>(R.id.CityList).selectedItem.toString()

            val newUser = User(
                userId = generateUniqueUserId(), // UserID should be generated or set in a way that makes sense for your application, possibly using Firebase Auth UID
                email = email,
                name = name,
                isMentor = false, // or true, based on your application logic
                // Add other fields as necessary
                location = "$city, $country",
                contactNumber = contactNumber,
                password = password
                // Make sure to add and initialize all required fields
            )

            DataManager.addUser(newUser)
            DataManager.sendUserSQL(this, newUser)
            findViewById<EditText>(R.id.EmailText).setText("")
            findViewById<EditText>(R.id.PasswordText).setText("")
            findViewById<EditText>(R.id.NameText).setText("")
            findViewById<EditText>(R.id.NumberText).setText("")

            DataManager.currentFragment = ProfileFragment()
            DataManager.currentUser = newUser

            val intent = Intent(this, BottomNavigationBar::class.java)
            startActivity(intent)
            finish()
        }

        val btn2 : TextView = findViewById(R.id.loginredirect)
        btn2.setOnClickListener{
            val intent = Intent(this, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }


    }
    fun generateUniqueUserId(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val randomUUID = UUID.randomUUID().toString()
        return "$currentTimeMillis-$randomUUID"
    }





}