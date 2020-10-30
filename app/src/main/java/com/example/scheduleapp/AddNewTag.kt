package com.example.scheduleapp

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.SeekBar
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_add_new_tag.*
import kotlinx.android.synthetic.main.activity_add_new_task.*
import kotlinx.android.synthetic.main.activity_add_new_task.SubmitButton
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

class AddNewTag : AppCompatActivity() {

    //this is needed for my little onclick listeners
    var myContext: Context?=null
    var tagView: TagView?=null
    var red=244
    var green=253
    var blue=253

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_tag)

        val tag=TaskTag("", Color.parseColor("#E0FEFE"))
        tagView=TagView(this,tag)
        val tagHolder:LinearLayout=findViewById(R.id.tagHolder)

        //add tagView to tag hlder
        tagHolder.addView(tagView)

        myContext=this as Context



        SubmitButton.setOnClickListener {
            MainActivity.tags.add(tag)
            //save tasks
            MainActivity.exportTags(this)
            //go back
            finish()
        }
        redBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                red=progress
                tag.setColor(Color.rgb(red.toFloat()/255,green.toFloat()/255,blue.toFloat()/255))
                refresh()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                return
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                return
            }

        })
        greenBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                green=progress
                tag.setColor(Color.rgb(red.toFloat()/255,green.toFloat()/255,blue.toFloat()/255))
                refresh()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                return
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                return
            }

        })
        blueBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blue=progress
                tag.setColor(Color.rgb(red.toFloat()/255,green.toFloat()/255,blue.toFloat()/255))
                refresh()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                return
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                return
            }

        })

        //deal with text

        nameInputTag.addTextChangedListener {
            //change name
            tag.setName(nameInputTag.text.toString())
            refresh()
        }
    }


    //changes the textView so that the new name and color are shown
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun refresh(){
        //remove tag view
        tagHolder.removeView(tagView)
        //make new view
        val newView=TagView(tagView!!.context,tagView!!.myTag)
        //add new view
        tagHolder.addView(newView)
        //return new view
        tagView=newView
    }
}