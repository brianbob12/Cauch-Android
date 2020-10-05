package com.example.scheduleapp

import java.io.Serializable
import java.sql.Time
import java.sql.Date

class Task :Serializable{
    //everything is private to avoid pointers leaving the object
    private var color:Int =0
    private var plannedTime: Time
    private var plannedDate:Date?=null
    private var name:String=""
    private var active:Boolean =false

    //holds a tag for the task
    private var myTag:TaskTag?=null

    //getters and setters becuase I could not figure out the kotlin for getters and setters
    public fun getName():String {
        return name
    }
    public fun setName(value:String){
        name=value
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
    public fun getPlannedDate():Date?{
        return plannedDate?.clone() as Date?
    }
    //setPlannedDate creates a clone of the time provided
    public fun setPlannedDate(value:Date){
        plannedDate=value.clone() as Date?
    }

    public fun setMyTag(tag:TaskTag){
        myTag=tag
    }
    public fun getMyTag():TaskTag?{
        return myTag
    }

    constructor(name:String){
        this.name=name
        this.plannedTime=Time(0,0,0)
    }
}