package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import org.mortbay.jetty.Main
import java.util.*
import kotlin.random.Random


class MainActivity : AppCompatActivity {

    private lateinit var appBarConfiguration: AppBarConfiguration



    companion object {

        //linked list of available tags
        //TODO export and import tags
        public var tags:ArrayList<TaskTag> = arrayListOf<TaskTag>()
        public var tagLookup:HashMap<String,TaskTag> = HashMap<String,TaskTag>()//I love hash tables!

        //days stuff
        public var selectedDay:java.sql.Date= java.sql.Date(java.util.Date().time)//today's data as java.sql.Date
        //Hashmap maps java.sql.Date.toString() to a DayList
        private var dayToDayList:HashMap<String,DayList> = HashMap<String,DayList>()

        //list of days is used for the import and export of DayLists trough serializable
        private var days: ArrayList<DayList> = arrayListOf()

        //list of tasks to be sheduled
        public var toSchedule:LinkedList<Task> = LinkedList()
        //the correspondong daylist of each task
        public var toScheduleDays:LinkedList<DayList> = LinkedList()

        //the selected task
        //if this is null nothing is selected
        public var selectedTask:Task?=null

        //returns the day list for the selected day and creates one if one does not exsist
        fun getSelectedDayList(): DayList {
            //creates a new day list for selectedDay
            if(dayToDayList.get(selectedDay.toString())!=null){

                return dayToDayList.get(selectedDay.toString())!!//dont' worry no runtime errors here
            }
            addDayList(selectedDay)//prevents the day being made again
            return dayToDayList.get(selectedDay.toString())!!//dont' worry no runtime errors here
        }

        //creates a new DayList from the date and adds it to days and dayToDayList
        // if this date already exsits the funciton returns without doing anything
        public fun addDayList(date: java.sql.Date){//java.sql.Date of disambiguation

            //check to see if the date already exsits
            if(dayToDayList.get(date.toString())!=null){
                //day already exsits
                return
            }
            val newDay=DayList(date)
            //add to hashmap
            dayToDayList.set(date.toString(),newDay)
            days.add(newDay)

            //dates are exported in the AddNewTask activity
        }
        //managing the selected day
        //moves selected day forward by one
        public fun forwardDay(context: Context){
            //move selected day forward one
            val c = Calendar.getInstance()
            c.time = selectedDay
            c.add(Calendar.DATE, 1)
            selectedDay=java.sql.Date(c.timeInMillis)
            //load day if need be
            if(!getSelectedDayList().loaded){
                getSelectedDayList().readDay(context)
            }
        }
        //moves selected day backwards by one
        public fun backDay(context: Context){
            //move selected day back one
            val c = Calendar.getInstance()
            c.time = selectedDay
            c.add(Calendar.DATE, -1)
            selectedDay=java.sql.Date(c.timeInMillis)
            //load day if need be
            if(!getSelectedDayList().loaded){
                getSelectedDayList().readDay(context)
            }
        }

    }

    constructor():super(){
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {

        // Create an explicit intent for an Activity in your app

        //create noticication channel
        //note if the channel alredy exsits nothing is done
        createNotificationChannel()

        //schedule unsheduled tasks

        for(i in 0..MainActivity.toSchedule.size-1){
            this.scheduleTaskNotification(MainActivity.toSchedule.get(i),MainActivity.toScheduleDays.get(i).date)
            //save tasks here
            //save the main day with the new task
            MainActivity.getSelectedDayList().saveDay(this)
        }


        //run manditory stuff
        super.onCreate(savedInstanceState)
        //google auth task_quick.xml
        val myInterface:GoogleClassroomInterface= GoogleClassroomInterface()

        //myInterface.main(this, arrayOf())//this needs to be async

        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //create tags as defult
        tags= arrayListOf()
        this.addTag(TaskTag("Math", Color.parseColor("#E0FEFE")))
        this.addTag(TaskTag("English", Color.parseColor("#C7CEEA")))
        this.addTag(TaskTag("Physics", Color.parseColor("#FFDAC1")))

        //load day if need be
        if(!getSelectedDayList().loaded){
            getSelectedDayList().readDay(this)
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_analytics, R.id.nav_classroom,R.id.nav_tags), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    public fun startAddNewTaskFragment(){
        startActivity(Intent(this, AddNewTask::class.java))
    }

    //adds tags
    public fun addTag(tag:TaskTag){
        tags.add(tag)
        tagLookup.set(tag.toString(),tag)
        //TODO export
    }

    //setup notification channell
    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("TaskReady", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    //takes a task and schedules a notification
    //the notification will run if the app is open or not.
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun scheduleTaskNotification(task:Task,date:Date){
        //check if task has schedlued time
        if(task.getPlannedTime()==null){
            return
        }
        val cal:Calendar=Calendar.getInstance()
        cal.time=task.getPlannedTime()
        val cal2:Calendar= Calendar.getInstance()
        cal2.time=date
        //merge cal1 and cal2
        cal.set(Calendar.DAY_OF_YEAR,cal2.get(Calendar.DAY_OF_YEAR))
        cal.set(Calendar.YEAR,cal2.get(Calendar.YEAR))
        cal.set(Calendar.MONTH,cal2.get(Calendar.MONTH))

        val calNow:Calendar= Calendar.getInstance()
        calNow.time=java.sql.Date(java.util.Date().time)//represents now

        val timeTo=cal.timeInMillis-calNow.timeInMillis

        if(timeTo<0){
            //the time has already passed
            return
        }

        //from now on we know we want to do this
        //the bundle holds data for whent he notification needs to be sent
        val bundle = PersistableBundle()
        bundle.putString("name", task.getName())
        bundle.putString("description", task.getDescription())


        val jobScheduler = getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        task.scheduleID=Random.nextInt()//random
        val jobInfo = JobInfo.Builder(11, ComponentName(this@MainActivity, NotificationJob::class.java))
            .setMinimumLatency(timeTo)
            .setExtras(bundle)
            .build()
        jobScheduler.schedule(jobInfo)
    }

}