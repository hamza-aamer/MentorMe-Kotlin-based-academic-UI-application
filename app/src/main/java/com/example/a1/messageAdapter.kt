package com.example.a1

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.HttpURLConnection
import java.net.URL


class messageAdapter(val context: Context, val items: ArrayList<Message>,val act: Context ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_MY_MESSAGE = 0
    private val TYPE_OTHER_MESSAGE = 1
    private val TYPE_MY_IMAGE = 2
    private val TYPE_OTHER_IMAGE = 3

    override fun getItemViewType(position: Int): Int {
        var chat : Message = items[position] as Message
        var myid = DataManager.currentUser!!.userId
        if (chat.senderId == myid) {
            if (chat.messageId[0]=='I')
                return TYPE_MY_IMAGE
            else
            return TYPE_MY_MESSAGE
        } else {
            if (chat.messageId[0]=='I')
                return TYPE_OTHER_IMAGE
            else
            return TYPE_OTHER_MESSAGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            TYPE_MY_MESSAGE -> {
                val view = inflater.inflate(R.layout.mymessage_item, parent, false)
                MyTextViewHolder(view)
            }
            TYPE_OTHER_MESSAGE -> {
                val view = inflater.inflate(R.layout.othermessage_item, parent, false)
                OtherTextViewHolder(view)
            }
            TYPE_MY_IMAGE -> {
                val view = inflater.inflate(R.layout.mymessage_image, parent, false)
                MyImageViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.othermessage_image, parent, false)
                OtherImageViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is MyTextViewHolder -> {
                // Assuming `item` has `text` and `time` properties for MyMessageType
                holder.txt.text = (item as Message).message
                holder.time.text = (item as Message).timestamp

                holder.txt.setOnLongClickListener {
                    ChatManager.focusedMessage = (item as Message)

                    val intent = Intent(act, messagesMenu::class.java)
                    act.startActivity(intent)

                    Log.d(TAG, "onBindViewHolder: " + "long click detected")
                    return@setOnLongClickListener true
                }


            }
            is OtherTextViewHolder -> {
                // Assuming `item` has `text`, `time`, and `picture` properties for OtherMessageType
                holder.txt.text = (item as Message).message
                holder.time.text = (item as Message).timestamp
                MentorManager.getAllMentors{it ->
                    for (mentor in it){
                        if(mentor.mentorId == (item as Message).senderId){
                            downloadAndSetImage(holder.pic, mentor.profileImageUrl)
                        }
                    }
                }
                DataManager.getAllUsers { it ->
                    for (user in it) {
                        if (user.userId == (item as Message).senderId) {
                            downloadAndSetImage(holder.pic, user.profileImageUrl)
                        }
                    }
                }


                // Set the image for `pic` ImageView. Example: holder.pic.setImageResource(item.pictureResource)
            }
            is MyImageViewHolder -> {
                // Assuming `item` has `text`, `time`, and `picture` properties for MyMessageType
                downloadAndSetImage(holder.txt, (item as Message).message)

                holder.time.text = (item as Message).timestamp
            }
            is OtherImageViewHolder -> {
                // Assuming `item` has `text`, `time`, and `picture` properties for OtherMessageType
                downloadAndSetImage(holder.txt, (item as Message).message)
                holder.time.text = (item as Message).timestamp
                MentorManager.getAllMentors{it ->
                    for (mentor in it){
                        if(mentor.mentorId == (item as Message).senderId){
                            downloadAndSetImage(holder.pic, mentor.profileImageUrl)
                        }
                    }
                }
                DataManager.getAllUsers { it ->
                    for (user in it) {
                        if (user.userId == (item as Message).senderId) {
                            downloadAndSetImage(holder.pic, user.profileImageUrl)
                        }
                    }
                }
            }
        }
    }

    fun downloadAndSetImage(imageView: ImageView, imageUrl: String) {
        Thread {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)




                // Use a Handler to post the result back to the main thread
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle exceptions or errors as necessary
            }
        }.start()
    }
    override fun getItemCount(): Int {
        return items.size
    }

    class MyTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt: TextView = itemView.findViewById(R.id.messagetext)
        val time: TextView = itemView.findViewById(R.id.messagetime)
    }
    class MyImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt: ImageView = itemView.findViewById(R.id.messagetext)
        val time: TextView = itemView.findViewById(R.id.messagetime)
    }


    class OtherTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt: TextView = itemView.findViewById(R.id.messagermessage)
        val time: TextView = itemView.findViewById(R.id.messagermessagetime)
        val pic: ImageView = itemView.findViewById(R.id.messagerpic)
    }

    class OtherImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt: ImageView = itemView.findViewById(R.id.messagermessage)
        val time: TextView = itemView.findViewById(R.id.messagermessagetime)
        val pic: ImageView = itemView.findViewById(R.id.messagerpic)
    }

}
