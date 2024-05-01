package com.example.a1

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.database.ContentObserver
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.FileObserver
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var mediaRecorder: MediaRecorder
    private var audioUri: Uri? = null // Placeholder for recording Uri

    private var param1: String? = null
    private var param2: String? = null
    private var recording: Boolean = false
    private var prevMessages: ArrayList<Message> = ArrayList()
    private val SCREENSHOT_NOTIFICATION_DELAY = 2000
    private fun isScreenshot(uri: Uri): Boolean {
        val contentResolver: ContentResolver = requireActivity().contentResolver
        val cursor = contentResolver.query(
            uri, arrayOf(MediaStore.Images.Media.DISPLAY_NAME),
            null, null, null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val fileName = it.getString(it.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME))
                if (fileName.contains("screenshot", ignoreCase = true)) {
                    return true
                }
            }
        }
        return false
    }
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
        //ChatManager.getallchats { chats ->
        //    ChatManager.chat = chats[0]
        //}
        ChatManager.getAllMessages(DataManager.context!!){messages ->
            ChatManager.chat=Chat("GlobalChat",messages!!)
        }
        messageRecyclerView=view.findViewById(R.id.messagesrecycler)


        if (requireActivity().packageManager.hasSystemFeature(PackageManager.FEATURE_MICROPHONE)){
            if (ContextCompat.checkSelfPermission(requireActivity(),Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), 200)
            }
        }

        val record = view.findViewById<ImageView>(R.id.sendvm)
        val storageReference = FirebaseStorage.getInstance().reference.child("ChatRecordings")

        initializeRecordingComponents(view)




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
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                ChatManager.getAllMessages(DataManager.context!!){messages ->
                    if (prevMessages!=messages){
                        loadChats()
                        prevMessages=ChatManager.chat!!.messages
                    }
                }

                handler.postDelayed(this, 3000) // 2000 milliseconds = 2 seconds
            }
        }
        handler.postDelayed(runnable, 3000)
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
//        screenshotObserver= object : ContentObserver(Handler()) {
//            override fun onChange(selfChange: Boolean, uri: Uri?) {
//                super.onChange(selfChange, uri)
//                uri?.let { screenshotUri ->
//                    // Check if the screenshot is taken
//                    if (isScreenshot(screenshotUri)) {
//                        // Delay sending the notification to allow the screenshot to be saved
//                        Handler().postDelayed({
//                            val screenshottaken = Message(
//                                messageId = generateUniqueScreenShotId(),
//                                senderId = DataManager.currentUser!!.userId,
//                                message = DataManager.currentUser!!.name + " took a screenshot",
//                                timestamp = getCurrentTimeString()
//                            )
//                            ChatManager.chat!!.messages.add(screenshottaken)
//                            ChatManager.updateChat()
//                            Log.d("ScreenshotObserver", "Screenshot detected: $screenshotUri")
//                        }, SCREENSHOT_NOTIFICATION_DELAY.toLong())
//                    }
//                }
//            }
//        }




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
    private fun startRecording() {
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, "recording_${System.currentTimeMillis()}.3gp")
                put(MediaStore.MediaColumns.MIME_TYPE, "audio/3gpp")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_MUSIC + "/Recordings")
            }

            val uri = requireActivity().contentResolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, contentValues)
            audioUri = uri // Saving Uri for upload

            setOutputFile(requireActivity().contentResolver.openFileDescriptor(uri!!, "w")?.fileDescriptor)
            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            try {
                prepare()
                start()
                Toast.makeText(activity, "Recording started", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Log.e("Recording", "prepare() failed: ${e.message}")
                Toast.makeText(activity, "Recording failed to start", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun initializeRecordingComponents(view: View) {
        // Checking and requesting necessary permissions...
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.RECORD_AUDIO), 200)
        }

        val record = view.findViewById<ImageView>(R.id.sendvm)
        val storageReference = FirebaseStorage.getInstance().reference.child("ChatRecordings")

        record.setOnClickListener {
            if (recording) {
                // Stop recording
                mediaRecorder.stop()
                mediaRecorder.release()
                recording = false
                Toast.makeText(activity, "Recording stopped", Toast.LENGTH_LONG).show()

                // Create a unique file name
                val fileName = "recording_${System.currentTimeMillis()}.3gp"
                val fileRef = storageReference.child(fileName)

                audioUri?.let { uri ->
                    // Generate unique ID for this voice message
                    val uniqueVMId = generateUniqueVoiceMessageId()

                    // Upload file to Firebase Storage
                    val uploadTask = fileRef.putFile(uri)
                    uploadTask.addOnSuccessListener {
                        fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                            val downloadUrl = downloadUri.toString()
                            postVMToFirestore(downloadUrl, uniqueVMId) // Pass the unique VM ID and URL
                            Toast.makeText(activity, "Recording uploaded successfully", Toast.LENGTH_LONG).show()
                        }
                    }.addOnFailureListener { e ->
                        Log.e("Firebase", "Upload failed: ${e.message}")
                        Toast.makeText(activity, "Upload failed", Toast.LENGTH_LONG).show()
                    }
                }

            } else {
                // Prepare for recording
                recording = true
                startRecording()
            }
        }
    }
    fun postVMToFirestore(url: String, messageId: String) {
        val newMessage = Message(
            messageId = messageId, // Use the unique ID for the voice message
            senderId = DataManager.currentUser!!.userId,
            message = url, // This will be the URL of the uploaded voice message
            timestamp = getCurrentTimeString(),
        )
        ChatManager.chat!!.messages.add(newMessage)
        ChatManager.updateChat()
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
    fun generateUniqueVoiceMessageId(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val randomUUID = UUID.randomUUID().toString()
        return "Recording-$currentTimeMillis-$randomUUID"
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
//        ChatManager.getallchats { chats ->
//            if (isAdded) {
//                activity?.let{
//                    messageRecyclerView.adapter = messageAdapter(this.requireContext(),chats[0].messages,it)
//                    messageRecyclerView.scrollToPosition(chats[0].messages.size-1)
//                }
//            }
//        }
        ChatManager.getAllMessages(DataManager.context!!){messages ->
            ChatManager.chat=Chat("GlobalChat",messages!!)
            if (isAdded) {
                activity?.let {
                    messageRecyclerView.adapter =
                        messageAdapter(this.requireContext(), ChatManager.chat!!.messages, it)
                    messageRecyclerView.scrollToPosition(ChatManager.chat!!.messages.size - 1)
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