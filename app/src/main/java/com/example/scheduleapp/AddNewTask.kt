package com.example.scheduleapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_new_task.*
import java.lang.IllegalArgumentException
import java.sql.Date
import java.sql.Time


class AddNewTask : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)

        SubmitButton.setOnClickListener {
            if(!nameBox.text.isEmpty()){
                val taskName:String = nameBox.text.toString()
                val task:Task=Task(taskName)
                var plannedTime: Time?=null
                var plannedDate: Date?=null
                //try to add date and time to the task
                try {
                    plannedTime=Time.valueOf(timeBox.text.toString())

                    task.setPlannedTime(plannedTime)
                }
                catch(e: IllegalArgumentException){
                    try {//add seconds to the time in case user forgot
                        plannedTime = Time.valueOf(timeBox.text.toString()+":00")

                        task.setPlannedTime(plannedTime)
                    }
                    catch(e: IllegalArgumentException){
                        Log.e("Time Error",e.toString())
                    }
                }
                try{
                    //this manipulates the date format a little to match that of java.sql.Date
                    val now = Date(System.currentTimeMillis())
                    val parts: List<String> = dateBox.text.toString().split("/")
                    var text:String =""
                    if(parts.size==2){
                        text=now.year.toString()+"-"+parts.get(1)+"-"+parts.get(0)
                        plannedDate=Date.valueOf(text)
                        task.setPlannedDate(plannedDate)
                    }
                    else if (parts.size==3){
                        text=parts.get(2)+"-"+parts.get(1)+"-"+parts.get(0)
                        plannedDate=Date.valueOf(text)
                        task.setPlannedDate(plannedDate)
                    }
                    else{
                        Log.e("Date Error","Wrong date components")
                    }

                }
                catch(e: IllegalArgumentException){
                    Log.e("Date Error",e.toString())
                }
                //add the new task to the list
                MainActivity.tasks.add(task)
                //start main activity
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}