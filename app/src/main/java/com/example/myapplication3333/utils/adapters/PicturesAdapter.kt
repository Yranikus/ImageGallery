package com.example.myapplication3333.utils.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication3333.DeleteListner
import com.example.myapplication3333.R
import com.example.myapplication3333.fragments.BrowseFragment
import com.example.myapplication3333.utils.entityes.PicHolder

class PicturesAdapter(val pictureList: ArrayList<String>, val pictureContx: Context,
                      var fragmentManager: FragmentManager, val foldername: String): RecyclerView.Adapter<PicHolder>() {



    override fun onCreateViewHolder(container: ViewGroup, position: Int): PicHolder {
        val inflater = LayoutInflater.from(container.context)
        val cell: View = inflater.inflate(R.layout.pic_holder_item, container, false)
        return PicHolder(cell)
    }

    override fun onBindViewHolder(holder: PicHolder, position: Int) {
        val image = pictureList[position]
        Glide.with(pictureContx)
            .load(image)
            .apply(RequestOptions().centerCrop())
            .into(holder.picture)
        ViewCompat.setTransitionName(holder.picture, position.toString() + "_image")
        holder.picture.setOnClickListener {
            var frag = BrowseFragment.newInstance(
                pictureList,
                position,
                foldername
            )
            frag.onClick(object :DeleteListner{
                override fun onClick() {
                    notifyDataSetChanged()
                }

            })
            fragmentManager.beginTransaction().add(
                R.id.displayContainer, frag
            ).addToBackStack(null).commit()

        }
    }

    override fun getItemCount(): Int {
        return pictureList.size
    }
}
