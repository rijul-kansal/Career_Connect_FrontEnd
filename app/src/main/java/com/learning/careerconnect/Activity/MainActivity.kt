package com.learning.careerconnect.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.learning.careerconnect.Cashes.JobAppliedDB
import com.learning.careerconnect.MVVM.*
import com.learning.careerconnect.Model.GetAllSavedLaterJobsOM
import com.learning.careerconnect.Model.UpdateMeIM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.ActivityMainBinding
import com.learning.careerconnect.fragment.AppliedJobFragment
import com.learning.careerconnect.fragment.GeminiFragment
import com.learning.careerconnect.fragment.PostNewJobFragment
import com.learning.careerconnect.fragment.ProfileJobFragment
import com.learning.careerconnect.fragment.QuizFragment
import com.learning.careerconnect.fragment.SavedJobFragment
import com.learning.careerconnect.fragment.SearchJobFragment
import com.learning.careerconnect.fragment.SeeAllPostedJobFragment

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener  {
    lateinit var binding:ActivityMainBinding
    lateinit var userVM: UserMVVM
    lateinit var jobVM: JobMVVM
    lateinit var quizVM: QuizMVVM
    var doubleBackToExitPressedOnce = false
    lateinit var token:String
    lateinit var typeOfUser:String


    lateinit var allSavedJobsJobId:ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        allSavedJobsJobId = ArrayList()
        userVM = ViewModelProvider(this)[UserMVVM::class.java]
        jobVM = ViewModelProvider(this)[JobMVVM::class.java]
        quizVM = ViewModelProvider(this)[QuizMVVM::class.java]
        val sharedPreference =  getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        val sharedPreference1 = getSharedPreferences(Constants.GET_ME_SP_PN, Context.MODE_PRIVATE)
        typeOfUser = sharedPreference1.getString(Constants.TYPE_OF_USER,"rk").toString()
        token = sharedPreference.getString(Constants.JWT_TOKEN_SP,"rk").toString()
        if(intent.hasExtra(Constants.FCM_TOKEN))
        {
            val fcmToken =intent.getStringExtra(Constants.FCM_TOKEN).toString()
            userVM.updateMe(UpdateMeIM(fcmToken = fcmToken), this, this, "Bearer ${token}")
        }
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))
        binding.navView.setNavigationItemSelectedListener(this)

        if(typeOfUser == "User")
        {
            binding.navView.inflateMenu(R.menu.user_menu)
        }
        else
        {
            binding.navView.inflateMenu(R.menu.recruiter_menu)
        }
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open_nav, R.string.close_nav)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        toggle.syncState()


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchJobFragment()).commit()
            binding.navView.setCheckedItem(R.id.searchJob)
        }
        jobVM.getAllSavedJobOnlyJobId(this,"Boarer $token")
        jobVM.getAllAppliedJobs(this,"Bearer $token",AppliedJobFragment(),"0")
        jobVM.getAllSavedLaterJobsFull(this,this,"Bearer $token", SavedJobFragment(),"0")
        jobVM.observerForGetAllSavedJobOnlyJobId().observe(this , Observer {
            res->
            Log.d("rk" ,"SAVED JOB ONLY JOBS")

            for(i in 0..res.data!!.data!!.size-1)
            {
                allSavedJobsJobId.add((res.data!!.data?.get(i)!!.jobId).toString())
            }

            val sharedPreference =  getSharedPreferences(Constants.ONLY_JOBID_SP, Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString(Constants.ONLY_JOBID_ARR,allSavedJobsJobId.toString())
            editor.commit()
        })
        jobVM.observerForGetAllAppliedJobs().observe(this, Observer { result ->
            Log.d("rk" ,"Applied Job API")
            if (result.status == "success") {
                val dbHandler = JobAppliedDB(this)
                dbHandler.deleteAll()
                for (item in result.data.data) {
                    val jobApplied = item.jobAppliedId
                    val companyLinks = jobApplied?.companyLinks?.joinToString("||") { it?.link ?: "" }
                    val locations = jobApplied?.location?.joinToString("||") ?: ""
                    val minimumQualifications = jobApplied?.minimumQualification?.joinToString("||") ?: ""
                    val perks = jobApplied?.perks?.joinToString("||") ?: ""
                    val responsibilities = jobApplied?.responsibilities?.joinToString("||") ?: ""
                    val skillsRequired = jobApplied?.skillsRequired?.joinToString("||") ?: ""

                    // Add data to the database
                    dbHandler.add(
                        _id = jobApplied?._id,
                        nameOfCompany = jobApplied?.nameOfCompany,
                        aboutCompany = jobApplied?.aboutCompany,
                        nameOfRole = jobApplied?.nameOfRole,
                        typeOfJob = jobApplied?.typeOfJob,
                        location = locations,
                        startDate = jobApplied?.startDate,
                        durationOfInternship = jobApplied?.durationOfInternship,
                        ctc = jobApplied?.costToCompany,
                        lastDateToApply = jobApplied?.lastDateToApply?.toString(),
                        descriptionAboutRole = jobApplied?.descriptionAboutRole,
                        skillsRequired = skillsRequired,
                        noOfOpenings = jobApplied?.noOfOpening?.toString(),
                        perks = perks,
                        noOfStudentsApplied = jobApplied?.noOfStudentsApplied?.toString(),
                        responsibilities = responsibilities,
                        roleCategory = jobApplied?.roleCategory,
                        minimumQualification = minimumQualifications,
                        companyLinks = companyLinks,
                        postedDate = jobApplied?.postedDate?.toString(),
                        type = item.type
                    )
                }
            }
        })
        userVM.observerForUpdateMe().observe(this, Observer {
            Log.d("rk","Update User API")
        })
        jobVM.observerForGetAllSavedLaterJobsFull().observe(this , Observer {
                res->
            Log.d("rk" ,"SAVED Later JOBS fully")

            var arr = ArrayList<GetAllSavedLaterJobsOM.Data.Data.JobId>()
            for(i in res.data?.data!!)
            {
                if (i != null) {
                    i.jobId?.let { arr.add(it) }
                }
            }
            val gson = Gson()
            val jsonString = gson.toJson(arr)
            val sharedPreference = getSharedPreferences(Constants.FULL_JOBID_SP, Context.MODE_PRIVATE)
            val editor = sharedPreference.edit()
            editor.putString(Constants.FULL_JOBID_ARR, jsonString)
            editor.commit()
        })
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.seeAllPostedJob -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SeeAllPostedJobFragment()).commit()
                supportActionBar?.setTitle("See All Posted Job")
            }
            R.id.postNewJob -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, PostNewJobFragment()).commit()
                supportActionBar?.setTitle("Post New Job")
            }
            R.id.searchJob -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SearchJobFragment()).commit()
                supportActionBar?.setTitle("Search Job")
            }
            R.id.appliedJobs -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AppliedJobFragment()).commit()
                supportActionBar?.setTitle("See All Applied Job")
            }
            R.id.profile->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, ProfileJobFragment()).commit()
                supportActionBar?.setTitle("Profile")
            }
            R.id.logout->{
                val sharedPreference =  getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
                var editor = sharedPreference.edit()
                editor.remove(Constants.JWT_TOKEN_SP)
                editor.remove(Constants.REFRESH_TOKEN_SP)
                editor.remove(Constants.TIME_LEFT)
                editor.commit()
                val sharedPreference1 = getSharedPreferences(Constants.GET_ME_SP_PN, Context.MODE_PRIVATE)
                val editor1 = sharedPreference1.edit()
                editor1.remove(Constants.GET_ME_SP)
                editor1.remove(Constants.TYPE_OF_USER)
                editor1.commit()
                startActivity(Intent(this,IntroActivity::class.java))
                finish()
            }
            R.id.savedJobs->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SavedJobFragment()).commit()
                supportActionBar?.setTitle("See All Saved Job")
            }
            R.id.quiz->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, QuizFragment()).commit()
                supportActionBar?.setTitle("Quiz Time")
            }
            R.id.geminiModel->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, GeminiFragment()).commit()
                supportActionBar?.setTitle("Ask Gemini")
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else if (isOnlyOneActivityInStack()) {
            if (doubleBackToExitPressedOnce) {
                showBackBtnDialog("would you like to exit app",this)
                return
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchJobFragment()).commit()
            supportActionBar?.setTitle("SearchJob")
            this.doubleBackToExitPressedOnce = true
            Handler(Looper.getMainLooper()).postDelayed(Runnable {
                doubleBackToExitPressedOnce = false
            }, 2000)

        } else {

            Log.d("rk", "More than one activity in the stack")
            super.onBackPressed()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.side_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.chat) {
            startActivity(Intent(this@MainActivity,ChatActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun errorFn(message:String) {
        Toast.makeText(this,message, Toast.LENGTH_LONG).show()
    }
}