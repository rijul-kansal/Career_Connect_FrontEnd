package com.learning.careerconnect.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.learning.careerconnect.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var binding:ActivitySplashScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val handler = Handler()
        handler.postDelayed( {
            startActivity(Intent(this, WalkThroughActivity::class.java))
            finish()
        }, 1000)
    }
}