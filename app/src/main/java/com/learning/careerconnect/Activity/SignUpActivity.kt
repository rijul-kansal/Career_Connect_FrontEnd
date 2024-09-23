package com.learning.careerconnect.Activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import com.bumptech.glide.Glide
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.ActivitySignUpBinding


class SignUpActivity : BaseActivity() {
    lateinit var binding:ActivitySignUpBinding
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