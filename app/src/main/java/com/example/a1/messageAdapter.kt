package com.example.a1

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import java.io.IOException


class messageAdapter(val context: Context, val items: ArrayList<Message>,val act: Context ) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_MY_MESSAGE = 0
    private val TYPE_OTHER_MESSAGE = 1
    private val TYPE_MY_IMAGE = 2
    private val TYPE_OTHER_IMAGE = 3
    private val TYPE_SCREENSHOT = 4
    private val TYPE_MY_RECORDING = 5
    private val TYPE_OTHER_RECORDING = 6


    override fun getItemViewType(position: Int): Int {
        val chat: Message = items[position]
        val myId = DataManager.currentUser!!.userId
        return when {
            chat.messageId.startsWith("S") -> TYPE_SCREENSHOT
            chat.messageId.startsWith("I") -> if (chat.senderId == myId) TYPE_MY_IMAGE else TYPE_OTHER_IMAGE
            chat.messageId.startsWith("R") -> if (chat.senderId == myId) TYPE_MY_RECORDING else TYPE_OTHER_RECORDING
            else -> if (chat.senderId == myId) TYPE_MY_MESSAGE else TYPE_OTHER_MESSAGE
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            TYPE_MY_RECORDING -> {
                val view = inflater.inflate(R.layout.item_my_recording, parent, false)
                MyRecordingViewHolder(view)
            }
            TYPE_OTHER_RECORDING -> {
                val view = inflater.inflate(R.layout.item_other_recording, parent, false)
                OtherRecordingViewHolder(view)
            }
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
            TYPE_OTHER_IMAGE -> {
                val view = inflater.inflate(R.layout.othermessage_image, parent, false)
                OtherImageViewHolder(view)
            }
            else -> {
                val view = inflater.inflate(R.layout.screenshot_item, parent, false)
                ScreenshotViewHolder(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (holder) {
            is MyRecordingViewHolder -> {
                holder.playButton.setOnClickListener {
                    Toast.makeText(context, "Playing audio", Toast.LENGTH_SHORT).show()
                    playAudioFromUrl(item.message, context)

                }
                holder.playButton.setOnLongClickListener{
                    ChatManager.chat!!.messages=removeMessageById(ChatManager.chat!!.messages,(item as Message).messageId)
                    ChatManager.updateChat()
                    true
                }

                holder.time.text = item.timestamp
            }
            is OtherRecordingViewHolder -> {
                holder.playButton.setOnClickListener {
                    Toast.makeText(context, "Playing audio", Toast.LENGTH_SHORT).show()
                    playAudioFromUrl(item.message, context)
                }
                holder.time.text = item.timestamp
                MentorManager.getAllMentors{it ->
                    for (mentor in it){
                        if(mentor.mentorId == (item as Message).senderId){
                            Glide.with(holder.profilePic)
                                .load(mentor.profileImageUrl)
                                .circleCrop()
                                .into(holder.profilePic)
                        }
                    }
                }
                DataManager.getAllUsers { it ->
                    for (user in it) {
                        if (user.userId == (item as Message).senderId) {
                            Glide.with(holder.profilePic)
                                .load(user.profileImageUrl)
                                .circleCrop()
                                .into(holder.profilePic)
                        }
                    }
                }            }
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
                            Glide.with(holder.pic)
                                .load(mentor.profileImageUrl)
                                .circleCrop()
                                .into(holder.pic)
                        }
                    }
                }
                DataManager.getAllUsers { it ->
                    for (user in it) {
                        if (user.userId == (item as Message).senderId) {
                            Glide.with(holder.pic)
                                .load(user.profileImageUrl)
                                .circleCrop()
                                .into(holder.pic)
                        }
                    }
                }


                // Set the image for `pic` ImageView. Example: holder.pic.setImageResource(item.pictureResource)
            }
            is MyImageViewHolder -> {
                // Assuming `item` has `text`, `time`, and `picture` properties for MyMessageType
                Glide.with(holder.txt)
                    .load((item as Message).message)
                    .into(holder.txt)
                holder.txt.setOnLongClickListener{
                    ChatManager.chat!!.messages=removeMessageById(ChatManager.chat!!.messages,(item as Message).messageId)
                    ChatManager.updateChat()
                    true
                }
                holder.time.text = (item as Message).timestamp
            }
            is OtherImageViewHolder -> {
                // Assuming `item` has `text`, `time`, and `picture` properties for OtherMessageType
                Glide.with(holder.txt)
                    .load((item as Message).message)
                    .into(holder.txt)
                holder.time.text = (item as Message).timestamp
                MentorManager.getAllMentors{it ->
                    for (mentor in it){
                        if(mentor.mentorId == (item as Message).senderId){
                            Glide.with(holder.pic)
                                .load(mentor.profileImageUrl)
                                .into(holder.pic)
                        }
                    }
                }
                DataManager.getAllUsers { it ->
                    for (user in it) {
                        if (user.userId == (item as Message).senderId) {
                            Glide.with(holder.pic)
                                .load(user.profileImageUrl)
                                .circleCrop()
                                .into(holder.pic)
                        }
                    }
                }
            }
            is ScreenshotViewHolder -> {
                holder.txt.text = (item as Message).message
            }
        }
    }
    fun playAudioFromUrl(url: String, context: Context) {
        try {
            // Initialize MediaPlayer
            val mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                prepareAsync() // Prepare the player asynchronously
            }

            mediaPlayer.setOnPreparedListener { mp ->
                mp.start()
            }

            mediaPlayer.setOnCompletionListener { mp ->
                mp.release()
            }

            mediaPlayer.setOnErrorListener { mp, what, extra ->
                Toast.makeText(context, "Error playing audio", Toast.LENGTH_SHORT).show()
                mp.release()
                true
            }
        } catch (e: IOException) {
            Toast.makeText(context, "Error setting data source", Toast.LENGTH_SHORT).show()
        }
    }


    class MyRecordingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playButton: TextView = itemView.findViewById(R.id.playaudio)
        val time: TextView = itemView.findViewById(R.id.messagetime)
        // Add onClickListener for playButton here to handle playback
    }

    class OtherRecordingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val playButton: TextView = itemView.findViewById(R.id.messagerrecording)
        val time: TextView = itemView.findViewById(R.id.messagermessagetime)
        val profilePic: ImageView = itemView.findViewById(R.id.messagerpic)
        // Add onClickListener for playButton here to handle playback
    }



    fun removeMessageById(messages: ArrayList<Message>, messageId: String): ArrayList<Message> {
        // Iterate through the list to find the message with the given messageId
        val iterator = messages.iterator()

        while (iterator.hasNext()) {
            val message = iterator.next()
            if (message.messageId == messageId) {
                // Remove the message from the list
                iterator.remove()
                break // Assuming messageId is unique and only one item needs to be removed
            }
        }

        // Return the updated list
        return messages
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

    class ScreenshotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txt: TextView = itemView.findViewById(R.id.screenshotText)
    }

}
