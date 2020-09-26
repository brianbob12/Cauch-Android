package com.example.scheduleapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.scheduleapp.R
import kotlinx.android.synthetic.main.activity_add_new_task.*


class AddNewTask : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_task)

        SubmitButton.setOnClickListener {

        }
    }
}