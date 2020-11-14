package com.example.scheduleapp.Tasks

import java.lang.Exception

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

//a custom exception for a string being invalid as a task
class TaskInvalidStringException(myCause:exceptionCause,source: String):Exception() {
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