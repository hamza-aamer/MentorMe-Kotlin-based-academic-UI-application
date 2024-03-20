package com.example.a1

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.FileObserver
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
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
    lateinit var PhotoLauncher: ActivityResultLauncher<Intent>
    private val SCREENSHOT_PATH = "${Environment.getExternalStorageDirectory().path}/Pictures/Screenshots"
    private lateinit var screenshotObserver: FileObserver

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
        val addphoto = view.findViewById<ImageView>(R.id.addphoto)
        ChatManager.getallchats { chats ->
            ChatManager.chat = chats[0]
        }
        messageRecyclerView=view.findViewById(R.id.messagesrecycler)



        send.setOnClickListener {
            val newmessage = Message(
                messageId = generateUniqueMessageId(),
                senderId = DataManager.currentUser!!.userId,
                message = message.text.toString(),
                timestamp = getCurrentTimeString()
            )
            ChatManager.chat!!.messages.add(newmessage)
            ChatManager.updateChat()
            message.setText("")

        }
        //this should run when a screenshot is taken


        screenshotObserver = object : FileObserver(SCREENSHOT_PATH, CREATE) {
            override fun onEvent(event: Int, path: String?) {
                if (event == CREATE && path != null) {
                    Log.d("ScreenshotObserver", "Screenshot detected: $path")
                    // Handle the screenshot event. You can notify the chat or perform other actions.
                    val screenshottaken = Message(
                        messageId = generateUniqueScreenShotId(),
                        senderId = DataManager.currentUser!!.userId,
                        message = DataManager.currentUser!!.name + " took a screenshot",
                        timestamp = getCurrentTimeString()
                    )
                    ChatManager.chat!!.messages.add(screenshottaken)
                    ChatManager.updateChat()
                }
            }
        }
        screenshotObserver.startWatching()



        addphoto.setOnClickListener {

            checkPermissionAndPickPhoto()
        }


        PhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //upload a photo
                uploadToFireBase(result.data?.data!!)

            }
        }

        opencam.setOnClickListener {
            val intent = Intent(activity, PhotoCameraScreen::class.java)
            startActivity(intent)

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
    fun checkPermissionAndPickPhoto(){
        var readExternalPhoto : String = ""
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            readExternalPhoto = android.Manifest.permission.READ_MEDIA_IMAGES
        }else{
            readExternalPhoto = android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if(ContextCompat.checkSelfPermission(requireActivity(),readExternalPhoto)== PackageManager.PERMISSION_GRANTED){
            //we have permission
            openPhotoPicker()
        }else{
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(readExternalPhoto),
                100
            )
        }
    }

    private fun openPhotoPicker(){
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        PhotoLauncher.launch(intent)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        screenshotObserver.stopWatching()
    }

    fun generateUniqueMessageId(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val randomUUID = UUID.randomUUID().toString()
        return "Message-$currentTimeMillis-$randomUUID"
    }
    fun generateUniqueScreenShotId(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val randomUUID = UUID.randomUUID().toString()
        return "Screenshot-$currentTimeMillis-$randomUUID"
    }
    fun generateUniqueImageId(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val randomUUID = UUID.randomUUID().toString()
        return "Image-$currentTimeMillis-$randomUUID"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messageRecyclerView = view.findViewById(R.id.messagesrecycler)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        messageRecyclerView.layoutManager = layoutManager

        loadChats()


        if (!DataManager.chatlisteneron) {
            FirestoreReference.db.collection("chats")
                .addSnapshotListener { value, e ->
                    if (e != null) {
                        Log.w(ContentValues.TAG, "Listen failed.", e)
                        return@addSnapshotListener
                    }
                    loadChats()
                }
            DataManager.chatlisteneron=true
        }

    }
    fun uploadToFireBase(photoUri : Uri){
        val photoRef =  FirebaseStorage.getInstance()
            .reference
            .child("chatPic/"+ DataManager.currentUser!!.userId )
        photoRef.putFile(photoUri)
            .addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener {downloadUrl->
                    //video model store in firebase firestore
                    postToFirestore(downloadUrl.toString())
                }
            }

    }
    fun postToFirestore(url : String){

        val newmessage = Message(
            messageId = generateUniqueImageId(),
            senderId = DataManager.currentUser!!.userId,
            message = url,
            timestamp = getCurrentTimeString()
        )
        ChatManager.chat!!.messages.add(newmessage)
        ChatManager.updateChat()
    }
    private fun loadChats() {
        ChatManager.getallchats { chats ->
            if (isAdded) {
                activity?.let{
                    messageRecyclerView.adapter = messageAdapter(this.requireContext(),chats[0].messages,it)
                    messageRecyclerView.scrollToPosition(chats[0].messages.size-1)
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