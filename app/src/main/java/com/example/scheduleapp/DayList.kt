package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.scheduleapp.Tasks.Task
import com.google.android.gms.analytics.HitBuilders
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.OutputStreamWriter
import java.sql.Date
import java.util.*


//stores a list of tasks for each day in order
class DayList{
    //it is very important that this is immutable
    public val date:Date
    public var loaded=false

    //this is true when the the daylist is today or later
    //this is false when the dayList is yesterday or earlier
    public var future=true

    constructor(date: Date){
        this.date =date

        //setup future(the variable)
        //check if it is today or later
        val cal1 = Calendar.getInstance()
        val cal2 = Calendar.getInstance()
        cal1.time = date//this dayList
        cal2.time = java.sql.Date(java.util.Date().time)//represents today
        val numberOfDays=(cal1.getTime().time - cal2.getTime().time) / (1000 * 60 * 60 * 24)//number of days between cal1 and cal2
        //bool
        future=numberOfDays>=0

    }

    //days of week for int to string conversion
    private val daysOfWeek:ArrayList<String> = arrayListOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")

    //tasks stuff
    //losts of pointers

    //sets up a liked list of tasks for itterating over all tasks
    public var tasks: ArrayList<Task> = arrayListOf<Task>()

    //do various setup stuff
    public fun setup(context: Context){
        //load day if need be
        if(!MainActivity.getSelectedDayList().loaded){
            MainActivity.getSelectedDayList().readDay(context)
        }
    }

    //gets an integer value of the day of the week for this day starting with 0 as a Sunday
    fun getDayOfWeek():Int{
        val cal: GregorianCalendar = GregorianCalendar();
        cal.setTime(date);
        // Getting the day of the week
        return(cal.get(Calendar.DAY_OF_WEEK))
    }

