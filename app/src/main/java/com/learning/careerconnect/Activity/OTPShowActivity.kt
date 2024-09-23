package com.learning.careerconnect.Activity


import android.os.Bundle
import com.learning.careerconnect.R

class OTPShowActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otpshow)
    }
    override fun onBackPressed() {
        if (isOnlyOneActivityInStack())
            showBackBtnDialog("Would you like to exit",this)
        else
            super.onBackPressed()
    }
}