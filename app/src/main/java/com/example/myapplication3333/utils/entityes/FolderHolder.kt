package com.example.myapplication3333.utils.entityes

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication3333.R

class FolderHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    var folderPic: ImageView
    var folderName: TextView
    var folderSize: TextView
    var folderCard: CardView

    init {
        folderPic = itemView.findViewById(R.id.folderPic)
        folderName = itemView.findViewById(R.id.folderName)
        folderSize = itemView.findViewById(R.id.folderSize)
        folderCard = itemView.findViewById(R.id.folderCard)
    }
}