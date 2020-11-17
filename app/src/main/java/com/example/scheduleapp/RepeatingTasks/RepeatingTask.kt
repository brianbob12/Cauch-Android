package com.example.scheduleapp.RepeatingTasks

import android.util.Log
import com.example.scheduleapp.MainActivity
import com.example.scheduleapp.TaskTag
import java.lang.IndexOutOfBoundsException
import java.lang.NumberFormatException
import java.sql.Date
import java.sql.Time
import java.util.*
import kotlin.collections.ArrayList

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

//holds methods and data for a repeating task

class RepeatingTask {
    //everything is private to avoid pointers leaving the object
    public var fixedTime:Boolean//this is false when the user wants the task to be fit into thier day whenever is best
    private var plannedTime: Time? = null
    private var startDate:Date
    private var name:String=""
    private var descripton:String=""
    //this variable is true if the task repeats every other week or day
    //if this is false, task repeats every week or day
    public var everyOther:Boolean=false
    //a string describing the repetition of this task
    //does not include every or every other
    public var repeatsSettingString:String =""

    //weekday variables
    //if a weekday variable is true the task will repeat on that week
    public var sunday=false
    public var monday=false
    public var tuesday=false
    public var wednesday=false
    public var thursday=false
    public var friday=false
    public var saturday=false


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
        return plannedTime?.clone() as Time?
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
    
    //takes in a string describing the repeats setting and sets up object variables accordingly
    public fun setRepeatsSetting(repeatsSettingString:String){
        this.repeatsSettingString=repeatsSettingString
        //set all days to false
        this.sunday=false
        this.monday=false
        this.tuesday=false
        this.thursday=false
        this.friday=false
        this.saturday=false
        if(repeatsSettingString=="Sunday"){
            this.sunday=true
        }
        else if(repeatsSettingString=="Monday"){
            this.monday=true
        }
        else if(repeatsSettingString=="Tuesday"){
            this.tuesday=true
        }
        else if(repeatsSettingString=="Wednesday"){
            this.wednesday=true
        }
        else if(repeatsSettingString=="Thursday"){
            this.thursday=true
        }
        else if(repeatsSettingString=="Friday"){
            this.friday=true
        }
        else if(repeatsSettingString=="Saturday"){
            this.saturday=true
        }
        else if(repeatsSettingString=="Weekdays"){
            this.monday=true
            this.tuesday=true
            this.wednesday=true
            this.thursday=true
            this.friday=true
        }
        else if(repeatsSettingString=="Day"){
            //this means every day
            this.sunday=true
            this.monday=true
            this.tuesday=true
            this.thursday=true
            this.friday=true
            this.saturday=true
        }
    }

    //makes task string for saving to files

    public override fun toString():String{
        var out:String=""

        //0
        out+=this.name
        out+="\t"
        //1
        if(this.plannedTime==null){
            out+="NULL"
        }
        else {
            out += this.plannedTime.toString()
        }
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
        out+="\t"
        //6
        out+=this.everyOther
        out+="\t"
        //7
        out+=this.repeatsSettingString
        out+="\t"


        return out
    }

    //makes task from string
    @Throws(RepeatingTaskInvalidStringException::class)
    public fun fromString(str:String)  {
        try {
            //remember backwards compatibility
            val stuff = str.split("\t")

            this.name = stuff[0]
            if (stuff[1] == "NULL") {
                this.plannedTime = null
            } else {
                try {
                    this.plannedTime = Time.valueOf(stuff[1])
                }
                catch (e:Error){
                    throw RepeatingTaskInvalidStringException(
                        RepeatingTaskInvalidStringException.exceptionCause.INVALIDDATATYPE,str)
                }
            }
            this.fixedTime = stuff[2] == "true"

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
            try {
                this.startDate = Date.valueOf(stuff[4])
            }
            catch(e:Error){
                throw RepeatingTaskInvalidStringException(
                    RepeatingTaskInvalidStringException.exceptionCause.INVALIDDATATYPE,str)
            }

            this.descripton = stuff[5]
            this.everyOther = stuff[6] == "true"

            this.setRepeatsSetting(stuff[7])
        }
        catch (e:IndexOutOfBoundsException){
            throw RepeatingTaskInvalidStringException(
                RepeatingTaskInvalidStringException.exceptionCause.INDEXOUTOFBOUNDS,str)
        }
    }


    public fun copy(): RepeatingTask {
        var out= RepeatingTask(
            name,
            fixedTime,
            startDate,
            everyOther
        )
        out.setDescription(descripton)
        plannedTime?.let { out.setPlannedTime(it) }
        out.setStartDate(startDate)
        return out
    }

    //returns true if the task should repeat for the given Date
    public fun validDay(subject: java.sql.Date):Boolean{
        //setup calendar for subject
        val subjectCal:Calendar = Calendar.getInstance()
        subjectCal.time=subject

        //setup calendar for the start date
        val startDateCal:Calendar = Calendar.getInstance()
        startDateCal.time=this.startDate

        //check that the subject is after the start date
        if(subjectCal.time.time - startDateCal.time.time <0){
            //before start date
            return false
        }
        if(this.repeatsSettingString=="Day"){
            //deal with the day setting
            if(this.everyOther){
                //we have shit to do
                val timeDifference= (
                        (subjectCal.time.time - startDateCal.time.time) / (1000 * 60 * 60 * 24)
                        ) as Int//number of full days between subject and startDate
                return timeDifference%2==0
            }
            else{
                //we're good here
                return true
            }
        }
        //if we got here then everyOther means every other week
        if(everyOther){
            //check if even number of weeks away from the start week
            val fullWeeks= (
                    (subjectCal.time.time - startDateCal.time.time) / (1000 * 60 * 60 * 24 * 7)
                    ) as Int//find the number of full weeks between start date and subject
            if(fullWeeks%2==0){
                //continue
                //must pass next check
            }
            else{
                //failure
                //dissapointing I know
                return false
            }
        }
        //last check
        //check day of week is correct
        if(subjectCal.get(Calendar.DAY_OF_WEEK)==Calendar.SUNDAY){
            return this.sunday
        }
        else if(subjectCal.get(Calendar.DAY_OF_WEEK)==Calendar.MONDAY){
            return this.monday
        }
        else if(subjectCal.get(Calendar.DAY_OF_WEEK)==Calendar.TUESDAY){
            return this.tuesday
        }
        else if(subjectCal.get(Calendar.DAY_OF_WEEK)==Calendar.WEDNESDAY){
            return this.wednesday
        }
        else if(subjectCal.get(Calendar.DAY_OF_WEEK)==Calendar.THURSDAY){
            return this.thursday
        }
        else if(subjectCal.get(Calendar.DAY_OF_WEEK)==Calendar.FRIDAY){
            return this.friday
        }
        else if(subjectCal.get(Calendar.DAY_OF_WEEK)==Calendar.SATURDAY){
            return this.saturday
        }


        //just here to stop compile time errors
        //this will never run
        return true
    }
}