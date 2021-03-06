package com.example.scheduleapp.ui.home

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.scheduleapp.*
import com.example.scheduleapp.Tasks.Task
import com.google.android.gms.analytics.HitBuilders
import java.util.*


class MyAdapter(context: Context, data: DayList,activity: MainActivity?,homeFragment: HomeFragment) : RecyclerView.Adapter<MyAdapter.MyViewHolder>(),
    ItemMoveCallback.ItemTouchHelperContract  {

    private var data:DayList
    private var myTasks:ArrayList<Task>//this is a copy of data.tasks
    private val context:Context
    private val activity:MainActivity?
    private val homeFragment:HomeFragment

    init {
        this.data = data
        this.myTasks= data.tasks.clone() as ArrayList<Task>
        this.context=context
        this.activity=activity
        this.homeFragment=homeFragment
    }

    override fun getItemViewType(position: Int): Int {
        return R.layout.task_card_row
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.task_card_row, parent, false)

        val myViewHolder=MyViewHolder(itemView)

        return myViewHolder
    }



    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //resize stuff
        holder.tagArea.requestLayout()

        val task=myTasks.get(position)
        //deal with text
        holder.title.setText(task.getName())
        val plannedTime=task.getPlannedTime()
        holder.timeText.setText(plannedTime.toString().subSequence(0 ,5))
        holder.checkBox.isChecked=false
        //deal with task

        holder.title.setTextColor(Color.BLACK)
        //check if tag is due tomorrow or sooner
        if(myTasks.get(position).getdueDate()!=null) {//checks that there is a due date
            val cal1 = Calendar.getInstance()
            val cal2 = Calendar.getInstance()
            cal1.time = myTasks.get(position).getdueDate()
            cal2.time = java.sql.Date(java.util.Date().time)//represents today
            val numberOfDays=(cal1.getTime().time - cal2.getTime().time) / (1000 * 60 * 60 * 24)//number of days between cal1 and cal2
            if(numberOfDays<1){
                //make text orange
                holder.title.setTextColor(Color.argb(255,255,125,0))
            }
            if(numberOfDays<0){
                //make text red
                holder.title.setTextColor(Color.RED)
            }
        }

        if(task.tags.size==0){
            //make invisible
            holder.tagArea.visibility=View.INVISIBLE
        }
        else{

            //set name for task
            //render tag highlight
            holder.tagArea.visibility=View.VISIBLE

            //change the color of tag highlight to the tag's color
            val background= context.getDrawable(R.drawable.tag_highlight_shape)
            //use the color of the first tag
            background?.setTint(task.tags.get(0).getColor())

            holder.tagArea.background=background

        }
        //set on click listener
        holder.checkBox.setOnClickListener {
            //remove task
            data.tasks.get(position).cancelNotification(context)
            data.tasks.removeAt(position)
            this.myTasks= data.tasks.clone() as ArrayList<Task>
            notifyItemRemoved(position)
            //update all after to reset onclick listeners
            notifyItemRangeChanged(position,myTasks.size)
            //export day
            data.saveDay(context)

            //google analytics
            MainActivity.mTracker?.send(
                HitBuilders.EventBuilder()
                .setCategory("Action")
                .setAction("CheckOffTask")
                .build())
        }
        //setup more popup menu
        //setup popup menu
        val popup = PopupMenu(context, holder.moreButton)
        popup.menu.add("Change Day")
        popup.menu.add("Edit")
        popup.menu.add("Delete")
        popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(menuItem: MenuItem?): Boolean {
                val name: String = menuItem!!.toString()//gets selected option
                //do stuff
                if(name=="Delete"){
                    //remove task
                    data.removeTask(context,data.tasks.get(position))
                    myTasks= data.tasks.clone() as ArrayList<Task>
                    notifyItemRemoved(position)
                    //update all after to reset onclick listeners
                    notifyItemRangeChanged(position,myTasks.size)
                    //export day
                    data.saveDay(context)

                    //google analytics
                    MainActivity.mTracker?.send(HitBuilders.EventBuilder()
                        .setCategory("Action")
                        .setAction("DeleteTask")
                        .build())
                }
                else if(name=="Edit"){
                    //select task
                    MainActivity.selectedTask=task
                    //launch the AddNewTask activity
                    activity?.startAddNewTaskFragment()
                }
                else if(name=="Change Day"){
                    //show day selector
                    //Create a View object yourself through inflater
                    val inflater = LayoutInflater.from(context)
                    val popupView: View = inflater.inflate(R.layout.day_select_popup,null)

                    //Specify the length and width through constants
                    val width = LinearLayout.LayoutParams.MATCH_PARENT
                    val height = LinearLayout.LayoutParams.MATCH_PARENT

                    //Make Inactive Items Outside Of PopupWindow
                    val focusable = true

                    //Create a window with our parameters
                    val popupWindow = PopupWindow(popupView, width, height, focusable)

                    //Set the location of the window on the screen
                    popupWindow.showAtLocation(holder.rowView, Gravity.CENTER, 0, 0)

                    //Add the information

                    //deal with date
                    //defult postpone to next day
                    var targetDate: Calendar= Calendar.getInstance()
                    targetDate.time=data.date//represents selected day
                    targetDate.add(Calendar.DATE,1)//represents selected day + 1

                    val dateBox: DatePicker = popupView.findViewById(R.id.changeDayDatePick)
                    dateBox.updateDate(targetDate.get(Calendar.YEAR),targetDate.get(Calendar.MONTH),targetDate.get(Calendar.DAY_OF_MONTH))

                    //Handler for clicking on the inactive zone of the window
                    popupView.setOnTouchListener { v, event -> //Close the window when clicked
                        popupWindow.dismiss()
                        true
                    }

                    //set onsubmit confirmation
                    val submitButton:Button =popupView.findViewById(R.id.changeDaySubmit)
                    submitButton.setOnClickListener {
                        //update selected day
                        targetDate.time=java.sql.Date(dateBox.year-1900,dateBox.month,dateBox.dayOfMonth)
                        var targetLabel:String= DayList(java.sql.Date(targetDate.timeInMillis)).toString()
                        //an alert box confirming the details

                        //this builder is used to setup the dialogue box
                        val builder: AlertDialog.Builder= AlertDialog.Builder(context)
                            .setMessage(
                                Html.fromHtml(
                                    "Would you like to change your task's planned date to <b>"
                                            + targetLabel + "</b> ?"))
                            .setCancelable(true)//allows calculation
                            //yes button postpones task
                            .setPositiveButton("yes"
                            ) { _, _ ->
                                //push task
                                MainActivity.getSelectedDayList().changeDayOfTask(
                                    context,data.tasks.get(position),targetDate)

                                //refresh MyTasks
                                myTasks= data.tasks.clone() as ArrayList<Task>


                                notifyItemRemoved(position)
                                //update all after to reset onclick listeners
                                notifyItemRangeChanged(position,myTasks.size)

                                //close popup
                                popupWindow.dismiss()
                            }
                            //no button does nothing
                            .setNegativeButton("no"
                            ) { di, _ -> //this closes the message box
                                di.cancel()
                            }


                        builder.create().show()
                    }
                }
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
            popUpClass.showPopupWindow(holder.rowView,myTasks.get(position),activity)
        }
    }

    override fun getItemCount(): Int {
        return myTasks.size
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onRowMoved(fromPosition: Int, toPosition: Int) {
        //cancel swipe to switch days
        homeFragment.x1 = null // this will stop any swiping
        homeFragment.y1 = null

        if (fromPosition < toPosition) {
            notifyItemChanged(fromPosition)
            for (i in fromPosition until toPosition) {
                data.swapTasks(context, i, i + 1)
                notifyItemChanged(i+1)
            }
        } else {
            notifyItemChanged(fromPosition)
            for (i in fromPosition downTo toPosition + 1) {
                data.swapTasks(context,i, i - 1)
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
            tagArea=itemView.findViewById(R.id.tagHighlight)
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