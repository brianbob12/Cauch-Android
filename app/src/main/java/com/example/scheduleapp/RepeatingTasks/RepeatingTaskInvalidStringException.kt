package com.example.scheduleapp.RepeatingTasks

import java.lang.Exception

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

//a custom exception for a string bing invalid as a repeating task
class RepeatingTaskInvalidStringException(myCause:exceptionCause,source:String):Exception() {

    val myCause: exceptionCause
    val source:String

    init {
        this.myCause=myCause
        this.source=source
    }

    override fun toString(): String {
        return super.toString()
    }

    enum class exceptionCause{
        INDEXOUTOFBOUNDS,
        INVALIDDATATYPE
    }
}