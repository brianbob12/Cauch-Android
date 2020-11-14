package com.example.scheduleapp.Tasks

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.app.job.JobScheduler
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.RepeatingTasks.RepeatingTaskInvalidStringException
import com.example.scheduleapp.TaskTag
import java.lang.Exception
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException
import java.sql.Time
import java.sql.Date

class Task {
    //everything is private to avoid pointers leaving the object
    private var plannedTime: Time
    private var dueDate:Date?=null
    private var name:String=""
    private var active:Boolean =false
    private var descripton:String=""
    public var scheduleID:Int?=null

    //holds a tag for the task
    public var tags:ArrayList<TaskTag> = arrayListOf()

    //getters and setters becuase I could not figure out the kotlin for getters and setters
    public fun getName():String {
        return name
    }
    public fun setName(value:String){
        name=value
    }
    public fun getDescription():String {
        return descripton
    }
    public fun setDescription(value:String){
        descripton=value
    }
    public fun getActive():Boolean {
        return active
    }
    public fun setActive(value:Boolean){
        active=value
    }
    //returns a clone of the time
    public fun getPlannedTime():Time{
        return plannedTime.clone() as Time
    }
    //setPlannedTime creates a clone of the time provided
    public fun setPlannedTime(value:Time){
        plannedTime= (value.clone() as Time?)!!
    }
    public fun getdueDate():Date?{
        return dueDate?.clone() as Date?
    }
    //setPlannedDate creates a clone of the time provided
    public fun setdueDate(value:Date){
        dueDate=value.clone() as Date?
    }


    constructor(name:String){
        this.name=name
        this.plannedTime=Time(0,0,0)
    }

    //makes task string for saving to files

    //TODO add schedule id
    public override fun toString():String{
        var out:String=""

        //0
        out+=this.name
        out+="\t"
        //1
        out+=this.plannedTime.toString()
        out+="\t"
        //2
        out+=this.active.toString()
        out+="\t"
        //3
        for(tag in this.tags){
            out+=tag.id.toString()
            out+=","
        }
        out+="\t"
        //4
        if(this.dueDate==null){
            out+="NULL"
        }
        else {
            out += this.dueDate.toString()
        }
        out+="\t"
        //5
       out+=descripton
        out+="\t"
        //6
        if(scheduleID==null){
            out+="NULL"
        }
        else {
            out += scheduleID.toString()
        }
        return out
    }

    //makes task from string
    @Throws(TaskInvalidStringException::class)
    public fun fromString(str:String) {
        //remember backwards compatibility
        val stuff=str.split("\t")
        try {
            this.name = stuff[0]
            try {
                this.plannedTime = Time.valueOf(stuff[1])
            } catch (e: Exception) {
                //fatal
                throw RepeatingTaskInvalidStringException(
                    RepeatingTaskInvalidStringException.exceptionCause.INVALIDDATATYPE, str
                )
            }
            this.active = stuff[2] == "true"

            //tags
            val rawTagKeys: List<String> = stuff[3].split(",")
            //tag lookup
            for (tagKey in rawTagKeys) {
                try {
                    MainActivity.tagLookup.get(tagKey.toInt())?.let { this.tags.add(it) }
                } catch (e: NumberFormatException) {
                    //not valid int
                    Log.e("BAD TAG ID", tagKey)
                    //not fatal
                    continue
                }
            }
        }
        catch (e:IndexOutOfBoundsException){
            //fatal
            throw RepeatingTaskInvalidStringException(
                RepeatingTaskInvalidStringException.exceptionCause.INDEXOUTOFBOUNDS,str)
        }
        //lots of nice error handeling here
        try {
            if (stuff[4] != "NULL") {
                this.dueDate = Date.valueOf(stuff[4])
            }
            this.descripton = stuff[5]
            if(stuff[6]=="NULL"){
                this.scheduleID=null
            }
            else{
                this.scheduleID=stuff[6] as? Int //avoids exceptions
            }
        }
        catch(e:Exception){
            //non-fatal
            Log.e("Tag Import Error",e.toString())
        }
    }

    //cancels the notification attributed to this task
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun cancelNotification(context:Context){
        // cancel notification
        val jobScheduler:JobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        this.scheduleID?.let { jobScheduler.cancel(it) }
    }

    public fun copy(): Task {
        var out= Task(name)
        out.setDescription(descripton)
        out.setActive(active)
        out.setPlannedTime(plannedTime)
        dueDate?.let { out.setdueDate(it) }
        return out
    }
}