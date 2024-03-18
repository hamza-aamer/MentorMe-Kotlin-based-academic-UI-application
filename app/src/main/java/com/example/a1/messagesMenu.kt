package com.example.a1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a1.databinding.ActivityMessagesMenuBinding

class messagesMenu : AppCompatActivity() {

    // Declare the binding variable here without initializing it
    private lateinit var binding: ActivityMessagesMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the binding here, where the layout inflater is available
        binding = ActivityMessagesMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.messagetext.setText(ChatManager.focusedMessage!!.message)
        binding.BackArrow.setOnClickListener {
            finish()
        }
        binding.deletemessage.setOnClickListener {
            ChatManager.chat!!.messages=removeMessageById(ChatManager.chat!!.messages,ChatManager.focusedMessage!!.messageId)
            ChatManager.updateChat()
            finish()
        }
        binding.editmessage.setOnClickListener {
            ChatManager.chat!!.messages=updateMessageById(ChatManager.chat!!.messages,ChatManager.focusedMessage!!.messageId,binding.messagetext.text.toString())
            ChatManager.updateChat()
            finish()
        }
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
    fun updateMessageById(messages: ArrayList<Message>, messageId: String, updatedMessage: String): ArrayList<Message> {
        // Find the index of the message with the given messageId
        val index = messages.indexOfFirst { it.messageId == messageId }

        if (index != -1) { // Check if the message was found
            // Update the message at the found index with a new Message object that has the updated message text
            val oldMessage = messages[index]
            messages[index] = oldMessage.copy(message = updatedMessage)
        }

        // Return the updated list
        return messages
    }


}
