package com.example.scheduleapp.ui.tags

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.scheduleapp.R

class TagsFragment : Fragment() {

    private lateinit var tagsViewModel: TagsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        tagsViewModel =
                ViewModelProviders.of(this).get(TagsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_tags, container, false)
        val textView: TextView = root.findViewById(R.id.text_tags)
        tagsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}