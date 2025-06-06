package com.example.a1
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [OpenedChatFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class OpenedChatFragment : Fragment() {
    // TODO: Rename and change types of parameters
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
        val view= inflater.inflate(R.layout.fragment_opened_chat, container, false)

        val back=view.findViewById<TextView>(R.id.BackArrow)


        back.setOnClickListener {
            val fragmentManager = getFragmentManager()
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.FrameLayout, ChatFragment())
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }


        val opencam=view.findViewById<ImageView>(R.id.opencam)


        opencam.setOnClickListener {
            val intent = Intent(activity, PhotoCameraScreen::class.java)
            startActivity(intent)
            activity?.finish()

        }

        val startcall=view.findViewById<ImageView>(R.id.startcall)


        startcall.setOnClickListener {
            val intent = Intent(activity, VoiceCallScreen::class.java)
            startActivity(intent)
            activity?.finish()

        }

        val startvidcall=view.findViewById<ImageView>(R.id.startvidcall)


        startvidcall.setOnClickListener {
            val intent = Intent(activity, VidCallScreen::class.java)
            startActivity(intent)
            activity?.finish()

        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment OpenedChatFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            OpenedChatFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}