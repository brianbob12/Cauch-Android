package com.example.scheduleapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.MyAdapter
import com.example.scheduleapp.R


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    //stuff to do with the RecyclerView
    private var recyclerView:RecyclerView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        Log.e("Testing","T-1")
        val root:View = inflater.inflate(R.layout.fragment_home, container, false)
        // Add the following lines to create RecyclerView
        recyclerView = root.findViewById<RecyclerView>(R.id.ListForTasks)
        recyclerView?.setHasFixedSize(true)
        recyclerView?.setLayoutManager(LinearLayoutManager(root.context))
        recyclerView?.setAdapter(MyAdapter(1234))
        Log.e("Testing","T-2")
        return root
    }
    /*
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.e("Tesing","creating view")
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val view:View = inflater.inflate(R.layout.fragment_home, container, false)
        Log.e("Tesing","creating view2")
        //deal with RecylerView wich is used for the drag and dropable tasks
        // Add the following lines to create RecyclerView
        val recyclerView:RecyclerView = view.findViewById(R.id.ListForTasks);
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(view.context))
        recyclerView.setAdapter(MyAdapter(1234))

        /*
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
        */
        Log.e("Tesing","ending creating view")
        return view
    }
    */
}