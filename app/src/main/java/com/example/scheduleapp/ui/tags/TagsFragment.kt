package com.example.scheduleapp.ui.tags

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.app.AlertDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.Gravity
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
import com.google.android.material.floatingactionbutton.FloatingActionButton

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
            tagView.setOnClickListener {
                //deselect tag
                MainActivity.selectedTag=tag
                (activity as MainActivity?)?.startAddNewTagFragment()
            }
            val tagHolderParams:LinearLayout.LayoutParams=LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            tagHolderParams.gravity=Gravity.CENTER
            tagHolder.layoutParams=tagHolderParams
            tagHolder.addView(tagView)


            val deleteButton:ImageButton= ImageButton(root.context)
            val deleteLayout:LinearLayout.LayoutParams=LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            deleteLayout.gravity=Gravity.RIGHT
            deleteButton.layoutParams=deleteLayout
            deleteButton.setImageResource(R.drawable.ic_delete)
            deleteButton.background=null
            deleteButton.setOnClickListener {
                //an alert box confirming the delete
                Log.e("TESTING","clicked")
                //this builder is used to setup the dialogue box
                val builder: AlertDialog.Builder= AlertDialog.Builder(root.context)
                    .setMessage(
                        Html.fromHtml(
                        "Are you sure you want to delete the tag: <b>"
                                + tag.getName() + "</b> ?"))
                    .setCancelable(false)//prevents calculation
                    //yes button deletes alarm
                    .setPositiveButton("yes"
                    ) { _, _ -> //delete alarm here

                        //remove from view
                        tagList?.removeView(tagHolder)

                        //remove from the big list
                        MainActivity.tags.remove(tag)

                        //delete from storage
                        MainActivity.exportTags(root.context)
                    }
                    //no button does nothing
                    .setNegativeButton("no"
                    ) { di, _ -> //this closes the message box
                        di.cancel()
                    }
                builder.create().show()
                true
            }
            tagHolder.addView(deleteButton)
            tagList?.addView(tagHolder)
        }

        //deal with plus button
        val addTask: FloatingActionButton =root.findViewById(R.id.addTagButton)
        addTask.setOnClickListener {
            //deselect tag
            MainActivity.selectedTag=null
            (activity as MainActivity?)?.startAddNewTagFragment()
        }

        return root
    }

    //runs after we come home after making us a new tag
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume() {
        super.onResume()
        tagList?.removeAllViews()//clear it all
        //add all tags
        for(tag in MainActivity.tags){
            val tagHolder:LinearLayout= LinearLayout(context)
            val tagView= context?.let { TagView(it,tag) }
            tagView?.setOnClickListener {
                //deselect tag
                MainActivity.selectedTag=tag
                (activity as MainActivity?)?.startAddNewTagFragment()
            }

            val tagHolderParams:LinearLayout.LayoutParams=LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            tagHolderParams.gravity=Gravity.CENTER
            tagHolder.layoutParams=tagHolderParams
            tagHolder.addView(tagView)


            val deleteButton:ImageButton= ImageButton(context)
            val deleteLayout:LinearLayout.LayoutParams=LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            deleteLayout.gravity=Gravity.RIGHT
            deleteButton.layoutParams=deleteLayout
            deleteButton.setImageResource(R.drawable.ic_delete)
            deleteButton.background=null
            deleteButton.setOnClickListener {
                //an alert box confirming the delete
                //this builder is used to setup the dialogue box
                val builder: AlertDialog.Builder= AlertDialog.Builder(context)
                    .setMessage(
                        Html.fromHtml(
                            "Are you sure you want to delete the tag: <b>"
                                    + tag.getName() + "</b> ?"))
                    .setCancelable(false)//prevents calculation
                    //yes button deletes alarm
                    .setPositiveButton("yes"
                    ) { _, _ -> //delete alarm here

                        //remove from view
                        tagList?.removeView(tagHolder)

                        //remove from the big list
                        MainActivity.tags.remove(tag)

                        //delete from storage
                        context?.let { it1 -> MainActivity.exportTags(it1) }
                    }
                    //no button does nothing
                    .setNegativeButton("no"
                    ) { di, _ -> //this closes the message box
                        di.cancel()
                    }
                builder.create().show()
                true
            }
            tagHolder.addView(deleteButton)
            tagList?.addView(tagHolder)
        }
    }
}