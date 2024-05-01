package com.example.a1

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var mentorsRecyclerView: RecyclerView


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
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_home, container, false)


        DataManager.context=requireContext()
        if(!DataManager.chatNotifListener) {
            FirestoreReference.db.collection("chats")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    Log.d(ContentValues.TAG, "Loading Chat")
                    ChatManager.getallchats { chats ->
                        Log.d("TAG", "onCreateView: " + chats[0].messages[chats[0].messages.size-1].message)
                        if (chats[0].messages[chats[0].messages.size-1].senderId != DataManager.currentUser!!.userId) {
                            Toast.makeText(
                                DataManager.context,
                                "You Recieved A Message",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.d("TAG", "onCreateView: " + "You Recieved A Message")
                            //add condition to check if DataManager.currentUser!!.Notifications already has an entry for this
                            if (!DataManager.currentUser!!.Notifications.contains("You have new messages in the community chat")){
                                DataManager.currentUser!!.Notifications.add("You have new messages in the community chat")
                                DataManager.updateUser(DataManager.context!!,DataManager.currentUser!!)
                            }
                        }
                    }
                }
            DataManager.chatNotifListener=true
        }
        val btn: ImageButton = view.findViewById<ImageButton>(R.id.notifbutton)
        btn.setOnClickListener{
            val intent = Intent(activity, NotificationScreen::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mentorsRecyclerView = view.findViewById(R.id.topmentors)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        mentorsRecyclerView.layoutManager = layoutManager

        loadMentors()


        FirestoreReference.db.collection("mentors")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@addSnapshotListener
                }
                loadMentors()
            }


    }
    private fun loadMentors() {
        MentorManager.getAllMentors { mentors ->
            if (isAdded) {
                mentorsRecyclerView.adapter = mentorAdapter(requireContext(),mentors)
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}