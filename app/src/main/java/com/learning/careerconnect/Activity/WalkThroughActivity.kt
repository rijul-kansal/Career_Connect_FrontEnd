package com.learning.careerconnect.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.learning.careerconnect.R
import com.learning.careerconnect.Adapter.WalkThroughAdapter
import com.learning.careerconnect.databinding.ActivityWalkThroughBinding

class WalkThroughActivity : AppCompatActivity() {
    private lateinit var dots: ArrayList<TextView>
    lateinit var viewPagerAdapter: WalkThroughAdapter
    lateinit var binding:ActivityWalkThroughBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityWalkThroughBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    try {
        Log.d("rk","call on create from walk through Screen")
        dots = ArrayList()
        binding.backBtn.setOnClickListener {
            if (getItem(0) > 0) {
                binding.slideViewPager.setCurrentItem(getItem(-1), true)
            }
        }
        binding.forwardBtn.setOnClickListener {
            if (getItem(0) < 3) {
                binding.slideViewPager.setCurrentItem(getItem(1), true)
            } else {
                val i = Intent(this@WalkThroughActivity, IntroActivity::class.java)
                startActivity(i)
                finish()
            }
        }
        binding.skipBtn.setOnClickListener {
            val i = Intent(this@WalkThroughActivity, IntroActivity::class.java)
            startActivity(i)
            finish()
        }
        binding.getStartedBtn.setOnClickListener {
            val i = Intent(this@WalkThroughActivity, IntroActivity::class.java)
            startActivity(i)
            finish()
        }
        viewPagerAdapter = WalkThroughAdapter(this)
        binding.slideViewPager.adapter = viewPagerAdapter
        setUpIndicator(0)
        binding.slideViewPager.addOnPageChangeListener(viewListener)
        } catch (e: Exception) {
            Log.d("rk", e.message.toString())
        }
    }
    private fun setUpIndicator(position: Int) {
        dots.clear()
        binding.indicatorLayout.removeAllViews()
        for (i in 0..3) {
            dots.add(TextView(this))
            dots[i].text = Html.fromHtml("&#8226;")
            dots[i].textSize = 35F
            dots[i].setTextColor(
                resources.getColor(
                    R.color.main_colour,
                    applicationContext.theme
                )
            )
            binding.indicatorLayout.addView(dots[i])
        }
        if (dots.isNotEmpty()) {
            dots[position].setTextColor(resources.getColor(R.color.black, applicationContext.theme))
        }
    }

    private val viewListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
        override fun onPageSelected(position: Int) {
            setUpIndicator(position)

            if (position > 0) {
                binding.backBtn.visibility = View.VISIBLE
            } else {
                binding.backBtn.visibility = View.INVISIBLE
            }

            if(position==3)
            {
                binding.forwardBtn.visibility=View.INVISIBLE
                binding.getStartedBtn.visibility=View.VISIBLE
                binding.backBtn.visibility=View.INVISIBLE
                binding.indicatorLayout.visibility=View.INVISIBLE
                val animation = AnimationUtils.loadAnimation(this@WalkThroughActivity,
                    R.anim.slider
                )
                binding.getStartedBtn.startAnimation(animation)
            }
            else
            {
                binding.backBtn.visibility=View.VISIBLE
                binding.forwardBtn.visibility=View.VISIBLE
                binding.indicatorLayout.visibility=View.VISIBLE
                binding.getStartedBtn.visibility=View.INVISIBLE
                binding.getStartedBtn.clearAnimation()
            }
        }
        override fun onPageScrollStateChanged(state: Int) {}
    }

    private fun getItem(i: Int): Int {
        return binding.slideViewPager.currentItem.plus(i)
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("rk","call on destroy from walk through Screen")
    }
}