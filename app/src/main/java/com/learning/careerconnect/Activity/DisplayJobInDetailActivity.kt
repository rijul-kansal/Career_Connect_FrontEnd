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
import com.google.gson.GsonBuilder
import com.learning.careerconnect.Adapter.JobResponsibilitiesAdapter
import com.learning.careerconnect.Cashes.JobAppliedDB
import com.learning.careerconnect.MVVM.JobMVVM
import com.learning.careerconnect.Model.AppliedJobsModelSQLite
import com.learning.careerconnect.Model.ApplyJobIM
import com.learning.careerconnect.Model.SavedJobIM
import com.learning.careerconnect.Model.SearchAllJobsOM
import com.learning.careerconnect.Model.UnSavedJobIM
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

        val sharedPreference1 =  getSharedPreferences(Constants.ONLY_JOBID_SP, Context.MODE_PRIVATE)
        val gson = GsonBuilder().create()
        val fileData= sharedPreference1.getString(Constants.ONLY_JOBID_ARR,"0")
        val jobIdArr = gson.fromJson(fileData , Array<String>::class.java).toMutableList()

        binding.LL10.setOnClickListener {

            binding.applyBtnProgressBar.visibility = View.VISIBLE
            jobMVVM.applyForJob(this,"Bearer $token", ApplyJobIM(job._id))
        }

        jobMVVM.observerForApplyForJob().observe(this, Observer {
            result->
            Log.d("rk","apply for job")


            binding.applyBtnProgressBar.visibility = View.GONE
            if(result.status == "success")
            {
                val dbHandler = JobAppliedDB(this)
                val data= dbHandler.read()
                data.removeAt(data.size-1)
                val model = getModel()
                data.add(0,model)
                dbHandler.deleteAll()

                for (i in 0..data.size - 1) {
                    val job = data[i]

                    dbHandler.add(
                        _id = job._id,
                        nameOfCompany = job.nameOfCompany,
                        aboutCompany = job.aboutCompany,
                        nameOfRole = job.nameOfRole,
                        typeOfJob = job.typeOfJob,
                        location = job.location,
                        startDate = job.startDate,
                        durationOfInternship = job.durationOfInternship,
                        ctc = job.costToCompany,
                        lastDateToApply = job.lastDateToApply,
                        descriptionAboutRole = job.descriptionAboutRole,
                        skillsRequired = job.skillsRequired,
                        noOfOpenings = job.noOfOpening,
                        perks = job.perks,
                        noOfStudentsApplied = job.noOfStudentsApplied,
                        responsibilities = job.responsibilities,
                        roleCategory = job.roleCategory,
                        minimumQualification = job.minimumQualification,
                        companyLinks = job.companyLinks,
                        postedDate = job.postedDate,
                        type = job.type
                    )
                }

                Log.d("rk",data.toString())
                finish()
            }
        })

        binding.imageOfbookmark.setOnClickListener {

            if(jobIdArr.contains(job._id))
            {
                jobMVVM.unSaveJob(this,"Bearer $token", UnSavedJobIM(job._id))
            }
            else
            {
                jobMVVM.saveJobForLater(this,"Bearer $token", SavedJobIM(job._id))
            }
        }

        jobMVVM.observerForSaveJobForLater().observe(this, Observer {
                result->
            Log.d("rk","save job for later api")
            if(result.status == "success")
            {
                Glide.with(this)
                    .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/bookmark_closed.png")
                    .placeholder(R.drawable.career_connect_white_bg)
                    .into(binding.imageOfbookmark)
                binding.aboutRole.text=job.descriptionAboutRole
                job._id?.let { jobIdArr.add(it) }
                var editor = sharedPreference1.edit()
                editor.putString(Constants.ONLY_JOBID_ARR,jobIdArr.toString())
                editor.commit()
            }
        })


        jobMVVM.observerForUnSaveJob().observe(this, Observer {
                result->
            Log.d("rk","un save job for later api")
            Log.d("rk",result.toString())
            if(result.status == "success")
            {
                Glide.with(this)
                    .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/bookmark_open.png")
                    .placeholder(R.drawable.career_connect_white_bg)
                    .into(binding.imageOfbookmark)
                binding.aboutRole.text=job.descriptionAboutRole
                jobIdArr.remove(job._id)
                var editor = sharedPreference1.edit()
                editor.putString(Constants.ONLY_JOBID_ARR,jobIdArr.toString())
                editor.commit()
            }
        })


        if(jobIdArr.contains(job._id))
        {
            Glide.with(this)
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/bookmark_closed.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.imageOfbookmark)
        }
        else
        {
            Glide.with(this)
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/bookmark_open.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.imageOfbookmark)
        }
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

    fun errorFn(message:String) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
        binding.applyBtnProgressBar.visibility = View.GONE
    }

    private fun getModel(): AppliedJobsModelSQLite {
        // Map the fields from job to AppliedJobsModelSQLite
        return AppliedJobsModelSQLite(
            _id = job._id,
            aboutCompany = job.aboutCompany,
            companyLinks = job.companyLinks?.joinToString("||") { it?.link ?: "" }, // Convert list to ||-separated string
            costToCompany = job.costToCompany,
            descriptionAboutRole = job.descriptionAboutRole,
            durationOfInternship = job.durationOfInternship,
            lastDateToApply = job.lastDateToApply?.toString(),
            location = job.location?.joinToString("||") ?: "",
            minimumQualification = job.minimumQualification?.joinToString("||") ?: "",
            nameOfCompany = job.nameOfCompany,
            nameOfRole = job.nameOfRole,
            noOfOpening = job.noOfOpening?.toString(),
            noOfStudentsApplied = job.noOfStudentsApplied?.toString(),
            perks = job.perks?.joinToString("||") ?: "",
            postedDate = job.postedDate?.toString(),
            responsibilities = job.responsibilities?.joinToString("||") ?: "",
            roleCategory = job.roleCategory,
            skillsRequired = job.skillsRequired?.joinToString("||") ?: "",
            startDate = job.startDate,
            typeOfJob = job.typeOfJob,
            type = "Applied" // Default type as "Applied"
        )
    }

}