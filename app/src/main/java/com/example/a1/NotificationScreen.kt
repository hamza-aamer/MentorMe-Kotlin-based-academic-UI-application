package com.example.a1

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView

class NotificationScreen : AppCompatActivity() {
    private lateinit var recycler: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_screen)
        recycler =findViewById<RecyclerView>(R.id.notificationsrecycler)
        val search = DataManager.currentUser!!.Notifications
        recycler.layoutManager= androidx.recyclerview.widget.LinearLayoutManager(this)
        Log.d("NotificationScreen", "Loading ${search.size} notifications.")
        val clear : TextView = findViewById<TextView>(R.id.clearnotifs)
        clear.setOnClickListener{
            DataManager.currentUser!!.Notifications.clear()
            DataManager.updateUser(DataManager.currentUser!!)
            val intent = Intent(this, NotificationScreen::class.java)
            startActivity(intent)
            finish()
        }
        recycler.adapter = NotificationAdapter(this,search)

        val button: TextView = findViewById<TextView>(R.id.BackArrow)
        button.setOnClickListener{
            val intent = Intent(this, BottomNavigationBar::class.java)
            startActivity(intent)
            finish()
        }
    }

}