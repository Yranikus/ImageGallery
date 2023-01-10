package com.example.myapplication3333.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication3333.DeleteListner
import com.example.myapplication3333.R
import com.example.myapplication3333.utils.adapters.BrowseAdapter



private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private const val ARG_PARAM3 = "foldername"

class BrowseFragment : Fragment() {

    private var param1: ArrayList<String> = ArrayList()
    private lateinit var delListner: DeleteListner
    private lateinit var foldername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_browse, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var curfrug = this
        var pager = view.findViewById<ViewPager2>(R.id.pager)
        pager.adapter = BrowseAdapter(curfrug, arguments?.get("param1") as ArrayList<String>,delListner,
            arguments?.get("foldername") as String
        )
    }

    override fun onStart() {
        super.onStart()
        var pager = view?.findViewById<ViewPager2>(R.id.pager)
        pager!!.setCurrentItem(arguments?.getInt("param2")!!,false)
    }


    fun onClick(del: DeleteListner) {
        delListner = del
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BrowseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: ArrayList<String>, param2: Int,foldername: String) =
            BrowseFragment().apply {
                arguments = Bundle().apply {
                    putStringArrayList(ARG_PARAM1, param1)
                    putInt(ARG_PARAM2, param2)
                    putString(ARG_PARAM3, foldername)
                }
            }
    }
}