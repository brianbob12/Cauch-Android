package com.example.scheduleapp.ui.home

import android.app.AlertDialog
import android.os.Bundle
import android.text.Html
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
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        //val textView: TextView = root.findViewById(R.id.text_home)
        //homeViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

        //show tasks
        val taskListLayout: LinearLayout? = root.findViewById(R.id.TaskListLayout)
        for (task in MainActivity.tasks){//iterate over all task
            //create a task view and add it to the list
            val taskView:TaskInList = TaskInList(root.context,task)
            taskListLayout?.addView(taskView)

            //deal with task cancelation
            /*
            taskView.setLongClickable(true)
            taskView.setOnLongClickListener {
                //an alert box confirming the delete
                //this builder is used to setup the dialogue box
                val builder: AlertDialog.Builder= AlertDialog.Builder(root.context)
                    .setMessage(
                        Html.fromHtml(
                        "Are you sure you want to delete the alarm: <b>"
                                + task.getName() + "</b> ?"))
                    .setCancelable(false)//prevents calculation
                    //yes button deletes alarm
                    .setPositiveButton("yes"
                    ) { _, _ -> //delete alarm here

                        //remove from view
                        taskListLayout?.removeView(taskView)
                        MainActivity.tasks.remove(task)

                        //TODO delete from storage

                    }
                    //no button does nothing
                    .setNegativeButton("no"
                    ) { di, _ -> //this closes the message box
                        di.cancel()
                    }
                builder.create().show()
                true
                true
            }
        */
        }
        val addTask: FloatingActionButton =root.findViewById(R.id.addTaskButton)
        addTask.setOnClickListener {
            (activity as MainActivity?)?.startAddNewTaskFragment()

        }

        return root
    }

}