package com.learning.careerconnect.Activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chaos.view.PinView
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.ActivitySignUpBinding


class SignUpActivity : BaseActivity() {
    lateinit var binding:ActivitySignUpBinding
    lateinit var otpDialog: Dialog
    lateinit var timerTV : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Glide
            .with(this)
            .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/auth_image1.png")
            .placeholder(R.drawable.career_connect_white_bg)
            .into(binding.generalIv)
        val languages = resources.getStringArray(R.array.typeOfUser)
        val arrayAdapter = ArrayAdapter(this, R.layout.drop_down_item_text_view, languages)
        binding.chooseTypeET.setAdapter(arrayAdapter)
        binding.chooseTypeET.keyListener=null
        binding.chooseTypeET.setOnClickListener {
            binding.chooseTypeET.showDropDown()
        }
        binding.chooseTypeET.setOnDismissListener {
            Log.d("rk",binding.chooseTypeET.text.toString())
        }

        binding.redirectToLoginPage.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
            finish()
        }

        binding.signUpBtn.setOnClickListener {
             showOTPDialog()
        }
    }

    private fun showOTPDialog() {
        otpDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        otpDialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.otp_show_dialog, null)
        val submitOtpButton = view.findViewById<TextView>(R.id.verifyAccountBtn)
        otpDialog.setContentView(view)
        otpDialog.setCanceledOnTouchOutside(false)
        val window = otpDialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)
        submitOtpButton.setOnClickListener {
            val otp = view.findViewById<PinView>(R.id.otpET).text.toString()
            val i =Intent(this, SignInActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
            finish()
        }
        otpDialog.show()
    }

    override fun onBackPressed() {
        if (isOnlyOneActivityInStack())
            showBackBtnDialog("Would you like to exit",this)
        else
            super.onBackPressed()
    }
}