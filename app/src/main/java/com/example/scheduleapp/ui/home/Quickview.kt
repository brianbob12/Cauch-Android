package com.example.scheduleapp.ui.home

import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import com.example.scheduleapp.R
import com.example.scheduleapp.TagView
import com.example.scheduleapp.Task


class Quickview {
    //PopupWindow display method
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun showPopupWindow(view: View, task: Task) {

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

        //Initialize the elements of our window, install the handler
        val nameBox: TextView = popupView.findViewById(R.id.nameText)
        nameBox.setText(task.getName())
        val buttonEdit: Button = popupView.findViewById(R.id.editButton)
        buttonEdit.setOnClickListener{
            //As an example, display the message
            Toast.makeText(view.getContext(), "EditTask", Toast.LENGTH_SHORT)
                .show()
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