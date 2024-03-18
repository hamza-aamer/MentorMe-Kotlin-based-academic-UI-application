package com.example.a1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class mentorAdapter(private val mentorList: ArrayList<Mentor>) : RecyclerView.Adapter<mentorAdapter.MentorViewHolder>() {

    class MentorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.mentorname)
        val roleTextView: TextView = view.findViewById(R.id.role)
        val mentorProfilePic: ImageView = view.findViewById(R.id.mentorprofilepic)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.mentor_item, parent, false)
        return MentorViewHolder(view)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        val mentor = mentorList[position]
        holder.nameTextView.text = mentor.name
        holder.roleTextView.text = mentor.role
        Glide.with(holder.mentorProfilePic)
            .load(mentor.profileImageUrl)
            .into(holder.mentorProfilePic)
        }



    override fun getItemCount() = mentorList.size
}