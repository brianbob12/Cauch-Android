package com.example.scheduleapp

import android.content.Context
import android.util.Log
import java.io.*
import java.sql.Date
import java.util.*


/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

//a class to hold persistent information that is ot best suited anywhere else
class PersistentContainer(){
    //a list of days that have tasks in them
    //when a user is prompted to postpone the tasks that date is removed
    //note java.util.date
    public var daysWithStuff: LinkedList<Date> = LinkedList<Date>()//empty

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
    }

}