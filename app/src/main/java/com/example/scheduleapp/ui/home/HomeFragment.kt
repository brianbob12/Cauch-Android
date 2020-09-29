package com.example.scheduleapp.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.ItemMoveCallback
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.MyAdapter
import com.example.scheduleapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


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
        val recycleAdapter:MyAdapter= MyAdapter(MainActivity.tasks)
        recyclerView?.setAdapter(recycleAdapter)

        val callback: ItemTouchHelper.Callback = ItemMoveCallback(recycleAdapter)
        val touchHelper = ItemTouchHelper(callback)
        touchHelper.attachToRecyclerView(recyclerView)

        //deal with plus button
        val addTask: FloatingActionButton =root.findViewById(R.id.addTaskButton)
        addTask.setOnClickListener {
            (activity as MainActivity?)?.startAddNewTaskFragment()

        }

        Log.e("Testing","T-2")
        return root
    }


}