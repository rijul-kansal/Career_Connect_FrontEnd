package com.learning.careerconnect.Activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.learning.careerconnect.Adapter.JobResponsibilitiesAdapter
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.ActivityDisplayJobInDetailBinding


class DisplayJobInDetailActivity : AppCompatActivity() {
    lateinit var binding:ActivityDisplayJobInDetailBinding
    lateinit var job : SearchAllJobsOM.Data.Data
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityDisplayJobInDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.JOB_DATA))
        {
            job = intent.getSerializableExtra(Constants.JOB_DATA) as SearchAllJobsOM.Data.Data
        }
        populateDate()

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
}