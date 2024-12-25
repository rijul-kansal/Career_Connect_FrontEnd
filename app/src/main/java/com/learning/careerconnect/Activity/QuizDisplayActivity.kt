package com.learning.careerconnect.Activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.learning.careerconnect.MVVM.QuizMVVM
import com.learning.careerconnect.Model.AddQuizScoreToDBIM
import com.learning.careerconnect.Model.GetQuizQuestionDisplayOM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.ActivityQuizDisplayBinding


class QuizDisplayActivity : BaseActivity() {

    lateinit var binding: ActivityQuizDisplayBinding
    lateinit var quizVM:QuizMVVM
    var quizType : String="AWS"
    var correctQuestion =0
    var index:Int=0
    var chosenOption:String=""
    lateinit var token:String
    var countDownTimer2: CountDownTimer?=null

    lateinit var quizArr :ArrayList<GetQuizQuestionDisplayOM.Data.Data>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        correctQuestion = 0
        index=-1
        quizArr = ArrayList()
        val video = Uri.parse("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/quiz-animated-icon-download-in-lottie-json-gif-static-svg-file-formats--paper-test-exam-education-pack-school-icons-4898374.mp4")
        val video1 = Uri.parse("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/10-second-time-animated-icon-download-in-lottie-json-gif-static-svg-file-formats--digit-timer-pack-sign-symbols-icons-6656793.mp4")
        binding.videoView1.setVideoURI(video1)
        binding.videoView.setVideoURI(video)
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
                for(i in res.data!!.data!!)
                {
                    quizArr.add(i!!)
                    Log.d("rk","$i\n")
                }
                binding.main.visibility= View.VISIBLE
                binding.toolbar.title = "Question No. ${index+1}"
                displayNextQuestion()
            }
        })

        binding.videoView.setOnPreparedListener(OnPreparedListener { mp ->
            mp.isLooping = true
            binding.videoView.start()
        })
        binding.option1.setOnClickListener {
            makeAllWhiteBg()
            if(chosenOption == binding.option1.text)
            {
                chosenOption = ""
            }
            else
            {
                makeSingleGreyBg(1)
                chosenOption = binding.option1.text.toString()
            }
        }
        binding.option2.setOnClickListener {
            makeAllWhiteBg()
            if(chosenOption == binding.option2.text)
            {
                chosenOption = ""
            }
            else
            {
                makeSingleGreyBg(2)
                chosenOption = binding.option2.text.toString()
            }
        }
        binding.option3.setOnClickListener {
            makeAllWhiteBg()
            if(chosenOption == binding.option3.text)
            {
                chosenOption = ""
            }
            else
            {
                makeSingleGreyBg(3)
                chosenOption = binding.option3.text.toString()
            }
        }
        binding.option4.setOnClickListener {
            makeAllWhiteBg()
            if(chosenOption == binding.option4.text)
            {
                chosenOption = ""
            }
            else
            {
                makeSingleGreyBg(4)
                chosenOption = binding.option4.text.toString()
            }
        }

        binding.submitBtn.setOnClickListener {
            if(chosenOption != "")
            {
                if(chosenOption == quizArr[index].correct_answer)
                {
                    correctQuestion++
                }
                countDownTimer2!!.cancel()
                displayNextQuestion()
            }
            else{
                toast("Please choose atleast one option",this)
            }
        }
        quizVM.observerForAddQuizScoreToDB().observe(this , Observer {
                res->
            if(res.status == "success")
            {
                var arr  = quizArr
                var ii =0
                for(i in 0..arr.size-1)
                {
                    if(arr[i].type == quizType)
                    {
                        ii=i
                        break
                    }
                }
                arr.removeAt(ii)
                val gson = Gson()
                val jsonString = gson.toJson(arr)
                val sharedPreference = getSharedPreferences(Constants.Quiz_SP_PN, Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString(Constants.TYPE_OF_QUIZ_User_CAN_GIVE, jsonString)
                editor.commit()
                val intent = Intent(this , QuizResultActivity::class.java)
                intent.putExtra(Constants.SCORE,correctQuestion)
                startActivity(intent)
                finish()
            }
        })
    }

    private fun displayNextQuestion() {
        Log.d("rk",index.toString())
        binding.videoView1.seekTo(0)
        countDownTimer2=null
        index++
        if(index==4)
        {
            binding.submitBtn.text="Submit"
        }
        binding.videoView1.start()
        if(index<=4)
        {
            binding.question.text = quizArr[index].question
            binding.option1.text= quizArr[index].options?.get(0) ?: ""
            binding.option2.text= quizArr[index].options?.get(1) ?: ""
            binding.option3.text= quizArr[index].options?.get(2) ?: ""
            binding.option4.text= quizArr[index].options?.get(3) ?: ""
            makeAllWhiteBg()
            chosenOption = ""
            countDownTimerFn2(10000)
        }
        else
        {
            quizVM.addQuizScoreToDB(this, AddQuizScoreToDBIM(correctQuestion.toString(),quizType),"Bearer $token")
        }
    }

    private fun makeAllWhiteBg() {
        binding.option1.setBackgroundResource(R.drawable.filter_option_bg_white_colour)
        binding.option2.setBackgroundResource(R.drawable.filter_option_bg_white_colour)
        binding.option3.setBackgroundResource(R.drawable.filter_option_bg_white_colour)
        binding.option4.setBackgroundResource(R.drawable.filter_option_bg_white_colour)
    }

    private fun makeSingleGreyBg(index:Int)
    {
        if(index == 1)
        {
            binding.option1.setBackgroundResource(R.drawable.filter_option_bg_greu_colour)
        }
        else if(index== 2)
        {
            binding.option2.setBackgroundResource(R.drawable.filter_option_bg_greu_colour)
        }
        else if(index== 3)
        {
            binding.option3.setBackgroundResource(R.drawable.filter_option_bg_greu_colour)
        }else if(index== 4)
        {
            binding.option4.setBackgroundResource(R.drawable.filter_option_bg_greu_colour)
        }
    }

    fun errorFn(message:String) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        cancelProgressBar()
    }

    fun countDownTimerFn2(time:Long)
    {
        countDownTimer2 = object : CountDownTimer(time, 1000) {
            override fun onTick(millis: Long) {
            }
            override fun onFinish() {
                countDownTimer2 = null
                displayNextQuestion()
            }
        }.start()
    }
}