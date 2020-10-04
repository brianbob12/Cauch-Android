package com.example.scheduleapp

import java.io.Serializable
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList


//stores a list of tasks for each day in order
class DayList : Serializable{
    //it is very important that this is immutable
    public val date:Date

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
        var output:String=""
        output+=daysOfWeek.get(this.getDayOfWeek())
        output+=" "
        output+=this.date.day//depreciated
        return output
    }

    public fun addTask(task: Task){
        //TODO deal witht he order of tasks
        tasks.add(task)
    }
}