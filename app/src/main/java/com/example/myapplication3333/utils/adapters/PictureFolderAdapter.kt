package com.example.myapplication3333.utils.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myapplication3333.ImageDisplayActivity
import com.example.myapplication3333.MainActivity
import com.example.myapplication3333.R
import com.example.myapplication3333.utils.entityes.FolderHolder
import com.example.myapplication3333.utils.entityes.ImageFolder

class PictureFolderAdapter(val folders: ArrayList<ImageFolder>, val folderContx: Context) :
    RecyclerView.Adapter<FolderHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderHolder {
        val inflater = LayoutInflater.from(parent.context)
        val cell: View = inflater.inflate(R.layout.picture_folder_item, parent, false)
        return FolderHolder(cell)
    }

    override fun onBindViewHolder(holder: FolderHolder, position: Int) {
        val folder: ImageFolder = folders[position]
        if (folder.firstPic != null) {
            Glide.with(folderContx)
                .load(folder.firstPic)
                .apply(RequestOptions().centerCrop())
                .into(holder.folderPic)
        }

        //setting the number of images
        val text = "" + folder.FolderName
        val folderSizeString = "" + folder.numberOfPics.toString() + " Media"
        holder.folderSize.text = folderSizeString
        holder.folderName.text = text
        holder.folderPic.setOnClickListener {
            folderContx.startActivity(Intent(folderContx, ImageDisplayActivity::class.java).apply {
                this.putStringArrayListExtra("imgs",  folder.imgPaths)
                this.putExtra("folderName", holder.folderName.text)
            })
        }
    }


    override fun getItemCount(): Int {
        return folders.size
    }

}