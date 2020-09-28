package com.example.scheduleapp

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class RecyclerViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    val view: TextView

    init {
        view = itemView.findViewById(R.id.mainTitle)
    }
}