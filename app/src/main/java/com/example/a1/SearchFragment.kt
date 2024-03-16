package com.example.a1

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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var Mentors : ArrayList<Mentor> = ArrayList()


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
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val img = view.findViewById<ImageView>(R.id.entimg) // Assuming the ID of your button is "entButton"
        val img2 = view.findViewById<ImageView>(R.id.persimg) // Assuming the ID of your button is "entButton"
        val img3 = view.findViewById<ImageView>(R.id.eduimg) // Assuming the ID of your button is "entButton"
        val searchtext = view.findViewById<EditText>(R.id.searchtext)
        val searchbtn = view.findViewById<ImageView>(R.id.searchicon)
        val back=view.findViewById<TextView>(R.id.BackArrow)


        searchbtn.setOnClickListener {
            var text = searchtext.text.toString()
            for (mentor in MentorManager.lastUpdatedAllMentors){
                if (mentor.name.contains(text) || mentor.role.contains(text)){
                    Mentors.add(mentor)
                }
            }

            for (Mentor in Mentors){
                Log.d("Mentor",Mentor.name)
            }
            MentorManager.searchedMentors=Mentors
            replacefrag(SearchResultsFragment())
        }

        back.setOnClickListener {
            val intent = Intent(activity, BottomNavigationBar::class.java)
            startActivity(intent)
            activity?.finish()
        }



        img.setOnClickListener {
            // Navigate to SearchViewResults fragment
//            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.SearchFragment, SearchResultsFragment.newInstance("param1", "param2"))
//            fragmentTransaction.commit()
            replacefrag(SearchResultsFragment())
        }
        img2.setOnClickListener {
            // Navigate to SearchViewResults fragment
//            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.SearchFragment, SearchResultsFragment.newInstance("param1", "param2"))
//            fragmentTransaction.commit()
            replacefrag(SearchResultsFragment())

        }
        img3.setOnClickListener {
            // Navigate to SearchViewResults fragment
//            val fragmentTransaction = requireActivity().supportFragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.SearchFragment, SearchResultsFragment.newInstance("param1", "param2"))
//            fragmentTransaction.commit()
            replacefrag(SearchResultsFragment())

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
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun replacefrag(fragment: Fragment){
        val fragmentManager = getFragmentManager()
        val fragmentTransaction = fragmentManager?.beginTransaction()
        fragmentTransaction?.replace(R.id.FrameLayout, fragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()


    }
}