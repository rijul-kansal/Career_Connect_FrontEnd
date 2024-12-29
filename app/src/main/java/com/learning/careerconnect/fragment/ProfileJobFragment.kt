package com.learning.careerconnect.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.learning.careerconnect.Adapter.ViewPagerForProfileAdapter
import com.learning.careerconnect.R
import com.learning.careerconnect.databinding.FragmentAppliedJobBinding
import com.learning.careerconnect.databinding.FragmentProfileJobBinding
import com.learning.careerconnect.fragment.Profile.AchievemnntsFragment
import com.learning.careerconnect.fragment.Profile.EducationFragment
import com.learning.careerconnect.fragment.Profile.ExperienceFragment
import com.learning.careerconnect.fragment.Profile.PersonalInfoFragment


class ProfileJobFragment : Fragment() {
    private lateinit var binding: FragmentProfileJobBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileJobBinding.inflate(inflater, container, false)
        /**
         *Personal
         *     name
         *     image
         *     currentLocation
         *     Gender
         *     DOB
         *     Mobile No
         *     Email
         *     language
         *     Resume Link
         *     Git link
         *     portfolio link
         *     Coding Profile Link
         *Education
         *     education
         *     skills
         *Experience
         *     Summary
         *     experience
         *     project
         *Achievements
         *     achievements
         *     Certificate Earned
         * */

        val adapter = ViewPagerForProfileAdapter(childFragmentManager)
        adapter.addFragment(PersonalInfoFragment(), "Personal")
        adapter.addFragment(ExperienceFragment(), "Experience")
        adapter.addFragment(EducationFragment(), "Education")
        adapter.addFragment(AchievemnntsFragment(), "Achievements")
        binding.viewPager.adapter = adapter
        binding.tabs.setupWithViewPager(binding.viewPager)
        return binding.root
    }
}