package com.learning.careerconnect.Activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.chaos.view.PinView
import com.learning.careerconnect.MVVM.AuthenticationMVVM
import com.learning.careerconnect.Model.LoginIM
import com.learning.careerconnect.Model.ResendOTPIM
import com.learning.careerconnect.Model.ResetPasswordIM
import com.learning.careerconnect.Model.VerifyOTPIM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {
    lateinit var binding:ActivitySignInBinding
    var email:String = ""
    var password:String = ""
    lateinit var otpDialog: Dialog
    lateinit var emailDialog: Dialog
    lateinit var resetPasswordDialog: Dialog
    lateinit var submitOtpButton: TextView
    lateinit var progressBarOTPButton: ProgressBar
    lateinit var timerTV : TextView

    lateinit var authVM: AuthenticationMVVM
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authVM = ViewModelProvider(this)[AuthenticationMVVM::class.java]
        if(intent.hasExtra(Constants.EMAIL) && intent.hasExtra(Constants.PASSWORD)) {
            email = intent.getStringExtra(Constants.EMAIL).toString()
            password = intent.getStringExtra(Constants.PASSWORD).toString()

            binding.emailET.setText(email)
            binding.passwordET.setText(password)
        }
        Glide
            .with(this)
            .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/auth_image2.png")
            .placeholder(R.drawable.career_connect_white_bg)
            .into(binding.generalIv)
        binding.signInBtn.setOnClickListener {
            email = binding.emailET.text.toString()
            password = binding.passwordET.text.toString()
            if(valid(email,password))
            {
                binding.progressBar.visibility= View.VISIBLE
                binding.signInBtn.visibility= View.INVISIBLE
                authVM.loginUser(LoginIM(email,password),this,this)
            }
        }
        authVM.observerForLoginUser().observe(this, Observer{
            Log.d("rk",it.toString())
            if(it.status == "success")
            {
                binding.progressBar.visibility= View.INVISIBLE
                binding.signInBtn.visibility= View.VISIBLE
                toast("Successfully Login!!!",this)
                val i =Intent(this, MainActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(i)
                finish()
            }
            else
            {
                toast(it.message.toString(),this)
            }
        })
        authVM.observerForVerifyOTPUser().observe(this, Observer{
            toast(it.message!!,this@SignInActivity)

            if(it.message == "Successfully verified user")
            {
                otpDialog.dismiss()
                val i =Intent(this, SignInActivity::class.java)
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(i)
                finish()
            }
            else
            {
                submitOtpButton.visibility=View.VISIBLE
                progressBarOTPButton.visibility=View.INVISIBLE

            }

            if(it.message == "Time Out!!!. Please click on reset button")
            {
                authVM.resendOTP(ResendOTPIM(email),this,this)
                countDownTimerFn(timerTV,300000)
            }

        })

        authVM.observerForResendOTP().observe(this, Observer{
            toast(it.message!!,this@SignInActivity)

            if(it.message == "successfully send OTP , Please check your email id")
            {
                emailDialog.dismiss()
                showResetPasswordDialog()
            }
            else if(it.message =="password change successfully")
            {
                resetPasswordDialog.dismiss()
            }
            else
            {
                submitOtpButton.visibility=View.VISIBLE
                progressBarOTPButton.visibility=View.INVISIBLE

            }
        })

        binding.forgottenPassword.setOnClickListener {
            showEmailDialog()
        }

        authVM.observerForResetPassword().observe(this, Observer{
            if(it.message == "Please check your email or otp")
            {
                it.message = "Please check your OTP"
            }
            toast(it.message!!,this@SignInActivity)

            if(it.message == "password change successfully")
            {
                resetPasswordDialog.dismiss()
            }
            else
            {
                submitOtpButton.visibility=View.VISIBLE
                progressBarOTPButton.visibility=View.INVISIBLE

            }


        })
    }
    fun errorFn(message:String) {

        if(message == "Please verify your email first")
        {
            showOTPDialog()
        }
        binding.progressBar.visibility= View.INVISIBLE
        binding.signInBtn.visibility= View.VISIBLE
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
    private fun valid(email:String,password:String): Boolean {
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
        return true
    }
    override fun onBackPressed() {
        if (isOnlyOneActivityInStack())
            showBackBtnDialog("Would you like to exit",this)
        else
            super.onBackPressed()
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
            authVM.resendOTP(ResendOTPIM(email),this,this)
            countDownTimerFn(timerTV,300000)
        }
        submitOtpButton.setOnClickListener {
            val otp = view.findViewById<PinView>(R.id.otpET).text.toString()
            if(otp.length > 0)
            {
                submitOtpButton.visibility=View.INVISIBLE
                progressBarOTPButton.visibility=View.VISIBLE
                authVM.verifyOTP(VerifyOTPIM(email,otp),this,this@SignInActivity)
            }
            else
            {
                view.findViewById<PinView>(R.id.otpET).setError("Please enter otp")
            }
        }
        otpDialog.show()
    }

    private fun showResetPasswordDialog() {
        resetPasswordDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        resetPasswordDialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.reset_password_show_dialog, null)
        submitOtpButton = view.findViewById(R.id.resetPassBtn)
        progressBarOTPButton = view.findViewById(R.id.progressBarDialog)
        timerTV = view.findViewById(R.id.timerTVReset)
        resetPasswordDialog.setContentView(view)
        resetPasswordDialog.setCanceledOnTouchOutside(false)
        val window = resetPasswordDialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)
        countDownTimerFn(timerTV,300000)
        timerTV.setOnClickListener{
            countDownTimer.cancel()
            authVM.resendOTP(ResendOTPIM(email,"forgottenPassword"),this,this)
            countDownTimerFn(timerTV,300000)
        }
        submitOtpButton.setOnClickListener {
            val otp = view.findViewById<PinView>(R.id.otpETResetPass).text.toString()
            val password = view.findViewById<EditText>(R.id.passwordETReset).text.toString()
            if(otp.length > 0 && password.length >= 8)
            {
                submitOtpButton.visibility=View.INVISIBLE
                progressBarOTPButton.visibility=View.VISIBLE
                authVM.resetPassword(ResetPasswordIM(email,otp,password),this,this@SignInActivity)
            }
            else if(otp.length <=0)
            {
                view.findViewById<PinView>(R.id.otpETResetPass).setError("Please enter otp")
            }
            else if( password.length <=0)
            {
                view.findViewById<EditText>(R.id.passwordETReset).setError("Please enter password")
            }else if( password.length <8)
            {
                view.findViewById<EditText>(R.id.passwordETReset).setError("password length should be 8 minimum char long")
            }
        }
        resetPasswordDialog.show()
    }
    private fun showEmailDialog() {
        emailDialog = Dialog(this, android.R.style.Theme_Translucent_NoTitleBar)
        emailDialog = Dialog(this)
        val view: View = LayoutInflater.from(this).inflate(R.layout.email_show_dialog, null)
        submitOtpButton = view.findViewById(R.id.sendOTP)
        progressBarOTPButton = view.findViewById(R.id.progressBarDialog)
        emailDialog.setContentView(view)
        emailDialog.setCanceledOnTouchOutside(false)
        val window = emailDialog.window
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setGravity(Gravity.BOTTOM)
        submitOtpButton.setOnClickListener {
            email = view.findViewById<EditText>(R.id.emailETDialog).text.toString()
            if(email.length > 0)
            {
                submitOtpButton.visibility=View.INVISIBLE
                progressBarOTPButton.visibility=View.VISIBLE
                authVM.resendOTP(ResendOTPIM(email,"forgottenPassword"),this,this@SignInActivity)
            }
            else
            {
                view.findViewById<EditText>(R.id.emailETDialog).setError("Please enter otp")
            }
        }
        emailDialog.show()
    }
    fun errorFnForVerifyOTP(message:String,context: Context) {
        submitOtpButton.visibility=View.VISIBLE
        progressBarOTPButton.visibility=View.INVISIBLE
        errorFn(message)
    }
}