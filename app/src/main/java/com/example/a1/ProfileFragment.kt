package com.example.a1




import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawable
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import java.net.HttpURLConnection
import java.net.URL


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var editpfp: ImageView
    private lateinit var pfp:ImageView
    private var param1: String? = null
    private var param2: String? = null
    lateinit var PhotoLauncher: ActivityResultLauncher<Intent>
    private lateinit var reviewsRecyclerView: RecyclerView


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
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val back=view.findViewById<TextView>(R.id.BackArrow)
        val settings=view.findViewById<ImageView>(R.id.editbanner)
        val book = view.findViewById<TextView>(R.id.showBookedSessions)
        val logoutbtn = view.findViewById<TextView>(R.id.logout)
        view.findViewById<TextView>(R.id.username).setText(DataManager.currentUser?.name)
        view.findViewById<TextView>(R.id.location).setText("📍"+DataManager.currentUser?.location.toString())
        editpfp = view.findViewById<ImageView>(R.id.editpfp)
        pfp = view.findViewById<ImageView>(R.id.pfp)


        PhotoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                //upload a photo
                uploadToFirestore(result.data?.data!!)

            }
        }

        editpfp.setOnClickListener {
            checkPermissionAndPickPhoto()
        }


        Glide.with(view.findViewById<ImageView>(R.id.pfp))
            .load(DataManager.currentUser!!.profileImageUrl)
            .placeholder(R.drawable.john)
            .into(pfp)



        downloadAndSetImage(pfp,DataManager.currentUser!!.profileImageUrl)





        logoutbtn.setOnClickListener {
            DataManager.currentUser=null
            DataManager.currentFragment=HomeFragment()
            val intent = Intent(activity, LoginScreen::class.java)
            startActivity(intent)
            activity?.finish()
        }
        book.setOnClickListener {
            val intent = Intent(activity, BookedSessionScreen::class.java)
            startActivity(intent)
            activity?.finish()
        }
        settings.setOnClickListener {
            val intent = Intent(activity, EditProfileScreen::class.java)
            startActivity(intent)
            activity?.finish()
        }

        back.setOnClickListener {
            val intent = Intent(activity, BottomNavigationBar::class.java)
            startActivity(intent)
            activity?.finish()
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        reviewsRecyclerView = view.findViewById(R.id.Reviews)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        reviewsRecyclerView.layoutManager = layoutManager

        loadReviews()
    }
    private fun loadReviews() {
        if (isAdded) { // Check fragment is attached
            reviewsRecyclerView.adapter = ReviewAdapter(DataManager.currentUser!!.reviewsGiven)
        }

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

    fun uploadToFirestore(photoUri : Uri){
        val photoRef =  FirebaseStorage.getInstance()
            .reference
            .child("profilePic/"+ DataManager.currentUser!!.userId )
        photoRef.putFile(photoUri)
            .addOnSuccessListener {
                photoRef.downloadUrl.addOnSuccessListener {downloadUrl->
                    //video model store in firebase firestore
                    postToFirestore(downloadUrl.toString())
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
                val roundedBitmapWrapper: RoundedBitmapDrawable =
                    RoundedBitmapDrawableFactory.create(Resources.getSystem(), bitmap)
                roundedBitmapWrapper.isCircular = true




                // Use a Handler to post the result back to the main thread
                Handler(Looper.getMainLooper()).post {
                    imageView.setImageDrawable(roundedBitmapWrapper)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Handle exceptions or errors as necessary
            }
        }.start()
    }
    fun postToFirestore(url : String){
        Firebase.firestore.collection("users")
            .document(DataManager.currentUser!!.userId)
            .update("profileImageUrl",url)
            .addOnSuccessListener {
                //update the url in the user model
                DataManager.currentUser!!.profileImageUrl = url
                downloadAndSetImage(pfp,url)
            }
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}