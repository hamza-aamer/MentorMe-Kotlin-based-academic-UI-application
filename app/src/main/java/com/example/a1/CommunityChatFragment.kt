package com.example.a1

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CommunityChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CommunityChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var messageRecyclerView: RecyclerView
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view= inflater.inflate(R.layout.fragment_community_chat, container, false)

        val back=view.findViewById<TextView>(R.id.BackArrow)
        val send = view.findViewById<ImageView>(R.id.sendmsg)
        val message = view.findViewById<EditText>(R.id.chatmessage)
        val opencam=view.findViewById<ImageView>(R.id.opencam)
        ChatManager.getallchats { chats ->
            ChatManager.chat = chats[0]
        }
        messageRecyclerView=view.findViewById(R.id.messagesrecycler)

        send.setOnClickListener {
            val newmessage = Message(
                messageId = generateUniqueUserId(),
                senderId = DataManager.currentUser!!.userId,
                message = message.text.toString(),
                timestamp = getCurrentTimeString()
            )
            ChatManager.chat!!.messages.add(newmessage)
            ChatManager.updateChat()
            message.setText("")

        }

        opencam.setOnClickListener {
            val intent = Intent(activity, PhotoCameraScreen::class.java)
            startActivity(intent)
            activity?.finish()

        }

        back.setOnClickListener {
            val fragmentManager = getFragmentManager()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.FrameLayout, ChatFragment())
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }


        return view
    }
    fun generateUniqueUserId(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val randomUUID = UUID.randomUUID().toString()
        return "Message-$currentTimeMillis-$randomUUID"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageRecyclerView = view.findViewById(R.id.messagesrecycler)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        messageRecyclerView.layoutManager = layoutManager

        loadChats()


        FirestoreReference.db.collection("chats")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(ContentValues.TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                loadChats()
            }


    }
    private fun loadChats() {
        ChatManager.getallchats { chats ->
            if (isAdded) {
                activity?.let{
                    messageRecyclerView.adapter = messageAdapter(this.requireContext(),chats[0].messages,it)
                }
            }
        }

    }
    fun getCurrentTimeString(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
        return dateFormat.format(Date())
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment CommunityChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CommunityChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}