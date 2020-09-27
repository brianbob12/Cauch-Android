package com.example.scheduleapp

import android.content.ClipData
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

class TaskInList :ConstraintLayout {
    var task:Task? = null;

    //create label for the task
    var name: TextView = TextView(context)

    constructor(context: Context, task: Task) : super(context) {
        this.task = task

        //set params so that it sits properly
        val taskParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        taskParams.setMargins(0, 0, 0, 0)

        layoutParams = taskParams

        addView(makeInfoView(task))


    }


    private fun makeInfoView(task: Task): LinearLayout {
        var infoView: LinearLayout= LinearLayout(context)
        infoView.setBackgroundColor(0xFF0000.toInt())
        name=TextView(context)

        //set params so that it sits properly
        val infoViewParams: LayoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            LayoutParams.MATCH_PARENT
        )
        infoViewParams.setMargins(40, 0, 0, 0)

        infoView.layoutParams = infoViewParams

        //set params so that it sits properly
        val nameParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        nameParams.weight = 1f
        name.layoutParams = nameParams


        name.textSize = 32F
        name.text = task.getName()
        //set it to bold
        name.typeface = Typeface.DEFAULT_BOLD
        name.setTextColor(Color.parseColor("#000000"))

        infoView.addView(name)

        return infoView
    }
}