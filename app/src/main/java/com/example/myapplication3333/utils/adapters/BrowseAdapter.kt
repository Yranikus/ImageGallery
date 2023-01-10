package com.example.myapplication3333.utils.adapters


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication3333.DeleteListner
import com.example.myapplication3333.fragments.ARG_OBJECT
import com.example.myapplication3333.fragments.ARG_OBJECT2
import com.example.myapplication3333.fragments.ImagePageFragment


class BrowseAdapter(val fragment: Fragment,val imgs: ArrayList<String>,
                    val displayDeleteListner: DeleteListner,var foldername: String ) : FragmentStateAdapter(fragment) {

    val DELETE_REQUEST_CODE = 13

    val pageIds= imgs.map { it.hashCode().toLong() }.toMutableList()

    override fun getItemCount(): Int {
        return imgs.size
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun createFragment(position: Int): Fragment {
        val fragmentN = ImagePageFragment()
        fragmentN.arguments = Bundle().apply {
            putString(ARG_OBJECT, imgs[position])
            putString(ARG_OBJECT2, foldername)
        }
        fragmentN.onClick(object : DeleteListner {
            @SuppressLint("NotifyDataSetChanged")
            override fun onClick() {
                imgs.removeAt(position)
                notifyItemRangeChanged(position, imgs.size)
                notifyDataSetChanged()
                displayDeleteListner.onClick()
            }
        })
        return fragmentN
    }

    override fun getItemId(position: Int): Long {
        return imgs[position].hashCode().toLong()
    }




    override fun containsItem(itemId: Long): Boolean {
        return pageIds.contains(itemId)
    }




}