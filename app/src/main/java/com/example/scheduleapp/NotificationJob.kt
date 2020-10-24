package com.example.scheduleapp

import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build

import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.util.*


@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class NotificationJob : JobService() {

    //inputs a task and sends a notificatoin
    fun sendNotification(context: Context,name:String,description:String) {

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, 0)

        val builder = context.let {
            NotificationCompat.Builder(it, "TaskReady")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(name)
                .setContentText(description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
        }

        with(context.let { NotificationManagerCompat.from(it) }) {
            // notificationId is a unique int for each notification that you must define
            if (builder != null) {//I think this if is redundant
                this.notify(0, builder.build())
            }
        }
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false //this cancels the job
    }

    //when it is time
    override fun onStartJob(params: JobParameters?): Boolean {
        //we find our stored goodies
        val name=params?.extras?.getString("name")
        val description=params?.extras?.getString("description")
        if(name!=null && description!=null){
            //and if everthing is in place
            //we send our notification
            sendNotification(applicationContext, name,description)
        }
        return true
    }

}