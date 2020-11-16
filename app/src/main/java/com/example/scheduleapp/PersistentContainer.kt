package com.example.scheduleapp

import android.content.Context
import android.util.Log
import com.example.scheduleapp.RepeatingTasks.RepeatingTask
import com.example.scheduleapp.RepeatingTasks.RepeatingTaskInvalidStringException
import com.example.scheduleapp.Tasks.Task
import java.io.*
import java.sql.Date
import java.util.*


/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

//a class to hold persistent information that is ot best suited anywhere else
//currently includes:
// - daysWithStuff
// - repeatingTasks
class PersistentContainer(){
    //a list of days that have tasks in them
    //when a user is prompted to postpone the tasks that date is removed
    //note java.util.date
    public var daysWithStuff: LinkedList<Date> = LinkedList<Date>()//empty

    //a list of all of the repeating taks
    public var repeatingTasks: LinkedList<RepeatingTask> = LinkedList<RepeatingTask>()//empy

    //adds to days with stuff and saves if need be
    public fun daysWithStuffAdd(context: Context,x:Date){
        //I have to check wheater x is in the list
        //we do this because we dont' want to get confused mit pointers
        var inList=false

        //this for loop is fast becuause it is a linked list
        for(day in daysWithStuff){
            if(day.equals(x)){
                inList=true
                break
            }
        }

        if(!inList){
            //if x not in daysWithStuff
            daysWithStuff.add(x)
            //then save
            save(context)
        }
    }
    //removes from days with stuff and saves if need be
    public fun daysWithStuffRemove(context: Context,x:Date){
        //I have to check wheater x is in the list
        //we do this because we dont' want to get confused mit pointers
        var inList=false

        //this for loop is fast becuause it is a linked list
        for(day in daysWithStuff){
            if(day.equals(x)){
                inList=true
                break
            }
        }

        if(inList){
            //if x not in daysWithStuff
            daysWithStuff.remove(x)
            //then save
            save(context)
        }
    }

    //download the data in the Persistent Container to a file
    public fun save(context: Context){
        //export daysWithStuf
        var file: FileOutputStream? = null
        try {
            file = FileOutputStream(File(context.filesDir, "daysWithStuff.datelist"))
        } catch (e: Exception){
            //in case of failure opening file return for now but later message box error
            Log.e("ERROR",e.toString())
            return
        }
        val outStream: ObjectOutputStream = ObjectOutputStream(file)

        try {
            outStream.writeObject(this.daysWithStuff)
            //type: LinkedList<java.util.Date>
        }
        catch (e:InvalidClassException){
            Log.e("ERROR",e.toString())
        }

        outStream.close()
        file.close()

        //export repeatingTasks to txt file
        //setup content
        var content:String=""
        for(task in repeatingTasks){
            content+=task.toString()
            content+="\n"//one character
        }
        //getFile
        //var name:String="D"+this.date.toString()
        var name:String="persistentContainerRepeatingTasks.txt"
        val repeatFile = File(context.filesDir,name)

        repeatFile.createNewFile()//creates a new empty file if this file does not exsist
        try {
            val outputStreamWriter = OutputStreamWriter(context.openFileOutput(name,Context.MODE_PRIVATE))
            outputStreamWriter.write(content)
            outputStreamWriter.close()
        } catch (e: IOException) {
            //non-fatal
            Log.e("Exception", "File write failed: " + e.toString())
        }
    }

    //retrieve the data for tje Persistent Container from a file

    public fun load(context: Context){
        //load daysWithStuff
        try{
            var file: FileInputStream? = null
            //catch if no file exists
            try {
                file = FileInputStream(File(context.filesDir, "daysWithStuff.datelist"))
            } catch (e: Exception){
                //in case of failure return empty now but later create file
                Log.e("SETTING UP","Setting up files(dayswithstuff.daylist) now")
                val newFile = File(context.filesDir, "daysWithStuff.datelist")
                newFile.createNewFile()
                return
            }

            var inStream: ObjectInputStream = ObjectInputStream(file)
            var item: LinkedList<Date>? = inStream.readObject() as LinkedList<Date>?//java.util.date
            //it is a valid list
            if (item != null) {
                this.daysWithStuff=item
            }


            inStream.close()
            file.close()
        }
        catch (e: EOFException){
            Log.e("ERROR",e.toString())
        }
        catch (e: InvalidClassException) {
            Log.e("ERROR",e.toString())
        }

        //load repeating tasks

        //filename
        var name:String="persistentContainerRepeatingTasks.txt"
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
        //note maintains the same pointer
        repeatingTasks.clear()
        for(taskInfo in out.split("\n")){
            if(taskInfo.length>0) {
                var newTask: RepeatingTask =
                    RepeatingTask("IMPORTED",false,java.sql.Date(0,0,0),false)
                try {
                    newTask.fromString(taskInfo)
                } catch (e: RepeatingTaskInvalidStringException) {
                    //non-fatal
                    Log.e("Task Error", e.toString())
                }
                finally {
                    //only runs if the fromString was successful
                    repeatingTasks.add(newTask)
                }
            }
        }
    }

    //finds days that had tasks that were never completed. Offers to move them to today
    public fun moveUncompletedTasks(context:Context){
        //find today
        var calToday:Calendar = Calendar.getInstance()
        calToday.time=java.util.Date()

    }

}