package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.graphics.Color
import java.io.Serializable

//class that colds information about each task
//serializable for export
class TaskTag:Serializable {
    private var name:String//tag names cannont include "\n" "\t" or ","
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

    //returs a string identifier
    //TODO when createing new tasks make sure that a new tag does not hav the same toString as an exsisting tag
    public override fun toString():String{
        var out:String=""
        out+=name
        out+=color.hashCode()
        return out
    }

}