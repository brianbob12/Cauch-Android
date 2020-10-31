package com.example.scheduleapp.ui.classroom

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


class ClassroomFragment : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_classroom, container, false)
        val textView: TextView = root.findViewById(R.id.text_tags)

        /*
        object : AsyncTask<Void?, Void?, Void?>() {
            var result: String? = null
            protected override fun doInBackground(vararg voids: Void?): Void? {
                MainActivity.myInterface.main(root.context, arrayOf())
                return null
            }

            override fun onPostExecute(aVoid: Void?) {
                Log.e("TESTING CLASSROOM","FINISHED")
                super.onPostExecute(aVoid)
            }

        }.execute()
        */



        return root
    }
}