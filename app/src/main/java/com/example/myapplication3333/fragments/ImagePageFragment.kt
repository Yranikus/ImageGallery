package com.example.myapplication3333.fragments

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.content.IntentSender.SendIntentException
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.os.persistableBundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication3333.DeleteListner
import com.example.myapplication3333.R
import com.example.myapplication3333.srvices.UriService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

const val ARG_OBJECT = "object"
const val ARG_OBJECT2 = "foldername"

class ImagePageFragment() : Fragment() {
    private var param1: String? = null
    private lateinit var delListner: DeleteListner
    private lateinit var foldername: String

    val DELETE_REQUEST_CODE = 13

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var uri = ""
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            uri = getString(ARG_OBJECT)!!
        }
        val img = view.findViewById<ImageView>(R.id.imagep)
        var foldername = arguments?.getString(ARG_OBJECT2)
        Glide.with(view)
            .load(uri)
            .apply(RequestOptions().dontTransform())
            .into(img)
        img.scaleType = ImageView.ScaleType.FIT_CENTER
        var path = this.view?.findViewById<TextView>(R.id.pathToImg)
        path?.text = uri
        var deleteBtn = this.view?.findViewById<ImageButton>(R.id.delete)
        deleteBtn?.setOnClickListener {
            lifecycleScope.launch {
                launch {
                    if (foldername != "Pictures") {
                        File(uri).delete()
                    } else {
                        deleteAPI30(UriService.getContentUriId(Uri.parse(uri), requireContext())!!
                        )
                        delay(2500L)
                    }
                }.join()
                if (!File(uri).isFile) {
                    delListner.onClick()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    @Throws(IntentSender.SendIntentException::class)
    private fun deleteAPI30(imageUri: Uri) {
        val contentResolver = this.requireContext().contentResolver
        // API 30
        val uriList: List<Uri> = mutableListOf(imageUri)
        val pendingIntent = MediaStore.createDeleteRequest(contentResolver, uriList)
        (this.requireContext() as Activity).startIntentSenderForResult(
            pendingIntent.intentSender,
            DELETE_REQUEST_CODE, null, 0,
            0, 0, null
        )

    }

    override fun onPause() {
        super.onPause()
        println(55555555)
        var scroll = this.view?.findViewById<ScrollView>(R.id.scroll)
        scroll?.fullScroll(View.FOCUS_UP)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_image_page, container, false)
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
        fun newInstance(param1: String, foldername: String) {
            BrowseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_OBJECT, param1)
                    putString(ARG_OBJECT2, foldername)
                }
            }
        }
    }


}