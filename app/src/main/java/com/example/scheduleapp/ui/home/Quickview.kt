package com.example.scheduleapp.ui.home

import android.graphics.Color
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.R
import com.example.scheduleapp.TagView
import com.example.scheduleapp.Tasks.Task
import com.google.android.gms.analytics.HitBuilders
import java.util.*


class Quickview {
    //PopupWindow display method
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun showPopupWindow(view: View, task: Task, activity:MainActivity?) {

        //Google analytics stuff
        MainActivity.mTracker?.setScreenName("TaskQuickView");
        MainActivity.mTracker?.send(HitBuilders.ScreenViewBuilder().build())

        //Create a View object yourself through inflater
        val inflater = LayoutInflater.from(view.context)
        val popupView: View = inflater.inflate(R.layout.task_quick,null)

        //Specify the length and width through constants
        val width = LinearLayout.LayoutParams.MATCH_PARENT
        val height = LinearLayout.LayoutParams.MATCH_PARENT

        //Make Inactive Items Outside Of PopupWindow
        val focusable = true

        //Create a window with our parameters
        val popupWindow = PopupWindow(popupView, width, height, focusable)

        //Set the location of the window on the screen
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)

        //Add the information
        val nameBox: TextView = popupView.findViewById(R.id.nameText)
        nameBox.text=task.getName()
        val descriptBox: TextView = popupView.findViewById(R.id.descriptionBox)
        descriptBox.text=task.getDescription()

        //due date
        val dueDateText: TextView = popupView.findViewById(R.id.dueDateBox)
        if(task.getdueDate()!=null) {
            dueDateText.visibility=View.VISIBLE
            dueDateText.setTextColor(Color.BLACK)
            var dayMessge = task.getdueDate().toString()
            //check if day is today
            val cal1: Calendar = Calendar.getInstance()
            val cal2: Calendar = Calendar.getInstance()
            cal1.time = task.getdueDate()
            cal2.time = java.sql.Date(java.util.Date().time)//represents today
            //make red if due soon
            //check if tag is due tomorrow or sooner
            val numberOfDays=(cal1.getTime().time - cal2.getTime().time) / (1000 * 60 * 60 * 24)//number of days between cal1 and cal2
            if(numberOfDays<1){
                //make text orange
                dueDateText.setTextColor(Color.argb(255,255,125,0))
            }
            if(numberOfDays<0){
                //make text red
                dueDateText.setTextColor(Color.RED)
            }


            if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
            ) {
                dayMessge = "Today"
            }
            cal2.add(Calendar.DATE, 1)//now cal2 is tomorrow
            if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
            ) {
                dayMessge = "Tomorrow"
            }
            cal2.add(Calendar.DATE, -2)//now cal2 is yesterday
            if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
            ) {
                dayMessge = "Yesterday"
            }
            dueDateText.text = "Due: " + dayMessge
        }
        else{
            dueDateText.visibility=View.INVISIBLE
        }

        //deal with button
        val buttonEdit: Button = popupView.findViewById(R.id.editButton)
        buttonEdit.setOnClickListener{
            //select task
            MainActivity.selectedTask=task
            //launch the AddNewTask activity
            activity?.startAddNewTaskFragment()
        }

        //add tags
        val tagHolder:LinearLayout=popupView.findViewById(R.id.tagList)
        for(tag in task.tags){
            val newTagView=TagView(popupView.context,tag)
            tagHolder.addView(newTagView)
        }

        //Handler for clicking on the inactive zone of the window
        popupView.setOnTouchListener { v, event -> //Close the window when clicked
            popupWindow.dismiss()
            true
        }
    }
}