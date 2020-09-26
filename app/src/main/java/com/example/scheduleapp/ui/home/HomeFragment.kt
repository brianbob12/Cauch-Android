package com.example.scheduleapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.R
import com.example.scheduleapp.Task
import com.example.scheduleapp.TaskInList
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel::class.java)
        var root = inflater.inflate(R.layout.fragment_home, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
        //homeViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

        val taskListLayout: LinearLayout? = root.findViewById(R.id.TaskListLayout)
        for (i in 0..10){
            var myTIN: TaskInList? = container?.getContext()?.let { TaskInList(it, Task(i.toString())) }
            if (myTIN != null) {
                taskListLayout?.addView(myTIN)
                myTIN.setLongClickable(true)
                myTIN.setOnLongClickListener {
                    true
                }
            }

        }
        val addTask: FloatingActionButton =root.findViewById(R.id.addTaskButton)
        addTask.setOnClickListener {
            //(activity as MainActivity?)?.startAddNewTaskFragment()
            
        }

        return root
    }

}