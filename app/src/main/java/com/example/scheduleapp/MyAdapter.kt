package com.example.scheduleapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*


class MyAdapter(seed: Int) : RecyclerView.Adapter<RecyclerViewHolder>() {
    private val random: Random
    override fun getItemViewType(position: Int): Int {
        return R.layout.my_text_view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.view.setText(random.nextInt().toString())
    }

    override fun getItemCount(): Int {
        return 100
    }

    init {
        random = Random(seed.toLong())
    }
}