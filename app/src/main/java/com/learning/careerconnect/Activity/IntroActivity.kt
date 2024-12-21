package com.learning.careerconnect.Activity

import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.ActivityIntroBinding

class IntroActivity : BaseActivity() {
    lateinit var binding:ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityIntroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // getting intro image
        Glide
            .with(this)
            .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/intro_image.png")
            .placeholder(R.drawable.career_connect_white_bg)
            .into(binding.imageView)
        binding.signIn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
        binding.signUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
        // checking permission
        if(!checkNotificationPermission(this))
        {
            requestPermission(this)
        }
    }
    override fun onBackPressed() {
        if (isOnlyOneActivityInStack())
            showBackBtnDialog("Would you like to exit",this)
        else
            super.onBackPressed()
    }
}