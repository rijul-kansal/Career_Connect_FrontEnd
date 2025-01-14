package com.learning.careerconnect.fragment.Profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.learning.careerconnect.Adapter.ProfileAdapter.EducationDisplayAdapter
import com.learning.careerconnect.Model.LoginOM
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

        binding.recycleViewEducation.layoutManager = LinearLayoutManager(requireActivity())
        val itemAdapter = EducationDisplayAdapter(educationArr,requireContext())
        binding.recycleViewEducation.adapter = itemAdapter

    }
}