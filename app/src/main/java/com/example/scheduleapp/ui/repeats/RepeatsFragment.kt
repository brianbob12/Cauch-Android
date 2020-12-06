package com.example.scheduleapp.ui.repeats

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.R
import com.google.android.gms.analytics.HitBuilders
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_repeats.*


class RepeatsFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_repeats, container, false)

        //deal with plus button
        val addTask: FloatingActionButton =root.findViewById(R.id.addRepeatingTaskButton)

        //temporary
        //addTask.visibility=View.INVISIBLE

        addTask.setOnClickListener {
            //deselect task
            MainActivity.selectedRepeatingTask=null
            (activity as MainActivity?)?.startAddNewRepeatingTaskFragment()

        }

        return root
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResume(){
        super.onResume()
        //Google analytics stuff
        MainActivity.mTracker?.setScreenName("RepeatsFragment");
        MainActivity.mTracker?.send(HitBuilders.ScreenViewBuilder().build())

        //load all tasks
        //remove all exsisiting children
        repeatingTaskList.removeAllViews()

        //add repeating tasks to the list
        for(task in MainActivity.persistentContainer.repeatingTasks){
            val toAdd= LinearLayout(context)
            layoutInflater.inflate(R.layout.repeating_task_card_row,toAdd)

            //get things that have to be changed
            val tagHighlight:LinearLayout = (toAdd.getChildAt(0)as ConstraintLayout).getChildAt(0) as LinearLayout
            val moreButton: ImageButton = (toAdd.getChildAt(0)as ConstraintLayout).getChildAt(1) as ImageButton
            val mainTitle:TextView = (toAdd.getChildAt(0)as ConstraintLayout).getChildAt(2) as TextView
            val scheduleText:TextView = (toAdd.getChildAt(0)as ConstraintLayout).getChildAt(3) as TextView

            //set mainTitle
            mainTitle.text=task.getName()

            //set scheduleText
            var scheduleMessage="Every "
            if(task.everyOther){
                scheduleMessage+="Other "
            }
            scheduleMessage+=task.repeatsSettingString

            scheduleText.text=scheduleMessage

            //deal with the tag highlight
            if(task.tags.size==0){
                tagHighlight.visibility=View.INVISIBLE
            }
            else{
                //render tag highlight
                tagHighlight.visibility=View.VISIBLE

                //change the color of tag highlight to the tag's color
                val background= this.context?.getDrawable(R.drawable.tag_highlight_shape)
                //use the color of the first tag
                background?.setTint(task.tags.get(0).getColor())

                tagHighlight.background=background
            }

            //now for the moreButton
            //setup more popup menu
            //setup popup menu
            val popup = PopupMenu(context,moreButton)
            popup.menu.add("Edit")
            popup.menu.add("Delete")
            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                    val name: String = menuItem!!.toString()//gets selected option
                    //do stuff
                    if(name=="Delete"){
                        //remove task from permentant storge
                        MainActivity.persistentContainer.repeatingTasks.remove(task)
                        context?.let { MainActivity.persistentContainer.save(it) }

                        //removing task from screen


                        repeatingTaskList.removeView(toAdd)




                        //google analytics
                        MainActivity.mTracker?.send(HitBuilders.EventBuilder()
                            .setCategory("Action")
                            .setAction("DeleteRepeatingTask")
                            .build())
                    }
                    else if(name=="Edit"){
                        //select task
                        MainActivity.selectedRepeatingTask=task
                        //launch the AddNewRepeatingTask activity
                        (activity as MainActivity?)?.startAddNewRepeatingTaskFragment()
                    }
                    return true
                }
            })
            moreButton.setOnClickListener {
                //open popup menu
                //inflate menu
                val inflater: MenuInflater = popup.getMenuInflater()
                inflater.inflate(R.menu.tag_select_menu, popup.getMenu())
                popup.show()
            }

            //this line is essencial
            repeatingTaskList.addView(toAdd)


        }
    }
}