package com.example.scheduleapp.ui.pomodoro

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.scheduleapp.R
import kotlinx.android.synthetic.main.fragment_pomodoro.view.*


class PomodoroFragment() : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val root: View = inflater.inflate(R.layout.fragment_pomodoro, container, false)
        //Making timer
        val text: TextView = root.findViewById(R.id.timerText)
        object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                text.setText("seconds remaining: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                text.setText("Time's finished!")
            }
        }.start()

        return root
    }
}