package com.example.a1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.Spinner;
import android.widget.TextView
import android.widget.Toast;


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
            val intent = Intent(this, TwoFactorScreen::class.java)
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
}