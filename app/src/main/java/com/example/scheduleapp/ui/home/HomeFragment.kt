package com.example.scheduleapp.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.DayList
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.R
import com.example.scheduleapp.TaskTag
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    //stuff to do with the RecyclerView
    private var recyclerView:RecyclerView? = null

    //the selected day
    private var selectedDate:DayList = MainActivity.todayDayList()



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
        val recycleAdapter: MyAdapter =
            MyAdapter(selectedDate)
        recyclerView?.setAdapter(recycleAdapter)

        val callback: ItemTouchHelper.Callback =
            ItemMoveCallback(recycleAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        //deal with plus button
        val addTask: FloatingActionButton =root.findViewById(R.id.addTaskButton)
        addTask.setOnClickListener {
            (activity as MainActivity?)?.startAddNewTaskFragment()

        }



        return root
    }


}