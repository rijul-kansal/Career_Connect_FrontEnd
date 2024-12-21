package com.learning.careerconnect.Activity

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.learning.careerconnect.MVVM.QuizMVVM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.ActivityQuizDisplayBinding

class QuizDisplayActivity : BaseActivity() {

    lateinit var binding: ActivityQuizDisplayBinding
    lateinit var quizVM:QuizMVVM
    var quizType : String="AWS"
    lateinit var token:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        quizVM = ViewModelProvider(this)[QuizMVVM::class.java]
        val sharedPreference =  getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        token = sharedPreference.getString(Constants.JWT_TOKEN_SP,"rk").toString()
        if(intent.hasExtra(Constants.TYPE_OF_QUIZ))
        {
            quizType = intent.getStringExtra(Constants.TYPE_OF_QUIZ).toString()
        }
        showProgressBar(this)
        quizVM.getQuestion(this,quizType,"Bearer $token")
        quizVM.observerForGetQuestion().observe(this , Observer {
            res->
            if(res.status == "success")
            {
                cancelProgressBar()
                Log.d("rk",res.toString())
            }
        })
    }
    fun errorFn(message:String) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
}