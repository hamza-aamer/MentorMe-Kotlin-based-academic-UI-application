package com.example.a1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.a1.databinding.ActivityLoginScreenBinding

class LoginScreen : AppCompatActivity() {


    private lateinit var binding: ActivityLoginScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.RegisterRedirect.setOnClickListener {
            val intent = Intent(this, RegisterScreen::class.java)
            startActivity(intent)
            finish()
        }

        binding.forgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassScreen::class.java)
            startActivity(intent)
            finish()
        }

        val logininfo = fetchText(this)
        if (logininfo[0] != "" && logininfo[1] != "") {
            val email = logininfo[0]
            val pass = logininfo[1]
            DataManager.getAllUsers { users ->
                for (user in users) {
                    if (user.email == email && user.password == pass) {
                        DataManager.currentUser = user
                        DataManager.currentFragment = HomeFragment()
                        val intent = Intent(this, BottomNavigationBar::class.java)
                        startActivity(intent)
                        finish()
                        break
                    }
                }
            }
        }

        binding.loginbtn.setOnClickListener{
            DataManager.getAllUsers { users ->
                for (user in users){
                    Log.d("getAllUsers", "User: ${user.name}, Email: ${user.email}, Password: ${user.password}")
                    Log.d("currenttext", "Email: ${binding.EmailText.text}, Password: ${binding.PasswordText.text}")
                    if (user.email==binding.EmailText.text.toString() && user.password==binding.PasswordText.text.toString()){
                        setText(this, binding.EmailText.text.toString(), binding.PasswordText.text.toString())
                        DataManager.currentUser=user
                        DataManager.currentFragment=HomeFragment()
                        val intent = Intent(this,BottomNavigationBar::class.java)
                        startActivity(intent)
                        finish()
                        break
                    }
                }

            }

        }

    }
    fun fetchText(context: Context) : Array<String> {
        val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val mail = sharedPreferences.getString("mail", "") ?: ""
        val pass = sharedPreferences.getString("pass", "") ?: ""

        return arrayOf(mail, pass)
    }
    fun setText(context: Context, mail: String, pass: String) {
        val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("mail", mail)
        editor.putString("pass", pass)
        editor.apply() // or editor.commit() for synchronous operation
    }

}