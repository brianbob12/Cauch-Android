package com.example.scheduleapp.ui.analytics

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
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.R
import com.google.android.gms.analytics.HitBuilders

class AnalyticsFragment : Fragment() {

    private lateinit var analyticsViewModel: AnalyticsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        analyticsViewModel =
                ViewModelProviders.of(this).get(AnalyticsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_analytics, container, false)
        val textView: TextView = root.findViewById(R.id.text_gallery)
        analyticsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
    override fun onResume(){
        super.onResume()
        //Google analytics stuff
        MainActivity.mTracker?.setScreenName("Analytics");
        MainActivity.mTracker?.send(HitBuilders.ScreenViewBuilder().build())
    }
}