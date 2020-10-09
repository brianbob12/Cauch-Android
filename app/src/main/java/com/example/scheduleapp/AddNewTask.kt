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
import android.widget.TextView
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.contains
import androidx.core.view.forEach
import androidx.core.view.isEmpty
import kotlinx.android.synthetic.main.activity_add_new_task.*
import java.sql.Time


class AddNewTask() : AppCompatActivity() {
    //TODO pass this activity the selected DayList

    //holds the tagList
    var tagList: LinearLayout?=null

    var selectedTags: ArrayList<TaskTag> = arrayListOf()

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
        //add items only if the popup menu is empty
        //TODO check which items are included int he popup menu and add only add excluded items

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
                    var taskView: View = View.inflate(myContext,R.layout.task_tag, tagList)
                    //wait for the view to finish inflating to begin configuring
                    //set name
                    taskView.findViewById<TextView>(R.id.tagName).setText(selectedTag.getName())
                    //set background color or the tag thing.
                    taskView.findViewById<ConstraintLayout>(R.id.layout).background.setTint(selectedTag.getColor())

                    //add tag to selected tags
                    selectedTags.add(selectedTag)
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
            val taskName:String = nameInput.text.toString()
            val timePicker:TimePicker=findViewById(R.id.timePicker)

            val task:Task=Task(taskName)
            var plannedTime: Time= Time(timePicker.hour,timePicker.minute,0)
            task.setPlannedTime(plannedTime)

            //TODO add a mechanism to pick tags
            var tagTaskTag:TaskTag = TaskTag("TAG", Color.parseColor("#FF0000"))
            if(selectedTags.size>0) {
                //TODO add all selected tags
                task.setMyTag(selectedTags.get(0))
            }

            //add the new task to the list
            MainActivity.todayDayList().addTask(task)
            //start main activity
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}