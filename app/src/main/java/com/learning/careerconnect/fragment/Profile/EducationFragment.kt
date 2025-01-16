package com.learning.careerconnect.fragment.Profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.learning.careerconnect.Adapter.ProfileAdapter.EducationDisplayAdapter
import com.learning.careerconnect.Adapter.ProfileAdapter.LanguageKnownAdapter
import com.learning.careerconnect.Model.LoginOM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.FragmentEducationBinding


class EducationFragment : Fragment() {
    lateinit var binding:FragmentEducationBinding
    lateinit var userData: LoginOM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEducationBinding.inflate(inflater, container, false)

        val sharedPreference = requireActivity().getSharedPreferences(Constants.GET_ME_SP_PN, Context.MODE_PRIVATE)
        val gson = Gson()
        val fileData = sharedPreference.getString(Constants.GET_ME_SP, null)
        val listType = object : TypeToken<LoginOM>() {}.type
        userData = gson.fromJson(fileData, listType)
        populateData()
        return binding.root
    }

    private fun populateData() {
        var educationArr = userData.data!!.data!!.education
        var skills = userData.data!!.data!!.skills as ArrayList<String>
        binding.recycleViewEducation.layoutManager = LinearLayoutManager(requireActivity())
        val itemAdapterEducation = EducationDisplayAdapter(educationArr,requireContext())
        binding.recycleViewEducation.adapter = itemAdapterEducation
        binding.recycleViewSkill.layoutManager = LinearLayoutManager(requireActivity())
        val itemAdapterSkills = LanguageKnownAdapter(skills)
        binding.recycleViewSkill.adapter = itemAdapterSkills
        if(userData.data!!.data!!.links!!.codeChef != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/codechef.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.codechef)
        }
        else
        {
            binding.codechef.visibility = View.GONE
        }
        if(userData.data!!.data!!.links!!.codeForces != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/codeforces.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.codeforces)
        }
        else
        {
            binding.codeforces.visibility = View.GONE
        }
        if(userData.data!!.data!!.links!!.github != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/github.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.github)
        }
        else
        {
            binding.github.visibility = View.GONE
        }
        if(userData.data!!.data!!.links!!.leetCode != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/leetcode.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.leetcode)
        }
        else
        {
            binding.leetcode.visibility = View.GONE
        }
        if(userData.data!!.data!!.links!!.website != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/website.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.website)
        }
        else
        {
            binding.website.visibility = View.GONE
        }
        if(userData.data!!.data!!.links!!.linkedin != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/linkedin.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.linkedin)
        }
        else
        {
            binding.linkedin.visibility = View.GONE
        }
    }
}