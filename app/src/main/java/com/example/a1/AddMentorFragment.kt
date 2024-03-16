package com.example.a1

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddMentorFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddMentorFragment : Fragment() {
    // TODO: Rename and change types of parameters
    lateinit var PhotoLauncher: ActivityResultLauncher<Intent>
    val mentorid: String = generateUniqueUserId()
    private var imageurl: String = ""
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
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_add_mentor, container, false)

        val uploadmentor = view.findViewById<TextView>(R.id.UploadAddition)

        val name = view.findViewById<EditText>(R.id.editNameText)
        val description = view.findViewById<EditText>(R.id.editDescText)
        val avail = view.findViewById<EditText>(R.id.editAvailText)




        uploadmentor.setOnClickListener {
            val newMentor = Mentor(
                mentorId = mentorid,
                role = "Mentor",
                name = name.text.toString(),
                mentorDesc = description.text.toString(),
                mentorStatus = avail.text.toString(),
                profileImageUrl = imageurl
            )
            MentorManager.addMentor(newMentor)
            DataManager.currentFragment = HomeFragment()
            val intent = Intent(activity, BottomNavigationBar::class.java)
            startActivity(intent)
        }

        PhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                //upload a photo
                uploadToFirestore(result.data?.data!!)

            }
        }



        val back=view.findViewById<TextView>(R.id.BackArrow)
        val uploadbtn = view.findViewById<ImageView>(R.id.uploadphotobtn)

        uploadbtn.setOnClickListener {
            checkPermissionAndPickPhoto()
            view.findViewById<TextView>(R.id.photouploadstatus).setText("Photo Uploaded")
        }

        back.setOnClickListener {
            val intent = Intent(activity, BottomNavigationBar::class.java)
            startActivity(intent)
            activity?.finish()
        }

        return view
    }
    fun generateUniqueUserId(): String {
        val currentTimeMillis = System.currentTimeMillis()
        val randomUUID = UUID.randomUUID().toString()
        return "$currentTimeMillis-$randomUUID"
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

    fun uploadToFirestore(photoUri : Uri){
        val photoRef =  FirebaseStorage.getInstance()
            .reference
            .child("mentorPic/"+ mentorid )
        photoRef.putFile(photoUri)
            .addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener {downloadUrl->
                    //video model store in firebase firestore
                    postToFirestore(downloadUrl.toString())
                    imageurl=downloadUrl.toString()
                }
            }
    }

    fun postToFirestore(url : String){
        Firebase.firestore.collection("mentors")
            .document(mentorid)
            .update("profileImageUrl",url)
            .addOnSuccessListener {
                //update the url in the user model
                imageurl = url
            }
    }
    private fun openPhotoPicker(){
        var intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        PhotoLauncher.launch(intent)
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddMentorFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddMentorFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}