package com.learning.careerconnect.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.CountDownTimer
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.learning.careerconnect.R
import kotlin.math.abs

open class BaseActivity : AppCompatActivity() {
    lateinit  var countDownTimer: CountDownTimer
    lateinit var dialog:Dialog
    // checking if only one activity is there or not in stack or not
    // if true then only 1 activity is there in stack
    fun isOnlyOneActivityInStack(): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val tasks = activityManager.appTasks
        for (task in tasks) {
            val taskInfo = task.taskInfo
            if (taskInfo.numActivities == 1) {
                return true
            }
        }
        return false
    }

    // display back btn
    fun showBackBtnDialog(message:String,context: Context)
    {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.exit_dialog)
        var text=dialog.findViewById<TextView>(R.id.textViewExitDialog)
        var okBtn=dialog.findViewById<TextView>(R.id.okBtnExitDialog)
        var cancelBtn=dialog.findViewById<TextView>(R.id.cancelBtnExitDialog)
        text.text = message
        okBtn.setOnClickListener {
            finish()
        }
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }

    // toast message
    fun toast(message:String,context: Context)
    {
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
    // count down fns
    fun countDownTimerFn(view:TextView,time:Long)
    {
        view.isClickable = false
        countDownTimer =object : CountDownTimer(time, 1000) {

            // Callback function, fired on regular interval
            override fun onTick(millis: Long) {
                var min = millis/(1000*60)
                view.text = "resend OTP in 0${min}:${abs(min*60*1000 - millis) /1000}"
            }

            override fun onFinish() {
                view.isClickable = true
                view.text = getString(R.string.resend_otp)
            }
        }.start()
    }
    // checking notification permission
    @SuppressLint("InlinedApi")
    fun checkNotificationPermission(context: Activity): Boolean {
        return (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
        ) == PackageManager.PERMISSION_GRANTED)
    }
    // else req permission
    fun requestPermission(context: Activity) {
        ActivityCompat.requestPermissions(context ,permissions(), 1)
    }

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    val storagePermissions33 = arrayOf(
        Manifest.permission.ACCESS_NOTIFICATION_POLICY,
        Manifest.permission.POST_NOTIFICATIONS
    )
    fun permissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            storagePermissions33
        } else {
            storagePermissions
        }
    }
    val storagePermissions = arrayOf(
        Manifest.permission.ACCESS_NOTIFICATION_POLICY,
        Manifest.permission.POST_NOTIFICATIONS
    )

    fun showProgressBar(activity: Activity)
    {
        dialog = Dialog(activity)
        dialog.setContentView(R.layout.progress_bar)
        dialog.show()
    }

    fun cancelProgressBar()
    {
        dialog.dismiss()
    }
}