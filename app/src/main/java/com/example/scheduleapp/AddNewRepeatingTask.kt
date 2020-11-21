package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */


import android.app.AlertDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.scheduleapp.RepeatingTasks.RepeatingTask
import com.google.android.gms.analytics.HitBuilders
import kotlinx.android.synthetic.main.activity_add_new_repeating_task.*
import org.mortbay.jetty.Main
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

        val switch:Switch=findViewById(R.id.fixedTimeSwitch)//for later
        //setup spinner
        val mySpinner1:Spinner = findViewById(R.id.addRepeatingTaskSpinner1)
        val mySpinner2:Spinner = findViewById(R.id.addRepeatingTaskSpinner2)
        val arraySpinner1 = arrayOf(
            "Day","Sunday","Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Weekday"
        )
        val adapter1 = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, arraySpinner1
        )
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner1.adapter = adapter1

        val arraySpinner2 = arrayOf(
            "Every","Every Other"
        )
        val adapter2 = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, arraySpinner2
        )
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        mySpinner2.adapter = adapter2

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
                    //make sure the tag cannot be selected again
                    menuItem.setEnabled(false)
                    //render task
                    val newTag = TagView(myContext!!,selectedTag)
                    selectedTags.add(selectedTag)
                    newTag.setOnClickListener {
                        //remove tag
                        selectedTags.remove(selectedTag)
                        //remove View
                        tagList?.removeView(newTag)
                        //reenable tag opton
                        menuItem.setEnabled(true)

                    }
                    tagList?.addView(newTag)
                }

                return true
            }

        })

        val timePicker: TimePicker =findViewById(R.id.timePicker)
        val timeHolder:ConstraintLayout=findViewById(R.id.timeHolder)

        if(MainActivity.selectedRepeatingTask!=null){
            //there is a task selected
            //lets fill in the values
            nameInput.setText(MainActivity.selectedRepeatingTask!!.getName())
            descriptionInput.setText(MainActivity.selectedRepeatingTask!!.getDescription())
            if(MainActivity.selectedRepeatingTask!!.getPlannedTime()!=null) {
                timePicker.hour = MainActivity.selectedRepeatingTask!!.getPlannedTime()!!.hours
                timePicker.minute = MainActivity.selectedRepeatingTask!!.getPlannedTime()!!.minutes
            }
            //add tags
            for(tag in MainActivity.selectedRepeatingTask!!.tags){
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
            //set fixed time switch
            switch.isChecked= !MainActivity.selectedRepeatingTask!!.fixedTime
            if(MainActivity.selectedRepeatingTask!!.fixedTime&& MainActivity.selectedRepeatingTask!!.getPlannedTime()!=null){
                //update clock
                timePicker.hour= MainActivity.selectedRepeatingTask!!.getPlannedTime()?.hours!!
                timePicker.minute= MainActivity.selectedRepeatingTask!!.getPlannedTime()?.minutes!!
            }

            //deal with start date
            val cal:Calendar = Calendar.getInstance()
            cal.time=MainActivity.selectedRepeatingTask!!.getStartDate()
            addRepeatingTaskDatePicker.updateDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))

            //deal with spinners
            if(MainActivity.selectedRepeatingTask!!.everyOther){
                mySpinner2.setSelection(1)
            }
            //set spinner 1
            //there was probably a better way to do this
            if(MainActivity.selectedRepeatingTask!!.repeatsSettingString=="Day"){
                mySpinner1.setSelection(0)
            }
            if(MainActivity.selectedRepeatingTask!!.repeatsSettingString=="Sunday"){
                mySpinner1.setSelection(1)
            }
            if(MainActivity.selectedRepeatingTask!!.repeatsSettingString=="Monday"){
                mySpinner1.setSelection(2)
            }
            if(MainActivity.selectedRepeatingTask!!.repeatsSettingString=="Tuesday"){
                mySpinner1.setSelection(3)
            }
            if(MainActivity.selectedRepeatingTask!!.repeatsSettingString=="Wednesday"){
                mySpinner1.setSelection(4)
            }
            if(MainActivity.selectedRepeatingTask!!.repeatsSettingString=="Thursday"){
                mySpinner1.setSelection(5)
            }
            if(MainActivity.selectedRepeatingTask!!.repeatsSettingString=="Friday"){
                mySpinner1.setSelection(6)
            }
            if(MainActivity.selectedRepeatingTask!!.repeatsSettingString=="Saturday"){
                mySpinner1.setSelection(7)
            }
            if(MainActivity.selectedRepeatingTask!!.repeatsSettingString=="Weekday"){
                mySpinner1.setSelection(8)
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


            var plannedTime: Time = Time(timePicker.hour,timePicker.minute,0)
            var startDate:java.sql.Date = java.sql.Date(addRepeatingTaskDatePicker.year-1900,addRepeatingTaskDatePicker.month,addRepeatingTaskDatePicker.dayOfMonth)
            //a calendar of the startDate for checking stuff
            val startCal:Calendar = Calendar.getInstance()
            startCal.time=startDate


            //get repetition settings
            val everyOther:Boolean = (mySpinner2.selectedItem as String) =="Every Other"

            //get day of week
            val dayChoice= mySpinner1.selectedItem as String
            //check that the start date is correct

            if(dayChoice=="Sunday"){
            //check that that start date is a sunday
            if(startCal.get(Calendar.DAY_OF_WEEK)!=Calendar.SUNDAY){
                this.invalidStartDate(dayChoice)
                //end this onClickListener function
                return@setOnClickListener
                }
            }
            else if(dayChoice=="Monday"){
                //check that that start date is a monday
                if(startCal.get(Calendar.DAY_OF_WEEK)!=Calendar.MONDAY){
                    this.invalidStartDate(dayChoice)
                    //end this onClickListener function
                    return@setOnClickListener
                }
            }
            else if(dayChoice=="Tuesday"){
                //check that that start date is a tuesday
                if(startCal.get(Calendar.DAY_OF_WEEK)!=Calendar.TUESDAY){
                    this.invalidStartDate(dayChoice)
                    //end this onClickListener function
                    return@setOnClickListener
                }
            }
            else if(dayChoice=="Wednesday"){
                //check that that start date is a wednesday
                if(startCal.get(Calendar.DAY_OF_WEEK)!=Calendar.WEDNESDAY){
                    this.invalidStartDate(dayChoice)
                    //end this onClickListener function
                    return@setOnClickListener
                }
            }
            else if(dayChoice=="Thursday"){
                //check that that start date is a thursday
                if(startCal.get(Calendar.DAY_OF_WEEK)!=Calendar.THURSDAY){
                    this.invalidStartDate(dayChoice)
                    //end this onClickListener function
                    return@setOnClickListener
                }
            }
            else if(dayChoice=="Friday"){
                //check that that start date is a friday
                if(startCal.get(Calendar.DAY_OF_WEEK)!=Calendar.FRIDAY){
                    this.invalidStartDate(dayChoice)
                    //end this onClickListener function
                    return@setOnClickListener
                }
            }
            else if(dayChoice=="Saturday"){
                //check that that start date is a saturday
                if(startCal.get(Calendar.DAY_OF_WEEK)!=Calendar.SATURDAY){
                    this.invalidStartDate(dayChoice)
                    //end this onClickListener function
                    return@setOnClickListener
                }
            }
            else if(dayChoice=="Weekday"){
                //check that hte start dat is not a saturday or sunday
                if(startCal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
                    this.invalidStartDate(dayChoice)
                    //end this onClickListener function
                    return@setOnClickListener
                }
                if(startCal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
                    this.invalidStartDate(dayChoice)
                    //end this onClickListener function
                    return@setOnClickListener
                }
            }


            val fixedTime= !fixedTimeSwitch.isChecked

            if(MainActivity.selectedRepeatingTask==null) {
                val task: RepeatingTask =
                    RepeatingTask(
                        taskName,
                        fixedTime,
                        startDate,
                        everyOther
                    )

                task.setRepeatsSetting(dayChoice)

                if(fixedTime) {
                    task.setPlannedTime(plannedTime)
                }
                task.setDescription(descript)

                task.tags.addAll(selectedTags)

                //add to persistent container
                MainActivity.persistentContainer.repeatingTasks.add(task)
                MainActivity.persistentContainer.save(this)

                //google analytics
                mTracker.send(
                    HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("NewTask")
                    .build())
            }
            else{

                //we are going to update exsisting task
                MainActivity.selectedRepeatingTask!!.everyOther=everyOther
                MainActivity.selectedRepeatingTask!!.fixedTime=!switch.isChecked
                MainActivity.selectedRepeatingTask!!.setName(taskName)
                MainActivity.selectedRepeatingTask!!.setRepeatsSetting(dayChoice)
                MainActivity.selectedRepeatingTask!!.setStartDate(startDate)
                if(fixedTime) {
                    MainActivity.selectedRepeatingTask!!.setPlannedTime(plannedTime)
                }
                else{
                    MainActivity.selectedRepeatingTask!!.setPlannedTime(null)
                    //this prevents mistakes from being made elsewhere
                }
                MainActivity.selectedRepeatingTask!!.setDescription(descript)

                //setup repeat setting
                MainActivity.selectedRepeatingTask!!.setRepeatsSetting(dayChoice)

                //deal with tags
                //clear all tags
                MainActivity.selectedRepeatingTask!!.tags.clear()
                //readd tags
                MainActivity.selectedRepeatingTask!!.tags.addAll(selectedTags)

                //save persistentContainer
                MainActivity.persistentContainer.save(this)

                //google analytics
                mTracker.send(
                    HitBuilders.EventBuilder()
                    .setCategory("Action")
                    .setAction("EditRepeatingTask")
                    .build())
            }
            //end
            finish()
        }
    }

    override fun onResume(){
        super.onResume()
        //Google analytics stuff
        MainActivity.mTracker?.setScreenName("AddNewRepeatingTask");
        MainActivity.mTracker?.send(HitBuilders.ScreenViewBuilder().build())
    }

    //sends a popup box saying there is an invalid start date
    private fun invalidStartDate(specification:String){
        //specification is the condition that the start date has to meet
        //for example, "Monday" or "Weekday"
        //an alert box confirming the delete
        //this builder is used to setup the dialogue box
        val builder: AlertDialog.Builder= AlertDialog.Builder(this)
            .setMessage(
                Html.fromHtml(
                    "Ivalid start date please select a <b>"
                            + specification + "</b> ."))
            .setCancelable(true)//

        builder.create().show()
    }
}