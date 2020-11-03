package com.example.scheduleapp

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.analytics.HitBuilders
import kotlinx.android.synthetic.main.activity_add_new_task.*
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

class AddNewRepeatingTask : AppCompatActivity() {

    var selectedTags: ArrayList<TaskTag> = arrayListOf()//holds pointers to the tags

    var tagList: LinearLayout?=null

    //this is needed for my little onclick listeners
    var myContext: Context?=null


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {

        //Google Analytics
        // Obtain the shared Tracker instance.
        val application: AnalyticsApplication =  getApplication() as AnalyticsApplication
        val mTracker = application.getDefaultTracker()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_repeating_task)



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
                        //put pointer to the tag into selected tag
                        selectedTag=tag
                    }
                }
                if(selectedTag!=null){
                    //TODO make sure the tag is not already selected
                    //render task
                    val newTag = TagView(myContext!!,selectedTag)
                    selectedTags.add(selectedTag)
                    newTag.setOnClickListener {
                        //remove tag
                        selectedTags.remove(selectedTag)
                        //remove View
                        tagList?.removeView(newTag)

                    }
                    tagList?.addView(newTag)
                }

                return true
            }

        })

        if(MainActivity.selectedRepeatingTask!=null){
            //there is a task selected
            //lets fill in the values
            nameInput.setText(MainActivity.selectedTask!!.getName())
            descriptionInput.setText(MainActivity.selectedTask!!.getDescription())
            timePicker.hour=MainActivity.selectedTask!!.getPlannedTime().hours
            timePicker.minute=MainActivity.selectedTask!!.getPlannedTime().minutes
            //add tags
            for(tag in MainActivity.selectedTask!!.tags){
                val newTagView = TagView(myContext!!,tag)
                selectedTags.add(tag)
                newTagView.setOnClickListener {
                    //remove tag
                    selectedTags.remove(tag)
                    //remove View
                    tagList?.removeView(newTagView)

                }
                tagList?.addView(newTagView)
            }
        }
        //deal with adding tasks
        newTagButton.setOnClickListener {

            //inflate menu
            val inflater: MenuInflater = popup.getMenuInflater()
            inflater.inflate(R.menu.tag_select_menu, popup.getMenu())
            popup.show()
        }

        SubmitButton.setOnClickListener {
            //setup our new repeating task
            var taskName:String = nameInput.text.toString()
            var descript:String= descriptionInput.text.toString()
            //repalce wierd chars with spce for when it is saved to a text file
            taskName=taskName.replace("\n"," ")
            taskName=taskName.replace("\t","")
            descript=descript.replace("\n"," ")
            descript=descript.replace("\t","")

            val timePicker: TimePicker =findViewById(R.id.timePicker)
            var plannedTime: Time = Time(timePicker.hour,timePicker.minute,0)
            //TODO add option ot exclude date
            var dueDate:java.sql.Date = java.sql.Date(addTaskDatePicker.year-1900,addTaskDatePicker.month,addTaskDatePicker.dayOfMonth)

            if(MainActivity.selectedTask==null) {
                val task:Task=Task(taskName)


                task.setdueDate(dueDate)
                task.setPlannedTime(plannedTime)
                task.setDescription(descript)

                task.tags.addAll(selectedTags)

                //we are going to make a new task
                //add the new task to the list
                MainActivity.getSelectedDayList().addTask(this,task)

                //schedule the notification for the task
                MainActivity.toSchedule.push(task)
                MainActivity.toScheduleDays.push(MainActivity.getSelectedDayList())

                //google analytics
                mTracker.send(
                    HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("NewTask")
                    .build())
            }
            else{

                //we are going to update exsisting task
                MainActivity.selectedTask!!.setName(taskName)
                MainActivity.selectedTask!!.setdueDate(dueDate)
                MainActivity.selectedTask!!.setPlannedTime(plannedTime)
                MainActivity.selectedTask!!.setDescription(descript)

                //deal with tags
                //clear all tags
                MainActivity.selectedTask!!.tags.clear()
                //readd tags
                MainActivity.selectedTask!!.tags.addAll(selectedTags)

                //delete scheduled notification in case the time has changed
                MainActivity.selectedTask!!.cancelNotification(this)
                //this may not work
                //because I suspect that one needs to pass the context for the MainActivity

                //the task has to be reordered in the daylist
                MainActivity.getSelectedDayList().reOrderTask(this,MainActivity.selectedTask!!)

                //schedule the notification for the task
                MainActivity.toSchedule.push(MainActivity.selectedTask)
                MainActivity.toScheduleDays.push(MainActivity.getSelectedDayList())

                //google analytics
                mTracker.send(
                    HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("EditTask")
                    .build())
            }
            //start main activity
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onResume(){
        super.onResume()
        //Google analytics stuff
        MainActivity.mTracker?.setScreenName("AddNewRepeatingTask");
        MainActivity.mTracker?.send(HitBuilders.ScreenViewBuilder().build())
    }
}