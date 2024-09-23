package com.learning.careerconnect.Activity

import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.learning.careerconnect.R

open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
}