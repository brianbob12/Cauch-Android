package com.example.scheduleapp

import android.graphics.Color
import java.io.Serializable

//class that colds information about each task
//serializable for export
class TaskTag:Serializable {
    private var name:String
    private var color:Int

    //getters and setters
    public fun getName():String{
        return name
    }
    public fun getColor():Int{
        return color
    }
    public fun copy():TaskTag{
        val new:TaskTag=TaskTag(name,color)
        //TODO update this as more variabes are added to tag
        return new
    }

    constructor(name:String, color: Int){
        this.name=name
        this.color=color
    }

}