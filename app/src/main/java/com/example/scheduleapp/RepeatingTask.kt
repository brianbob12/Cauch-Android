package com.example.scheduleapp

import android.app.job.JobScheduler
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.lang.Exception
import java.lang.NumberFormatException
import java.sql.Date
import java.sql.Time

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */


class RepeatingTask {
    //everything is private to avoid pointers leaving the object
    private var fixedTime:Boolean//this is false when the user wants the task to be fit into thier day whenever is best
    private var plannedTime: Time? = null
    private var startDate:Date
    private var name:String=""
    private var descripton:String=""
    //this variable is true if the task repeats every other week or day
    //if this is false, task repeats every week or day
    private var everyOther:Boolean=false
        get() {return this.everyOther}

    //weekday variables
    //if a weekday variable is true the task will repeat on that week
    public var sunday=false
    public var monday=false
    public var tuesday=false
    public var wednesday=false
    public var thursday=false
    public var friday=false
    public var saturday=false

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
    //returns a clone of the time
    public fun getPlannedTime(): Time? {
        return plannedTime?.clone() as Time
    }
    //setPlannedTime creates a clone of the time provided
    public fun setPlannedTime(value: Time?){
        plannedTime= value?.clone() as Time?
    }
    public fun getStartDate():Date{
        return startDate.clone() as Date
    }
    //setPlannedDate creates a clone of the time provided
    public fun setStartDate(value:Date){
        startDate=value.clone() as Date
    }

    constructor(name:String,fixedTime:Boolean,startDate:Date,everyOther:Boolean){
        this.name=name
        this.startDate=startDate
        this.everyOther=everyOther
        this.fixedTime=fixedTime
        if(!fixedTime) {
            this.plannedTime = Time(0, 0, 0)
        }
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
        out+=this.fixedTime.toString()
        out+="\t"
        //3
        for(tag in this.tags){
            out+=tag.id.toString()
            out+=","
        }
        out+="\t"
        //4
        if(this.startDate==null){
            out+="NULL"
        }
        else {
            out += this.startDate.toString()
        }
        out+="\t"
        //5
        out+=descripton

        return out
    }

    //makes task from string
    //TODO import schedule id
    public fun fromString(str:String) {
        //remember backwards compatibility
        val stuff=str.split("\t")

        this.name=stuff[0]
        this.plannedTime= Time.valueOf(stuff[1])
        this.fixedTime=stuff[2]=="true"

        //tags
        val rawTagKeys: List<String> = stuff[3].split(",")
        //tag lookup
        for(tagKey in rawTagKeys){
            try {
                MainActivity.tagLookup.get(tagKey.toInt())?.let { this.tags.add(it) }
            }
            catch(e: NumberFormatException){
                //not valid int
                Log.e("BAD TAG ID",tagKey)
                continue
            }
        }

        if(stuff[4]!="NULL"){
            this.startDate= Date.valueOf(stuff[4])
        }
        try{
            this.descripton=stuff[5]
        }
        catch(e: Exception){
            Log.e("Tag Import Error",e.toString())
        }
    }

    //cancels the notification attributed to this task
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    public fun cancelNotification(context: Context){
        // cancel notification
        val jobScheduler: JobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        this.scheduleID?.let { jobScheduler.cancel(it) }
    }

    public fun copy():RepeatingTask{
        var out=RepeatingTask(name,fixedTime,startDate,everyOther)
        out.setDescription(descripton)
        plannedTime?.let { out.setPlannedTime(it) }
        out.setStartDate(startDate)
        return out
    }
}