package com.learning.careerconnect.Activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.learning.careerconnect.Adapter.JobResponsibilitiesAdapter
import com.learning.careerconnect.MVVM.JobMVVM
import com.learning.careerconnect.Model.ApplyJobIM
import com.learning.careerconnect.Model.SavedJobIM
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.ActivityDisplayJobInDetailBinding


class DisplayJobInDetailActivity : BaseActivity() {
    lateinit var binding:ActivityDisplayJobInDetailBinding
    lateinit var job : SearchAllJobsOM.Data.Data
    lateinit var jobMVVM:JobMVVM
    lateinit var token :String
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityDisplayJobInDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        jobMVVM = ViewModelProvider(this)[JobMVVM::class.java]

        val sharedPreference =  getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        token = sharedPreference.getString(Constants.JWT_TOKEN_SP,"rk").toString()
        if(intent.hasExtra(Constants.JOB_DATA))
        {
            job = intent.getSerializableExtra(Constants.JOB_DATA) as SearchAllJobsOM.Data.Data
        }
        populateDate()

        binding.LL10.setOnClickListener {
            binding.applyBtnProgressBar.visibility = View.VISIBLE
            jobMVVM.applyForJob(this,"Bearer $token", ApplyJobIM(job._id))
        }

        jobMVVM.observerForApplyForJob().observe(this, Observer {
            result->
            binding.applyBtnProgressBar.visibility = View.GONE
            if(result.status == "success")
            {
                finish()
            }
        })

        binding.imageOfbookmark.setOnClickListener {
            jobMVVM.saveJobForLater(this,"Bearer $token", SavedJobIM(job._id))
        }

        jobMVVM.observerForSaveJobForLater().observe(this, Observer {
                result->
            Log.d("rk",result.toString())
            if(result.status == "success")
            {
                Glide.with(this)
                    .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/bookmark_closed.png")
                    .placeholder(R.drawable.career_connect_white_bg)
                    .into(binding.imageOfbookmark)
                binding.aboutRole.text=job.descriptionAboutRole
            }
        })
    }

    private fun populateDate() {
        binding.nameOfCompany.text = job.nameOfCompany
        binding.location.text = job.location?.get(0) ?: "Not Disclosed"
        binding.typeOfJob.text = job.typeOfJob
        binding.toolbar.title = job.nameOfRole

        Glide.with(this)
            .load(job.companyLinks?.get(1)?.link)
            .placeholder(R.drawable.career_connect_white_bg)
            .into(binding.imageOfCompany)

        Glide.with(this)
            .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/bookmark_open.png")
            .placeholder(R.drawable.career_connect_white_bg)
            .into(binding.imageOfbookmark)
        binding.aboutRole.text=job.descriptionAboutRole


        var responAndQualifi =ArrayList<String>()
        var skillsReqArr =ArrayList<String>()
        var perksArr =ArrayList<String>()

        for(i in 0..job.responsibilities!!.size-1) job.responsibilities?.get(i)?.let { responAndQualifi.add(it) }
        for(i in 0..job.minimumQualification!!.size-1) job.minimumQualification?.get(i)?.let { responAndQualifi.add(it) }
        for(i in 0..job.skillsRequired!!.size-1) job.skillsRequired?.get(i)?.let { skillsReqArr.add(it) }
        for(i in 0..job.perks!!.size-1) job.perks?.get(i)?.let { perksArr.add(it) }


        binding.responsibilitiesRV.layoutManager = LinearLayoutManager(this)
        val itemAdapter = JobResponsibilitiesAdapter(responAndQualifi)
        binding.responsibilitiesRV.adapter = itemAdapter

        binding.requiredSkillsRV.layoutManager = LinearLayoutManager(this)
        val itemAdapter1 = JobResponsibilitiesAdapter(skillsReqArr)
        binding.requiredSkillsRV.adapter = itemAdapter1

        binding.perksRV.layoutManager = LinearLayoutManager(this)
        val itemAdapter2 = JobResponsibilitiesAdapter(perksArr)
        binding.perksRV.adapter = itemAdapter2


        binding.aboutCompany.text = job.aboutCompany

        binding.ctc.text = "CTC -: ${job.costToCompany}"

        if(job.durationOfInternship !=null)
            binding.duration.text = "Duration -: ${job.durationOfInternship}"
        else
            binding.duration.visibility= View.GONE
        binding.noOfOpening.text = "No of Openings -: ${job.noOfOpening}"
        binding.noOfApplicants.text = "No of Applicants -: ${job.noOfStudentsApplied}"
        binding.startDate.text = "Start date -: ${job.startDate}"


        binding.nameOfCompany.setOnClickListener {
            val url = job.companyLinks!!.get(0)!!.link
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
        }
    }

    fun errorFn(message:String) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        binding.applyBtnProgressBar.visibility = View.GONE
    }
}