    override fun toString(): String {
        //check if day is today
        val cal1:Calendar = Calendar.getInstance()
        val cal2:Calendar = Calendar.getInstance()
        cal1.time=this.date
        cal2.time=java.sql.Date(java.util.Date().time)//represents today
        if(cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)){
            return "Today"
        }
        cal2.add(Calendar.DATE, 1)//now cal2 is tomorrow
        if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
        ) {
            return "Tomorrow"
        }
        cal2.add(Calendar.DATE, -2)//now cal2 is yesterday
        if (cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR) &&
            cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
        ) {
            return "Yesterday"
        }
        //else
        var output:String=""
        output+=daysOfWeek.get(this.getDayOfWeek()-1)
        output+=" "
        //add the day of month
        output+= cal1[Calendar.DAY_OF_MONTH]
        return output
    }

    public fun addTask(context: Context,task: Task){
        //deals with the order of tasks
        if(tasks.size==0) {
            tasks.add(task)
        }
        else{
            //place task in order
            for(i in 0..tasks.size-1){
                if(task.getPlannedTime() <tasks.get(i).getPlannedTime()){
                    //add task ahead of i
                    tasks.add(i,task)
                    return
                }
            }
            //if we get here the task goes on the end
            tasks.add(task)
        }
        if(future){
            //add to daysWithStuff
            MainActivity.persistentContainer.daysWithStuffAdd(context,date)
            //this function makes sure not to add the date if it is already in the list
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun removeTask(context: Context, task: Task){
        //cancell notification
        task.cancelNotification(context)

        //remove task
        tasks.remove(task)
        //check if now have no taks
        if(tasks.size==0) {
            if (future) {
                //add to daysWithStuff
                MainActivity.persistentContainer.daysWithStuffRemove(context, date)
                //this function makes sure not to add the date if it is already in the list
            }
        }
    }

    //this function is called during drag and drop when tasks are dragged over eachother
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun swapTasks(context:Context, ia:Int, ib:Int){
        //swaps the position in the list as well as the times

        //swap times
        val timeA=tasks.get(ia).getPlannedTime()//note this is a clone of the time
        tasks.get(ia).setPlannedTime(tasks.get(ib).getPlannedTime())
        tasks.get(ib).setPlannedTime(timeA)

        //now deal with notifications
        tasks.get(ia).cancelNotification(context)
        tasks.get(ib).cancelNotification(context)
        //shedule new notifications
        MainActivity.toSchedule.add(tasks.get(ia))
        MainActivity.toScheduleDays.add(this)
        MainActivity.toSchedule.add(tasks.get(ib))
        MainActivity.toScheduleDays.add(this)

        //swap positions in the list
        Collections.swap(tasks, ia, ib )
    }



    //saves the day to internal storage
    //this will override anthing already there
    public fun saveDay(context: Context){
        //setup content
        var content:String=""
        for(task in tasks){
            content+=task.toString()
            content+="\n"//one character
        }
        //getFile
        //var name:String="D"+this.date.toString()
        var name:String=this.date.toString()+".txt"
        val file = File(context.filesDir,name)

        file.createNewFile()//creates a new empty file if this file does not exsist
        try {
            val outputStreamWriter = OutputStreamWriter(context.openFileOutput(name,Context.MODE_PRIVATE))
            outputStreamWriter.write(content)
            outputStreamWriter.close()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: " + e.toString())
        }
    }

    //takes in the date.toString as the name
    public fun readDay(context:Context){

        var name:String=this.date.toString()+".txt"
        //getFile

        val file = File(context.filesDir,name)
        var out:String=""

        try{
            val fis:FileInputStream=context.openFileInput(name)

            var i:Int=0

            while (fis.read().also { i = it } !== -1) {
                out+=i.toChar()
            }
            fis.close()
        }
        catch(e:Exception){
            Log.e("Test",e.toString())
        }


        //implement read tasks
        //clear old task
        tasks= arrayListOf()
        for(taskInfo in out.split("\n")){
            if(taskInfo.length>0) {
                var newTask: Task =
                    Task("task_quick.xml")
                try {
                    newTask.fromString(taskInfo)
                    this.addTask(context,newTask)
                } catch (e: IndexOutOfBoundsException) {
                    Log.e("Task Error", e.toString())
                }
            }
        }
        loaded=true
    }
    //re orders a task to be placed correctly
    public fun reOrderTask(context:Context,task: Task){
        //remove task
        tasks.remove(task)
        //readd task
        this.addTask(context,task)
    }

    //finds a suitable time during the day to schedule a new task
    /*
    To begin with the the new time will be the earliest possible that meets these conditions,
    1. Must be after 8am or after the first task
    2. Must be a half hour from any other task

    If no time exists that matches tease conditions the time will be 8am.

    TODO Change this system in future
    Find out what time the user usually starts task on each day of the week.
    Find out what time the user usually ends tasks on each day of the week.
    Find out how long tasks take(through survey) for each tag.
    Allow for a day to be full.
     */
    public fun findNewTime():java.sql.Time{
        var outTime:java.sql.Time = java.sql.Time(8,0,0)//depreciated
        if(tasks.size==0){
            return outTime
        }
        if(tasks.get(0).getPlannedTime().after(outTime)){//the earliest time is after 8 am
            //check if there is a half hour difference
            if(tasks.get(0).getPlannedTime().time-1800000>outTime.time){
                return outTime
            }
        }
        //remember tasks are in order from earliest to latest
        for(i in 0..tasks.size-2){//this includes the first task but excludes the last task
            outTime=java.sql.Time(tasks.get(i).getPlannedTime().time+1800000)
            //a half hour after the selected task
            if(outTime.time>=86400000){
                //the time is no longer today
                break //leads to returning eight am
            }
            //else
            //check if it is a half hour before the next task
            if(tasks.get(i+1).getPlannedTime().time-1800000>outTime.time){
                //we are good to go
                return outTime
            }
            //else continue
        }
        return java.sql.Time(8,0,0)
    }

    //changes the day of a task to another day
    //the task in question bust be a member of this DayList
    //returns true if successful
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun changeDayOfTask(context:Context, task: Task, targetDate:Calendar):Boolean{
        if(!(task in this.tasks)){
            return false
        }
        //make new task
        val newTask=task.copy()//shallow copy
        //copy over tags
        newTask.tags=task.tags//pointer is being passed here

        //remove old task
        task.cancelNotification(context)
        this.tasks.remove(task)

        //export day
        this.saveDay(context)

        //select target day
        MainActivity.selectedDay=java.sql.Date(targetDate.timeInMillis)
        //load day if need be
        if(!MainActivity.getSelectedDayList().loaded){
            MainActivity.getSelectedDayList().readDay(context)
        }

        //reset the time of the task
        newTask.setPlannedTime(MainActivity.getSelectedDayList().findNewTime())

        //add new task to target day
        MainActivity.getSelectedDayList().addTask(context,newTask)
        //export again
        MainActivity.getSelectedDayList().saveDay(context)

        //add notification for new task
        MainActivity.toSchedule.push(newTask)
        MainActivity.toScheduleDays.push(MainActivity.getSelectedDayList())

        //reselect original date
        MainActivity.selectedDay=this.date

        //google analytics
        MainActivity.mTracker?.send(
            HitBuilders.EventBuilder()
            .setCategory("Action")
            .setAction("ChangeTaskDate")
            .build())

        return true
    }
}