package com.example.myapplication3333.utils.entityes

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication3333.R

class PicHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var picture: ImageView

    init {
        picture = itemView.findViewById(R.id.image)
    }
}
