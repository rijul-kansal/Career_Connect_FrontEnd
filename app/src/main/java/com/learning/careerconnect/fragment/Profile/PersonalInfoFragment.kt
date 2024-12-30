package com.learning.careerconnect.fragment.Profile

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.learning.careerconnect.Adapter.ProfileAdapter.LanguageKnownAdapter
import com.learning.careerconnect.Model.LoginOM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.FragmentPersonalInfoBinding
import java.text.SimpleDateFormat
import java.util.Date

class PersonalInfoFragment : Fragment() {
    lateinit var binding :FragmentPersonalInfoBinding
    lateinit var userData:LoginOM
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPersonalInfoBinding.inflate(inflater, container, false)
        val sharedPreference = requireActivity().getSharedPreferences(Constants.GET_ME_SP_PN, Context.MODE_PRIVATE)
        val gson = Gson()
        val fileData = sharedPreference.getString(Constants.GET_ME_SP, null)
        Log.d("rk",fileData.toString())
        val listType = object : TypeToken<LoginOM>() {}.type
        userData = gson.fromJson(fileData, listType)

        populateData()
        Log.d("rk",userData.toString())

        return binding.root
    }

    private fun populateData() {
        binding.name.text = userData.data!!.data!!.name
        binding.email.text = userData.data!!.data!!.email
        binding.mobileNo.text = userData.data!!.data!!.mobileNumber.toString()

        if(userData.data!!.data!!.image != null)
            binding.gender.text = userData.data!!.data!!.gender
        else
            binding.gender.text = "Add Gender"
        if(userData.data!!.data!!.dateOfBirth != null)
            binding.DOB.text = userData.data!!.data!!.dateOfBirth?.let { getDateTime(it) }
        else
            binding.DOB.text = "Add DOB"
        var location = "${userData.data!!.data!!.currentLocation?.city}," +
                "${userData.data!!.data!!.currentLocation?.state}," +
                "${userData.data!!.data!!.currentLocation?.country}"
        if(location != "null,null,null")
            binding.currentLocation.text = location
        else
            binding.currentLocation.text = "Add Location"

        var arr = userData.data!!.data!!.language as ArrayList<String>
        binding.language.layoutManager = LinearLayoutManager(requireActivity())
        var languageItemAdapter = LanguageKnownAdapter(arr)
        val heightInDp = 70
        val heightInPx = (heightInDp * requireContext().resources.displayMetrics.density).toInt()
        val layoutparams = LinearLayout.LayoutParams(MATCH_PARENT, arr.size * heightInPx)
        layoutparams.setMargins(12,12,12,12)
        binding.cardViewLanguage.layoutParams=layoutparams
        binding.language.adapter = languageItemAdapter

        Glide
            .with(requireActivity())
            .load(userData.data!!.data!!.image)
            .placeholder(R.drawable.career_connect_white_bg)
            .into(binding.profileImage)
    }

    private fun getDateTime(s: Long): String? {
        try {
            val sdf = SimpleDateFormat("MM/dd/yyyy")
            val netDate = Date(s )
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
}