package com.learning.careerconnect.Activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.learning.careerconnect.R
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
    var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(resources.getColor(R.color.white))


        binding.navView.setNavigationItemSelectedListener(this)
        binding.navView.inflateMenu(R.menu.recruiter_menu)
        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.open_nav, R.string.close_nav)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.drawerArrowDrawable.color = resources.getColor(R.color.white)
        toggle.syncState()


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchJobFragment()).commit()
            binding.navView.setCheckedItem(R.id.searchJob)
        }
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
//                showBackBtnDialog()
                return
            }
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.fragment_container, CropPrediction()).commit()
//            supportActionBar?.setTitle("Crop Prediction")
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
}