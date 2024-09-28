package com.learning.careerconnect.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learning.careerconnect.MVVM.AuthenticationMVVM
import com.learning.careerconnect.Model.RefreshTokenIM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var binding:ActivitySplashScreenBinding
    lateinit var authVM: AuthenticationMVVM
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySplashScreenBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        authVM = ViewModelProvider(this)[AuthenticationMVVM::class.java]
        val sharedPreference =  getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        val token = sharedPreference.getString(Constants.JWT_TOKEN_SP,"rk")
        val refreshToken = sharedPreference.getString(Constants.REFRESH_TOKEN_SP,"rk")
        val timeLeft = sharedPreference.getLong(Constants.TIME_LEFT,-1)
        val currentTime = System.currentTimeMillis()
//        val currentTime = 1728406955000L
        val time = (currentTime-timeLeft)/(86400000);
//        Log.d("rk","token ${token}")
//        Log.d("rk","refreshToken ${refreshToken}")
//        Log.d("rk","time left ${timeLeft}")
//        Log.d("rk","current time ${currentTime}")
//        Log.d("rk","time ${time}")
        if(timeLeft!=-1L && token!="rk" && time <=8)
        {
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        else  if(timeLeft !=-1L && refreshToken!="rk" && time <=9)
        {
            authVM.refreshToken(RefreshTokenIM(refreshToken),this,this, "Bearer ${token}")
        }
        else if(timeLeft !=-1L && token!="rk" && time >= 10)
        {
            startActivity(Intent(this, SignInActivity::class.java))
            finish()
        }
        else
        {
            startActivity(Intent(this, WalkThroughActivity::class.java))
            finish()
        }
        authVM.observerForRefreshToken().observe(this, Observer {
//            Log.d("rk",it.toString())
            if(it.status=="success")
            {
                var editor = sharedPreference.edit()
                editor.putString(Constants.JWT_TOKEN_SP,it.token)
                editor.commit()
                startActivity(Intent(this,MainActivity::class.java))
                finish()
            }
            else
            {
                startActivity(Intent(this,IntroActivity::class.java))
                finish()
            }
        })
    }

    fun errorFn(message:String) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
}