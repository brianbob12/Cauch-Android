package com.example.scheduleapp.ui.repeats

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.R
import com.google.android.gms.analytics.HitBuilders
import com.google.android.material.floatingactionbutton.FloatingActionButton


class RepeatsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_repeats, container, false)

        //deal with plus button
        val addTask: FloatingActionButton =root.findViewById(R.id.addRepeatingTaskButton)

        //temporary
        addTask.visibility=View.INVISIBLE

        addTask.setOnClickListener {
            //deselect task
            MainActivity.selectedTask=null
            (activity as MainActivity?)?.startAddNewRepeatingTaskFragment()

        }

        return root
    }
    override fun onResume(){
        super.onResume()
        //Google analytics stuff
        MainActivity.mTracker?.setScreenName("RepeatsFragment");
        MainActivity.mTracker?.send(HitBuilders.ScreenViewBuilder().build())
    }
}