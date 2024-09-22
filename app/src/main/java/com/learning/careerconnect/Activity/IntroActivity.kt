package com.learning.careerconnect.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.learning.careerconnect.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {
    lateinit var binding:ActivityIntroBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityIntroBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("rk","on create activity called from intro activity")
        binding.signIn.setOnClickListener {
            startActivity(Intent(this,SignInActivity::class.java))
        }
        binding.signUp.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("rk","on destroy activity called from intro activity")
    }
}