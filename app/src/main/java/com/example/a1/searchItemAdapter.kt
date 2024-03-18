package com.example.a1

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.net.HttpURLConnection
import java.net.URL

class searchItemAdapter(private val context: Context, private val mentorList: ArrayList<Mentor>) : RecyclerView.Adapter<searchItemAdapter.MentorViewHolder>() {

    class MentorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.mentorname)
        val roleTextView: TextView = view.findViewById(R.id.role)
        val mentorProfilePic: ImageView = view.findViewById(R.id.mentorprofilepic)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_item, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        val mentor = mentorList[position]
        holder.nameTextView.text = mentor.name
        holder.roleTextView.text = mentor.role
        Glide.with(holder.mentorProfilePic)
            .load(mentor.profileImageUrl)
            .into(holder.mentorProfilePic)

        holder.mentorProfilePic.setOnClickListener {
            MentorManager.focusedMentor= mentor
            val intent = Intent(context, UserFocusedScreen::class.java)
            context.startActivity(intent)
        }

    }
    fun downloadAndSetImage(imageView: ImageView, imageUrl: String) {
        Thread {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream = connection.inputStream
                val originalBitmap = BitmapFactory.decodeStream(inputStream)


                // Use a Handler to post the result back to the main thread
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(originalBitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle exceptions or errors as necessary
            }
        }.start()
    }


    override fun getItemCount() = mentorList.size
}