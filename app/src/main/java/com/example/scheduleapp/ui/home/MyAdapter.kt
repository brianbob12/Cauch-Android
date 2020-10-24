package com.example.scheduleapp.ui.home

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.shapes.Shape
import android.os.Build
import android.util.Log
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.*
import kotlinx.android.synthetic.main.activity_add_new_task.*
import org.w3c.dom.Text
import java.util.*


class MyAdapter(context: Context, data: DayList) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(),
    ItemMoveCallback.ItemTouchHelperContract  {

    private var data:DayList
    private var myTasks:ArrayList<Task>//this is a copy of data.tasks
    private val context:Context

    init {
        this.data = data
        this.myTasks= data.tasks.clone() as ArrayList<Task>
        this.context=context
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.task_card_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.task_card_row, parent, false)
        return MyViewHolder(itemView)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val task=myTasks.get(position)
        //deal with text
        holder.title.setText(task.getName())
        val plannedTime=task.getPlannedTime()
        holder.timeText.setText(plannedTime.toString().subSequence(0 ,5))
        holder.checkBox.isChecked=false
        //deal with task
        if(task.tags.size==0){
            //make invisible
            holder.tagArea.visibility=View.INVISIBLE
        }
        else{
            //set name for task
            //render task
            holder.tagArea.visibility=View.VISIBLE
            val newTag = TagView(holder.tagArea.context,task.tags.get(0))
            holder.tagArea.removeAllViews()
            holder.tagArea.addView(newTag)
        }
        //set on click listener
        holder.checkBox.setOnClickListener {
            //remove task
            data.tasks.removeAt(position)
            this.myTasks= data.tasks.clone() as ArrayList<Task>
            notifyItemRemoved(position)
            //update all after to reset onclick listeners
            notifyItemRangeChanged(position,myTasks.size)
            //export day
            data.saveDay(context)
        }
        //setup more popup menu
        //setup popup menu
        val popup = PopupMenu(context, holder.moreButton)
        popup.menu.add("Postpone")
        popup.menu.add("Edit")
        popup.menu.add("Delete")
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                val name: String = menuItem!!.toString()//gets selected option
                //TODO do stuff
                return true
            }
        })
        holder.moreButton.setOnClickListener {
            //open popup menu
            //inflate menu
            val inflater: MenuInflater = popup.getMenuInflater()
            inflater.inflate(R.menu.tag_select_menu, popup.getMenu())
            popup.show()
        }

        //setup quickview
        holder.rowView.setOnClickListener {
            //quickview
            //inflate menu
            val popUpClass = Quickview()
            popUpClass.showPopupWindow(holder.rowView,myTasks.get(position))
        }
    }

    override fun getItemCount(): Int {
        return myTasks.size
    }

    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            notifyItemChanged(fromPosition)
            for (i in fromPosition until toPosition) {
                data.swapTasks( i, i + 1)
                notifyItemChanged(i+1)
            }
        } else {
            notifyItemChanged(fromPosition)
            for (i in fromPosition downTo toPosition + 1) {
                data.swapTasks(i, i - 1)
                notifyItemChanged(i-1)
            }
        }
        //reset myTasks
        this.myTasks= data.tasks.clone() as ArrayList<Task>
        notifyItemMoved(fromPosition, toPosition)
        //save
        data.saveDay(context)
    }

    override fun onRowSelected(myViewHolder: MyViewHolder?) {
        myViewHolder?.rowView?.setBackgroundResource(R.drawable.card_shape_dark)
    }

    override fun onRowClear(myViewHolder: MyViewHolder?) {
        myViewHolder?.rowView?.setBackgroundResource(R.drawable.card_shape_light)
    }

    //viewholder for the list
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView
        var rowView: View
        val timeText:TextView
        val tagArea:LinearLayout
        val checkBox:CheckBox
        val moreButton: ImageButton

        init {
            rowView = itemView
            title = itemView.findViewById(R.id.mainTitle)
            timeText=itemView.findViewById(R.id.timeText)
            tagArea=itemView.findViewById(R.id.tagArea)
            checkBox=itemView.findViewById((R.id.checkBox))
            moreButton=itemView.findViewById(R.id.moreButton)
        }
    }

    public fun reCloneTasks(){
        this.myTasks= data.tasks.clone() as ArrayList<Task>
    }

    //removes all items
    fun clear(){
        val size=myTasks.size
        //Log.e("TESTING",myTasks.toString())
        myTasks.clear()
        notifyItemRangeRemoved(0,size)
    }

    //changes day to new DayList
    fun changeDay(new:DayList){
        this.clear()
        data=new
        this.myTasks= data.tasks.clone() as ArrayList<Task>
        notifyItemRangeInserted(0,myTasks.size)
    }
}