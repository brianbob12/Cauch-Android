package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import java.util.*

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

    override fun onCreate(savedInstanceState: Bundle?) {
        //run manditory stuff
        super.onCreate(savedInstanceState)
        //google auth test
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


}