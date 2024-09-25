package com.learning.careerconnect.Activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.chaos.view.PinView
import com.learning.careerconnect.MVVM.AuthenticationMVVM
import com.learning.careerconnect.Model.ResendOTPIM
import com.learning.careerconnect.Model.SignUpIM
import com.learning.careerconnect.Model.VerifyOTPIM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.ActivitySignUpBinding
import kotlin.math.abs


class SignUpActivity : BaseActivity() {
    lateinit var binding:ActivitySignUpBinding
    lateinit var otpDialog: Dialog
    lateinit var authVM:AuthenticationMVVM
    lateinit var name :String
    lateinit var email :String
    lateinit var password :String
    lateinit var mobileNumber :String
    lateinit var typeOfUser :String

    lateinit var submitOtpButton:TextView
    lateinit var progressBarOTPButton:ProgressBar
    lateinit var timerTV : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignUpBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authVM = ViewModelProvider(this)[AuthenticationMVVM::class.java]
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

        binding.redirectToLoginPage.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }

        binding.signUpBtn.setOnClickListener {
            name = binding.nameET.text.toString()
            email = binding.emailET.text.toString()
            password = binding.passwordET.text.toString()
            mobileNumber = binding.mobileNumber.text.toString()
            typeOfUser = binding.chooseTypeET.text.toString()
            if(valid(name,email,password,mobileNumber,typeOfUser))
            {
                    binding.progressBar.visibility= View.VISIBLE
                    binding.signUpBtn.visibility= View.INVISIBLE
                    authVM.signUpUser(SignUpIM(email,mobileNumber,name,password,typeOfUser),this,this)
            }
        }

        authVM.observerForSignUpUser().observe(this, Observer{
            toast("Successfully register!!!",this@SignUpActivity)
            binding.progressBar.visibility= View.INVISIBLE
            binding.signUpBtn.visibility= View.VISIBLE
            showOTPDialog()
        })

        authVM.observerForVerifyOTPUser().observe(this, Observer{
            toast(it.message!!,this@SignUpActivity)

            if(it.message == "Successfully verified user")
            {
                otpDialog.dismiss()
                val i =Intent(this, SignInActivity::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                i.putExtra(Constants.EMAIL , email)
                i.putExtra(Constants.PASSWORD , password)
                startActivity(i)
                finish()
            }
            else
            {
                submitOtpButton.visibility=View.VISIBLE
                progressBarOTPButton.visibility=View.INVISIBLE

           }

        })
        authVM.observerForResendOTP().observe(this, Observer{
            toast(it.message!!,this@SignUpActivity)
        })
    }

    private fun valid(name:String,email:String,password:String,mobileNumber:String,typeOfUser:String): Boolean {
        if(name.length == 0)
        {
            binding.nameET.error = "Please enter name"
            return false
        }
        if(email.length == 0)
        {
            binding.emailET.error = "Please enter email"
            return false
        }
        if(password.length == 0)
        {
            binding.passwordET.error = "Please enter password"
            return false
        }
        if(password.length < 8)
        {
            binding.passwordET.error = "Password length should be minimum 8 char"
            return false
        }
        if(mobileNumber.length == 0)
        {
            binding.mobileNumber.error = "Please enter mobile number"
            return false
        }
        if(mobileNumber.length < 10)
        {
            binding.mobileNumber.error = "Please enter correct mobile number"
            return false
        }
        if(typeOfUser != "User" && typeOfUser !="Recruiter")
        {
            binding.chooseTypeET.error = "Please choose one option"
            return false
        }
        return true
    }

    private fun showOTPDialog() {
        otpDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        otpDialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.otp_show_dialog, null)
        submitOtpButton = view.findViewById(R.id.verifyAccountBtn)
        progressBarOTPButton = view.findViewById(R.id.progressBarDialog)
        timerTV = view.findViewById(R.id.timerTV)
        otpDialog.setContentView(view)
        otpDialog.setCanceledOnTouchOutside(false)
        val window = otpDialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)
        timerTV.setOnClickListener{
            countDownTimer.cancel()
            authVM.resendOTP(ResendOTPIM(email,"verifyUser"),this,this)
            countDownTimerFn(timerTV,300000)
        }
        countDownTimerFn(timerTV,300000)
        submitOtpButton.setOnClickListener {
            val otp = view.findViewById<PinView>(R.id.otpET).text.toString()
            if(otp.length > 0)
            {
                submitOtpButton.visibility=View.INVISIBLE
                progressBarOTPButton.visibility=View.VISIBLE
                authVM.verifyOTP(VerifyOTPIM(email,otp),this,this@SignUpActivity)
            }
            else
            {
                view.findViewById<PinView>(R.id.otpET).setError("Please enter otp")
            }
        }
        otpDialog.show()
    }

    override fun onBackPressed() {
        if (isOnlyOneActivityInStack())
            showBackBtnDialog("Would you like to exit",this)
        else
            super.onBackPressed()
    }

    fun errorFn(message:String,context: Context) {
        binding.progressBar.visibility= View.INVISIBLE
        binding.signUpBtn.visibility= View.VISIBLE
        Toast.makeText(context,message, Toast.LENGTH_LONG).show()
    }
    fun errorFnForVerifyOTP(message:String,context: Context) {
        submitOtpButton.visibility=View.VISIBLE
        progressBarOTPButton.visibility=View.INVISIBLE
        errorFn(message,context)
    }
}