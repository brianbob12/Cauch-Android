package com.example.scheduleapp

import java.sql.Time
import java.util.*

class Task {
    public var color:Int =0
    public var plannedTime: Time? =null
    public var plannedDate:Date?=null
    public var name:String=""

    constructor(name:String){
        this.name=name
    }
}