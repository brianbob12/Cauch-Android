package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.contains
import androidx.core.view.forEach
import androidx.core.view.isEmpty
import kotlinx.android.synthetic.main.activity_add_new_task.*
import kotlinx.android.synthetic.main.task_quick.*
import java.sql.Date
import java.sql.Time


class AddNewTask : AppCompatActivity() {
    //TODO pass this activity the selected DayList

    var selectedTags: ArrayList<TaskTag> = arrayListOf()

    var tagList: LinearLayout?=null

    //this is needed for my little onclick listeners
    var myContext: Context?=null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)



        //setup variables
        tagList =findViewById(R.id.tagLayout)
        myContext=this as Context

        //setup popup menu
        val popup = PopupMenu(this, newTagButton)

        if(popup.menu.size()==0) {
            for (task in MainActivity.tags) {
                popup.menu.add(task.getName())
            }
        }
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                val name:String= menuItem!!.toString()//gets the name of the tag
                //not find the tag
                var selectedTag:TaskTag? =null//this will stay null if the task cannot be found
                for(tag in MainActivity.tags){
                    if(tag.getName()==name){
                        //put copy of tag into selected tag
                        selectedTag=tag.copy()
                    }
                }
                if(selectedTag!=null){
                    //TODO make sure the tag is not already selected
                    //render task
                    val newTag = TagView(myContext!!,selectedTag)
                    selectedTags.add(selectedTag)
                    tagList?.addView(newTag)
                }

                return true
            }

        })

        //deal with adding tasks
        newTagButton.setOnClickListener {

            //inflate menu
            val inflater: MenuInflater = popup.getMenuInflater()
            inflater.inflate(R.menu.tag_select_menu, popup.getMenu())
            popup.show()
        }

        SubmitButton.setOnClickListener {
            var taskName:String = nameInput.text.toString()
            var descript:String= descriptionInput.text.toString()
            //repalce wierd chars with spce for when it is saved to a text file
            taskName=taskName.replace("\n"," ")
            taskName=taskName.replace("\t","")
            descript=descript.replace("\n"," ")
            descript=descript.replace("\t","")

            val timePicker:TimePicker=findViewById(R.id.timePicker)

            val task:Task=Task(taskName)
            var plannedTime: Time= Time(timePicker.hour,timePicker.minute,0)
            //TODO add option ot exclude date
            var dueDate:java.sql.Date = java.sql.Date(myDatePicker.year,myDatePicker.month,myDatePicker.dayOfMonth)
            task.setPlannedTime(plannedTime)
            task.setDescription(descript)

            task.tags.addAll(selectedTags)

            //TODO replace todayDayList with selected day
            //add the new task to the list
            MainActivity.getSelectedDayList().addTask(task)
            //save the main day with the new task
            MainActivity.getSelectedDayList().saveDay(this)
            //start main activity
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}