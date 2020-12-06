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
        val voluntary: Boolean = true
        while (voluntary == true) {
            object : CountDownTimer(1800000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val minutes: Long = millisUntilFinished / 1000 / 60
                    val seconds: Long =
                        millisUntilFinished / 1000 - 60 * (millisUntilFinished / 1000 / 60)
                    if (seconds >= 10) {
                        text.setText("" + minutes + ":" + seconds)
                    }
                    if (seconds < 10) {
                        text.setText("" + minutes + ":" + "0" + seconds)
                    }
                }


                override fun onFinish() {
                    text.setText("Well done! You're doing great!")
                    text.setText("-Why not take a little break?")
                }

            }.start()
            object : CountDownTimer(300000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val minutes: Long = millisUntilFinished / 1000 / 60
                    val seconds: Long =
                        millisUntilFinished / 1000 - 60 * (millisUntilFinished / 1000 / 60)
                    if (seconds >= 10) {
                        text.setText("" + minutes + ":" + seconds)
                    }
                    if (seconds < 10) {
                        text.setText("" + minutes + ":" + "0" + seconds)
                    }
                }


                override fun onFinish() {
                    text.setText("Let's get back to work!")

                }
            }.start()
            object : CountDownTimer(1800000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val minutes: Long = millisUntilFinished / 1000 / 60
                    val seconds: Long =
                        millisUntilFinished / 1000 - 60 * (millisUntilFinished / 1000 / 60)
                    if (seconds >= 10) {
                        text.setText("" + minutes + ":" + seconds)
                    }
                    if (seconds < 10) {
                        text.setText("" + minutes + ":" + "0" + seconds)
                    }
                }


                override fun onFinish() {
                    text.setText("Well done! You're doing great!")
                    text.setText("-Why not take a little break?")
                }

            }.start()
            object : CountDownTimer(300000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val minutes: Long = millisUntilFinished / 1000 / 60
                    val seconds: Long =
                        millisUntilFinished / 1000 - 60 * (millisUntilFinished / 1000 / 60)
                    if (seconds >= 10) {
                        text.setText("" + minutes + ":" + seconds)
                    }
                    if (seconds < 10) {
                        text.setText("" + minutes + ":" + "0" + seconds)
                    }
                }


                override fun onFinish() {
                    text.setText("Let's get back to work!")

                }
            }.start()
            object : CountDownTimer(1800000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val minutes: Long = millisUntilFinished / 1000 / 60
                    val seconds: Long =
                        millisUntilFinished / 1000 - 60 * (millisUntilFinished / 1000 / 60)
                    if (seconds >= 10) {
                        text.setText("" + minutes + ":" + seconds)
                    }
                    if (seconds < 10) {
                        text.setText("" + minutes + ":" + "0" + seconds)
                    }
                }


                override fun onFinish() {
                    text.setText("Well done! You're doing great!")
                    text.setText("-Why not take a little break?")
                }

            }.start()
            object : CountDownTimer(300000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val minutes: Long = millisUntilFinished / 1000 / 60
                    val seconds: Long =
                        millisUntilFinished / 1000 - 60 * (millisUntilFinished / 1000 / 60)
                    if (seconds >= 10) {
                        text.setText("" + minutes + ":" + seconds)
                    }
                    if (seconds < 10) {
                        text.setText("" + minutes + ":" + "0" + seconds)
                    }
                }


                override fun onFinish() {
                    text.setText("Let's get back to work!")

                }
            }.start()
            object : CountDownTimer(1800000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val minutes: Long = millisUntilFinished / 1000 / 60
                    val seconds: Long =
                        millisUntilFinished / 1000 - 60 * (millisUntilFinished / 1000 / 60)
                    if (seconds >= 10) {
                        text.setText("" + minutes + ":" + seconds)
                    }
                    if (seconds < 10) {
                        text.setText("" + minutes + ":" + "0" + seconds)
                    }
                }


                override fun onFinish() {
                    text.setText("Well done! You're doing great!")
                    text.setText("-Why not take a little break?")
                }

            }.start()
            object : CountDownTimer(900000, 1000) {
                override fun onTick(millisUntilFinished: Long) {

                    val minutes: Long = millisUntilFinished / 1000 / 60
                    val seconds: Long =
                        millisUntilFinished / 1000 - 60 * (millisUntilFinished / 1000 / 60)
                    if (seconds >= 10) {
                        text.setText("" + minutes + ":" + seconds)
                    }
                    if (seconds < 10) {
                        text.setText("" + minutes + ":" + "0" + seconds)
                    }
                }


                override fun onFinish() {
                    text.setText("Well done! You have completed one full set of work.")



                }
            }.start()



        }
        return root
    }
}