package com.example.scheduleapp.ui.tags

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.R
import com.example.scheduleapp.TagView
import com.example.scheduleapp.TaskTag

class TagsFragment : Fragment() {


    private var tagList:LinearLayout? =null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_tags, container, false)


        tagList=root.findViewById(R.id.TagList)

        //add all tags
        for(tag in MainActivity.tags){
            val tagHolder:LinearLayout= LinearLayout(root.context)
            val tagView=TagView(root.context,tag)
            //todo layout perams
            tagHolder.addView(tagView)

            val deleteButton:ImageButton= ImageButton(root.context)
            deleteButton.setImageResource(R.drawable.ic_delete)
            deleteButton.background=null
            tagHolder.addView(deleteButton)
            tagList?.addView(tagHolder)
        }

        return root
    }
}