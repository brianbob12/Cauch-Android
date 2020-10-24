package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.util.Log
import java.io.Serializable
import java.lang.Exception
import java.sql.Time
import java.sql.Date

class Task {
    //everything is private to avoid pointers leaving the object
    private var plannedTime: Time
    private var dueDate:Date?=null
    private var name:String=""
    private var active:Boolean =false
    private var descripton:String=""

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
            out+=tag.toString()
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

        return out
    }

    //makes task from string
    public fun fromString(str:String) {
        //remember backwards compatibility
        val stuff=str.split("\t")

        this.name=stuff[0]
        this.plannedTime= Time.valueOf(stuff[1])
        this.active=stuff[2]=="true"

        //tags
        val tagKeys: List<String> = stuff[3].split(",")
        //tag lookup
        for(tagKey in tagKeys){
            MainActivity.tagLookup.get(tagKey)?.let { this.tags.add(it) }
        }

        if(stuff[4]!="NULL"){
            this.dueDate= Date.valueOf(stuff[4])
        }
        try{
            this.descripton=stuff[5]
        }
        catch(e:Exception){
            Log.e("Tag Import Error",e.toString())
        }
    }
}