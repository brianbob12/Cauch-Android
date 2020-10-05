package com.example.scheduleapp

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_add_new_task.*
import java.sql.Time


class AddNewTask : AppCompatActivity() {
    //TODO pass this activity the selected DayList
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)

        SubmitButton.setOnClickListener {
            val taskName:String = nameInput.text.toString()
            val timePicker:TimePicker=findViewById(R.id.timePicker)

            val task:Task=Task(taskName)
            var plannedTime: Time= Time(timePicker.hour,timePicker.minute,0)
            task.setPlannedTime(plannedTime)

            //TODO add a mechanism to pick tags
            var tagTaskTag:TaskTag = TaskTag("TAG", Color.parseColor("#FF0000"))
            task.setMyTag(tagTaskTag)

            //add the new task to the list
            MainActivity.todayDayList().addTask(task)
            //start main activity
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}