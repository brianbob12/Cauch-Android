package com.example.scheduleapp

/* Copyright (C) Cyrus Singer - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Cyrus Singer <japaneserhino@gmail.com>, October 2020
 */

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.core.view.setMargins

class TagView : ConstraintLayout {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, tag: TaskTag) : super(context) {



        //add text
        val textParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        textParams.topMargin=7.toDp(context)
        textParams.leftMargin=24.toDp(context)
        textParams.bottomMargin=7.toDp(context)
        textParams.rightMargin=24.toDp(context)
        val textView: TextView= TextView(context)
        textView.text=tag.getName()
        textView.textSize= 14F
        textView.layoutParams=textParams

        val holder:LinearLayout= LinearLayout(context)
        holder.addView(textView)
        addView(holder)
        textView.id= View.generateViewId()

        //set color
        background= context.getDrawable(R.drawable.task_tag_shape)
        background.setTint(tag.getColor())

        //set perams
        val layParams: ConstraintLayout.LayoutParams = ConstraintLayout.LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        layParams.topMargin=7
        layParams.setMargins(25)

        isVisible=true

        layoutParams=layParams

    }

    // Extension method to convert pixels to dp
    //credit https://android--code.blogspot.com/2020/02/android-kotlin-constraintlayout-set_28.html
    fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
    ).toInt()

}