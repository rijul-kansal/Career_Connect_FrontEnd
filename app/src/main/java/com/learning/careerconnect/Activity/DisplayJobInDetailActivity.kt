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
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.learning.careerconnect.Adapter.JobResponsibilitiesAdapter
import com.learning.careerconnect.Cashes.JobAppliedDB
import com.learning.careerconnect.MVVM.JobMVVM
import com.learning.careerconnect.Model.AppliedJobsModelSQLite
import com.learning.careerconnect.Model.ApplyJobIM
import com.learning.careerconnect.Model.GetAllAppliedJobsOM
import com.learning.careerconnect.Model.GetAllSavedLaterJobsOM
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
    var typeOfFragment:String=""


    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityDisplayJobInDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // initializing something
        jobMVVM = ViewModelProvider(this)[JobMVVM::class.java]

        // shared preference for token
        val sharedPreference =  getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        token = sharedPreference.getString(Constants.JWT_TOKEN_SP,"rk").toString()

        // getting intent fragment info
        // from which fragment this activity is called
        if(intent.hasExtra(Constants.TYPE_OF_FRAGMENT))
            typeOfFragment = intent.getStringExtra(Constants.TYPE_OF_FRAGMENT).toString()

        // if fragment is from Search Fragment then load data
        if(typeOfFragment !="SavedJobFragment" && typeOfFragment !="AppliedJobFragment" && intent.hasExtra(Constants.JOB_DATA))
            job = intent.getSerializableExtra(Constants.JOB_DATA) as SearchAllJobsOM.Data.Data
        // if fragment is from Applied Job  Fragment then load data
        else if(typeOfFragment  == "AppliedJobFragment")
        {
            // apply btn text will be applied only
            binding.LL10Btn.text = "Applied"
            // no need of bookmark after apply
            binding.imageOfbookmark.visibility = View.GONE
            // getting data from intent
            var model  = intent.getSerializableExtra(Constants.JOB_DATA1) as GetAllAppliedJobsOM.Data.Data

            // converting into SSearchAllJobsOM.Data.Data
            var companyLink :ArrayList<SearchAllJobsOM.Data.Data.CompanyLink> = ArrayList()
            companyLink.add(SearchAllJobsOM.Data.Data.CompanyLink(model.jobAppliedId!!.companyLinks?.get(0)?._id,model.jobAppliedId!!.companyLinks?.get(0)?.link,model.jobAppliedId!!.companyLinks?.get(0)?.name))
            companyLink.add(SearchAllJobsOM.Data.Data.CompanyLink(model.jobAppliedId!!.companyLinks?.get(1)?._id,model.jobAppliedId!!.companyLinks?.get(1)?.link,model.jobAppliedId!!.companyLinks?.get(1)?.name))
            job = SearchAllJobsOM.Data.Data(model.jobAppliedId!!.__v,model.jobAppliedId!!._id,model.jobAppliedId!!.aboutCompany,companyLink,
                model.jobAppliedId!!.costToCompany,model.jobAppliedId!!.descriptionAboutRole,model.jobAppliedId!!.durationOfInternship,model.jobAppliedId!!.lastDateToApply,model.jobAppliedId!!.location,
                        model.jobAppliedId!!.minimumQualification,model.jobAppliedId!!.nameOfCompany,model.jobAppliedId!!.nameOfRole,model.jobAppliedId!!.noOfOpening,
                model.jobAppliedId!!.noOfStudentsApplied,model.jobAppliedId!!.perks,model.jobAppliedId!!.postedDate,model.jobAppliedId!!.responsibilities,
                model.jobAppliedId!!.roleCategory,model.jobAppliedId!!.skillsRequired,model.jobAppliedId!!.startDate,model.jobAppliedId!!.stopResponses,model.jobAppliedId!!.typeOfJob)

        }
        // fragment is load from Saved Job Fragment
        else if(typeOfFragment  == "SavedJobFragment")
        {
            // getting data from intent
            var model  = intent.getSerializableExtra(Constants.JOB_DATA1) as GetAllSavedLaterJobsOM.Data.Data.JobId
            // converting into SSearchAllJobsOM.Data.Data
            var companyLink :ArrayList<SearchAllJobsOM.Data.Data.CompanyLink> = ArrayList()
            companyLink.add(SearchAllJobsOM.Data.Data.CompanyLink(model.companyLinks?.get(0)?._id,model.companyLinks?.get(0)?.link,model.companyLinks?.get(0)?.name))
            companyLink.add(SearchAllJobsOM.Data.Data.CompanyLink(model.companyLinks?.get(1)?._id,model.companyLinks?.get(1)?.link,model.companyLinks?.get(1)?.name))
            job = SearchAllJobsOM.Data.Data(model.__v,model._id,model.aboutCompany,companyLink,
                model.costToCompany,model.descriptionAboutRole,model.durationOfInternship,model.lastDateToApply,model.location,
                model.minimumQualification,model.nameOfCompany,model.nameOfRole,model.noOfOpening,
                model.noOfStudentsApplied,model.perks,model.postedDate,model.responsibilities,
                model.roleCategory,model.skillsRequired,model.startDate,model.stopResponses,model.typeOfJob)

        }
        // after getting data populate data
        populateDate()

        // requirement -- to show different bookmark status
        // used in when user come from Search Job Fragmnet
        val sharedPreference1 =  getSharedPreferences(Constants.ONLY_JOBID_SP, Context.MODE_PRIVATE)
        val gson = GsonBuilder().create()
        val fileData= sharedPreference1.getString(Constants.ONLY_JOBID_ARR,"0")
        val jobIdArr = gson.fromJson(fileData , Array<String>::class.java).toMutableList()
        // change bookmark
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
        // apply btn is only clickable when user not come from Applied job Fragment
        binding.LL10.setOnClickListener {
            if(typeOfFragment != "AppliedJobFragment")
            {
                binding.applyBtnProgressBar.visibility = View.VISIBLE
                jobMVVM.applyForJob(this,"Bearer $token", ApplyJobIM(job._id))
            }

        }

        // getting data if user applied and save  in local storage
        jobMVVM.observerForApplyForJob().observe(this, Observer {
            result->
            Log.d("rk","apply for job")
            binding.applyBtnProgressBar.visibility = View.GONE
            if(result.status == "success")
            {
                // reading data from sqlite
                val dbHandler = JobAppliedDB(this)
                val data= dbHandler.read()
                // if data size is greater than 10 then remove last element
                if(data.size >=10)
                    data.removeAt(data.size-1)
                val model = getModel()
                data.add(0,model)
                // first deleting all  element and then adding new element
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
                // remove job from saved preference
                removeJobFromSharedPreference(job)
                finish()
            }
        })
        // if job  is unsaved then save else save fro bookmark
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

        // saving job observer
        jobMVVM.observerForSaveJobForLater().observe(this, Observer {
                result->
            Log.d("rk","save job for later api")
            if(result.status == "success")
            {
                // change bookmark image
                Glide.with(this)
                    .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/bookmark_closed.png")
                    .placeholder(R.drawable.career_connect_white_bg)
                    .into(binding.imageOfbookmark)


                binding.aboutRole.text=job.descriptionAboutRole
                job._id?.let { jobIdArr.add(it) }
                // saving data toh job id only
                var editor = sharedPreference1.edit()
                editor.putString(Constants.ONLY_JOBID_ARR,jobIdArr.toString())
                editor.apply()


                // getting save job  in local database
                val sharedPreferenceToGetData = getSharedPreferences(Constants.FULL_JOBID_SP, Context.MODE_PRIVATE)
                val fileData = sharedPreferenceToGetData.getString(Constants.FULL_JOBID_ARR, null)
                val listType = object : TypeToken<ArrayList<GetAllSavedLaterJobsOM.Data.Data.JobId>>() {}.type
                val jobsArr = gson.fromJson<ArrayList<GetAllSavedLaterJobsOM.Data.Data.JobId>>(fileData, listType)

                val jobSaveSharedPreference = getSharedPreferences(Constants.FULL_JOBID_SP, Context.MODE_PRIVATE)
                val e=jobSaveSharedPreference.edit()
                if(jobsArr.size>=10)
                {
                    jobsArr.removeAt(0)
                }
                val castJob = castSearchJobToAppliedJob(job)
                jobsArr.add(castJob)
                val jsonString = gson.toJson(jobsArr)
                e.putString(Constants.FULL_JOBID_ARR,jsonString)
                e.apply()
            }
        })
        // unsaving job from DB
        jobMVVM.observerForUnSaveJob().observe(this, Observer {
                result->
            Log.d("rk","un save job for later api")
            if(result.status == "success")
            {
                // change bookmark image
                Glide.with(this)
                    .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/bookmark_open.png")
                    .placeholder(R.drawable.career_connect_white_bg)
                    .into(binding.imageOfbookmark)
                binding.aboutRole.text=job.descriptionAboutRole
                // remoing from local DB
                jobIdArr.remove(job._id)
                var editor = sharedPreference1.edit()
                editor.putString(Constants.ONLY_JOBID_ARR,jobIdArr.toString())
                editor.commit()
                // removing from shared preference also
                removeJobFromSharedPreference(job)
            }
        })
    }

    private fun removeJobFromSharedPreference(job: SearchAllJobsOM. Data. Data) {
        val gson = Gson()
        val sharedPreferenceToGetData = getSharedPreferences(Constants.FULL_JOBID_SP, Context.MODE_PRIVATE)
        val fileData = sharedPreferenceToGetData.getString(Constants.FULL_JOBID_ARR, null)
        val listType = object : TypeToken<ArrayList<GetAllSavedLaterJobsOM.Data.Data.JobId>>() {}.type
        val jobsArr = gson.fromJson<ArrayList<GetAllSavedLaterJobsOM.Data.Data.JobId>>(fileData, listType)
        var index=0
        for(i in jobsArr)
        {
            if(i._id == job._id)
            {
                break
            }
            index++
        }
        jobsArr.removeAt(index)
        val e=sharedPreferenceToGetData.edit()
        val jsonString = gson.toJson(jobsArr)
        e.putString(Constants.FULL_JOBID_ARR,jsonString)
        e.apply()
    }

    private fun castSearchJobToAppliedJob(job:SearchAllJobsOM.Data.Data): GetAllSavedLaterJobsOM.Data.Data.JobId {
        val newJob  = GetAllSavedLaterJobsOM.Data.Data.JobId()
        val newjobCompanyLink = ArrayList<GetAllSavedLaterJobsOM.Data.Data.JobId.CompanyLink>()

        for(i in job.companyLinks!!)
        {
            if (i != null) {
                i._id?.let { GetAllSavedLaterJobsOM.Data.Data.JobId.CompanyLink(it, i.link, i.name) }
                    ?.let { newjobCompanyLink.add(it) }
            }
        }
        newJob.__v = job.__v
        newJob._id = job._id
        newJob.aboutCompany = job.aboutCompany
        newJob.companyLinks = newjobCompanyLink
        newJob.costToCompany = job.costToCompany
        newJob.descriptionAboutRole = job.descriptionAboutRole
        newJob.durationOfInternship = job.durationOfInternship
        newJob.lastDateToApply = job.lastDateToApply
        newJob.location = job.location
        newJob.minimumQualification = job.minimumQualification
        newJob.nameOfCompany = job.nameOfCompany
        newJob.nameOfRole = job.nameOfRole
        newJob.noOfOpening = job.noOfOpening
        newJob.noOfStudentsApplied = job.noOfStudentsApplied
        newJob.perks = job.perks
        newJob.postedDate = job.postedDate
        newJob.responsibilities = job.responsibilities
        newJob.roleCategory = job.roleCategory
        newJob.skillsRequired = job.skillsRequired
        newJob.startDate = job.startDate
        newJob.stopResponses = job.stopResponses
        newJob.typeOfJob = job.typeOfJob

        return newJob
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