package com.example.scheduleapp.ui.home
/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.DayList
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment : Fragment() {

    //stuff to do with the RecyclerView
    private var recyclerView:RecyclerView? = null
    private var recycleAdapter: MyAdapter? = null

    //the selected day
    private var selectedDate:DayList = MainActivity.getSelectedDayList()

    //gesture stuff
    public var x1:Float? = null //public because it has to be changed by MyAdapter
    public var y1:Float? = null //public because it has to be changed by MyAdapter
    private val minumumSwipeXDistance = 300
    private val maximumSwipeYDistace = 500

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val root:View = inflater.inflate(R.layout.fragment_home, container, false)
        // Add the following lines to create RecyclerView
        recyclerView = root.findViewById<RecyclerView>(R.id.ListForTasks)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.setLayoutManager(LinearLayoutManager(root.context))

        this.setup(root)



        //deal with onclick listeners

        //deal with plus button
        val addTask: FloatingActionButton =root.findViewById(R.id.addTaskButton)
        addTask.setOnClickListener {
            //deselect task
            MainActivity.selectedTask=null
            (activity as MainActivity?)?.startAddNewTaskFragment()

        }

        val buttonForward: ImageButton =root.findViewById(R.id.dayForwardButton)
        buttonForward.setOnClickListener {

            //move on
            MainActivity.forwardDay(root.context)
            //show the name of the selected day
            val dayName:TextView= root.findViewById(R.id.dayName)
            dayName.text=MainActivity.getSelectedDayList().toString()
            //change day
            this.changeDay()
        }

        val buttonBack: ImageButton =root.findViewById(R.id.dayBackButton)
        buttonBack.setOnClickListener {

            //move on
            MainActivity.backDay(root.context)
            //show the name of the selected day
            val dayName:TextView= root.findViewById(R.id.dayName)
            dayName.text=MainActivity.getSelectedDayList().toString()
            //change day
            this.changeDay()
        }

        //swipe to move days
        val myLayout:ConstraintLayout= root.findViewById(R.id.homeLayout)
        myLayout.setOnTouchListener(object:View.OnTouchListener{
            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                if(motionEvent?.action== MotionEvent.ACTION_DOWN){
                    x1 = motionEvent.getX()
                    return true
                }
                else if(motionEvent?.action==MotionEvent.ACTION_UP){
                    if(x1!=null){
                        val distance= x1!! -motionEvent.getX()
                        if(distance<-minumumSwipeXDistance){
                            //right swipe
                            //move on
                            MainActivity.forwardDay(root.context)
                            //show the name of the selected day
                            val dayName:TextView= root.findViewById(R.id.dayName)
                            dayName.text=MainActivity.getSelectedDayList().toString()
                            //change day
                            changeDay()
                        }
                        else if(distance>minumumSwipeXDistance){
                            //left swipe
                            //move on
                            MainActivity.backDay(root.context)
                            //show the name of the selected day
                            val dayName:TextView= root.findViewById(R.id.dayName)
                            dayName.text=MainActivity.getSelectedDayList().toString()
                            //change day
                            changeDay()
                        }
                    }
                }
                view?.performClick()
                return true
            }

        })
        //now the same for the recycler view
        //it was easer not to make a class
        //more swiping to switch days
        recyclerView?.setOnTouchListener(object:View.OnTouchListener{
            override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
                if(motionEvent?.action== MotionEvent.ACTION_DOWN){
                    x1 = motionEvent.getX()
                    y1 = motionEvent.getY()
                    return true
                }
                else if(motionEvent?.action==MotionEvent.ACTION_UP){
                    if(x1!=null&&y1!=null) {
                        val distance = x1!! - motionEvent.getX()
                        var distanceY = y1!! - motionEvent.getY()
                        if (distanceY < maximumSwipeYDistace && distanceY > -maximumSwipeYDistace) {
                            if (distance < -minumumSwipeXDistance) {
                                //right swipe
                                //move on
                                MainActivity.forwardDay(root.context)
                                //show the name of the selected day
                                val dayName: TextView = root.findViewById(R.id.dayName)
                                dayName.text = MainActivity.getSelectedDayList().toString()
                                //change day
                                changeDay()
                                return true
                            } else if (distance > minumumSwipeXDistance) {
                                //left swipe
                                //move on
                                MainActivity.backDay(root.context)
                                //show the name of the selected day
                                val dayName: TextView = root.findViewById(R.id.dayName)
                                dayName.text = MainActivity.getSelectedDayList().toString()
                                //change day
                                changeDay()
                                return true
                            }
                        }
                    }
                }
                view?.performClick()
                return false
            }

        })

        return root
    }

    private fun setup(root:View){

        recycleAdapter =
            context?.let { MyAdapter(it,selectedDate,activity as MainActivity?,this) }
        recyclerView?.setAdapter(recycleAdapter)

        val callback: ItemTouchHelper.Callback =
            ItemMoveCallback(recycleAdapter!!)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        //show the name of the selected day
        val dayName:TextView= root.findViewById(R.id.dayName)
        dayName.text=MainActivity.getSelectedDayList().toString()
    }


    //resets the recyler view adapter for whatever is in MainActivity.selectedDay
    private fun changeDay(){
        recycleAdapter!!.changeDay(MainActivity.getSelectedDayList())
    }

    override fun onResume() {
        super.onResume()
        context?.let { MainActivity.getSelectedDayList().setup(it) }
    }
}