package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.content.Context
import android.util.Log
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

    constructor(date: Date){
        this.date =date
    }

    //days of week for int to string conversion
    private val daysOfWeek:ArrayList<String> = arrayListOf("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday")

    //tasks stuff
    //losts of pointers

    //sets up a liked list of tasks for itterating over all tasks
    public var tasks: ArrayList<Task> = arrayListOf<Task>()


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
        //else
        var output:String=""
        output+=daysOfWeek.get(this.getDayOfWeek()-1)
        output+=" "
        //add the day of month
        output+= cal1[Calendar.DAY_OF_MONTH]
        return output
    }

    public fun addTask(task: Task){
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
    }

    //this function is called during drag and drop when tasks are dragged over eachother
    public fun swapTasks(ia:Int,ib:Int){
        //swaps the position in the list as well as the times

        //swap times
        val timeA=tasks.get(ia).getPlannedTime()//note this is a clone of the time
        tasks.get(ia).setPlannedTime(tasks.get(ib).getPlannedTime())
        tasks.get(ib).setPlannedTime(timeA)

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
                var newTask: Task = Task("task_quick.xml")
                try {
                    newTask.fromString(taskInfo)
                    this.addTask(newTask)
                } catch (e: IndexOutOfBoundsException) {
                    Log.e("Task Error", e.toString())
                }
            }
        }
        loaded=true
    }
    //re orders a task to be placed correctly
    public fun reOrderTask(task: Task){
        //remove task
        tasks.remove(task)
        //readd task
        this.addTask(task)
    }
}