package com.learning.careerconnect.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.learning.careerconnect.Activity.BaseActivity
import com.learning.careerconnect.Model.GetAllSavedLaterJobsOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.FragmentSavedJobBinding

class SavedJobFragment : Fragment() {
    lateinit var binding:FragmentSavedJobBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSavedJobBinding.inflate(inflater, container, false)

        val sharedPreference = requireActivity().getSharedPreferences(Constants.FULL_JOBID_SP, Context.MODE_PRIVATE)
        val gson = Gson()
        val fileData = sharedPreference.getString(Constants.FULL_JOBID_ARR, null)
        val jobsArr = gson.fromJson(fileData, GetAllSavedLaterJobsOM::class.java)
        Log.d("rk", "Deserialized data: $jobsArr")
        return binding.root
    }

    fun errorFn(message: String) {
        BaseActivity().toast(message, requireContext())
    }
}