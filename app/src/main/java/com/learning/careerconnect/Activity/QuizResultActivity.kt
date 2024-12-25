package com.learning.careerconnect.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.ActivityQuizResultBinding

class QuizResultActivity : BaseActivity() {
    lateinit var binding: ActivityQuizResultBinding
    var score:Int=0
    lateinit var video:Uri
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        score = intent.getIntExtra(Constants.SCORE,0)
        if(score==0 || score==1)
            video = Uri.parse("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/one.mp4")
        else if(score==2)
            video = Uri.parse("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/two.mp4")
        else if(score==3)
            video = Uri.parse("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/three.mp4")
        else if(score==4)
            video = Uri.parse("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/four.mp4")
        else
            video = Uri.parse("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/five.mp4")
        binding.videoView.setVideoURI(video)
        binding.videoView.setOnPreparedListener(MediaPlayer.OnPreparedListener { mp ->
            mp.isLooping = true
            binding.videoView.start()
        })
        binding.goBackBtn.setOnClickListener {
            var i =Intent(this,MainActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(i)
            finish()
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
            toast("Press Go Back Btn",this)

    }
}