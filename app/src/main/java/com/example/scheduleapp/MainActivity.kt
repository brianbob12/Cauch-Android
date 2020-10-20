package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    companion object {

        //linked list of available tags
        //TODO export and import tags
        public var tags:ArrayList<TaskTag> = arrayListOf<TaskTag>()

        //Hashmap maps java.sql.Date.toString() to a DayList
        private var dayToDayList:HashMap<String,DayList> = HashMap<String,DayList>()

        //TODO import and export DayLists
        //list of days is used for the import and export of DayLists trough serializable
        private var days: ArrayList<DayList> = arrayListOf()

        //returns the day list for today and creates one if one does not exsist
        fun todayDayList(): DayList {
            val today:java.sql.Date= java.sql.Date(java.util.Date().time)//today's data as java.sql.Date
            //creates a new day list for today
            if(dayToDayList.get(today.toString())!=null){

                return dayToDayList.get(today.toString())!!//dont' worry no runtime errors here
            }
            addDayList(today)//prevents the day being made again
            return dayToDayList.get(today.toString())!!//dont' worry no runtime errors here 
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

            //TODO export dates
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //run manditory stuff
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        //TODO import dates
        val testDay:DayList= todayDayList()
        testDay.readDay(this)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        //create tags as defult
        tags= arrayListOf()
        tags.add(TaskTag("Math", Color.parseColor("#E0FEFE")))
        tags.add(TaskTag("English", Color.parseColor("#C7CEEA")))
        tags.add(TaskTag("Physics", Color.parseColor("#FFDAC1")))

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



}