package com.example.a1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NotificationAdapter(private val context: Context, private var notifList: ArrayList<String>) : RecyclerView.Adapter<NotificationAdapter.NotifViewHolder>() {

    class NotifViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val notifTextView: TextView = view.findViewById(R.id.notificationtext)
        val removeNotif: ImageView = view.findViewById(R.id.removenotif)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotifViewHolder {
        Log.d("NotificationAdapter", "onCreateViewHolder")

        val view = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false)
        return NotifViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotifViewHolder, position: Int) {
        Log.d("NotificationAdapter", "onBindViewHolder for position $position")

        val notification = notifList[position]
        holder.notifTextView.text = notification
        holder.removeNotif.setOnClickListener {
            // Perform action on click
            removeNotificationAt(position)
        }
    }

    override fun getItemCount(): Int {
        Log.d("NotificationAdapter", "getItemCount: ${notifList.size}")
        return notifList.size
    }
    private fun removeNotificationAt(position: Int) {
        // Safe check to prevent IndexOutOfBoundsException
        if (position >= 0 && position < notifList.size) {
            notifList.removeAt(position)
            DataManager.currentUser?.Notifications = notifList
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, notifList.size)
            DataManager.updateUser(context,DataManager.currentUser!!)
        }
    }
}
