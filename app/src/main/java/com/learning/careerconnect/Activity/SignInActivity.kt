package com.learning.careerconnect.Activity


import android.content.Intent
import android.os.Bundle
import com.bumptech.glide.Glide
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.ActivitySignInBinding

class SignInActivity : BaseActivity() {
    lateinit var binding:ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide
            .with(this)
            .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/auth_image2.png")
            .placeholder(R.drawable.career_connect_white_bg)
            .into(binding.generalIv)

        binding.signInBtn.setOnClickListener {
            val i =Intent(this, MainActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
            finish()
        }
    }

    override fun onBackPressed() {
        if (isOnlyOneActivityInStack())
            showBackBtnDialog("Would you like to exit",this)
        else
            super.onBackPressed()
    }
}