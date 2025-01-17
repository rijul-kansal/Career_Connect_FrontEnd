package com.learning.careerconnect.fragment.Profile

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.learning.careerconnect.Activity.BaseActivity
import com.learning.careerconnect.Adapter.ProfileAdapter.EducationDisplayAdapter
import com.learning.careerconnect.Adapter.ProfileAdapter.LanguageKnownAdapter
import com.learning.careerconnect.MVVM.ExtMVVM
import com.learning.careerconnect.MVVM.UserMVVM
import com.learning.careerconnect.Model.LoginOM
import com.learning.careerconnect.R
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.FragmentEducationBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream


class EducationFragment : Fragment() {
    lateinit var binding:FragmentEducationBinding
    lateinit var userData: LoginOM
    private var pdfFileUri: Uri? = null
    lateinit var userVM: UserMVVM
    lateinit var token:String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEducationBinding.inflate(inflater, container, false)
        userVM= ViewModelProvider(this)[UserMVVM::class.java]

        val sharedPreference1 =
            requireActivity().getSharedPreferences(Constants.TOKEN_SP_PN, Context.MODE_PRIVATE)
        token = sharedPreference1.getString(Constants.JWT_TOKEN_SP, "rk").toString()


        val sharedPreference = requireActivity().getSharedPreferences(Constants.GET_ME_SP_PN, Context.MODE_PRIVATE)
        val gson = Gson()
        val fileData = sharedPreference.getString(Constants.GET_ME_SP, null)
        val listType = object : TypeToken<LoginOM>() {}.type
        userData = gson.fromJson(fileData, listType)
        populateData()

        binding.resumeIV.setOnClickListener {
            launcher.launch("application/pdf")
        }
        return binding.root
    }
    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        pdfFileUri = uri
        val fileUri = uri?.let { DocumentFile.fromSingleUri(requireContext(), it)?.uri }
        BaseActivity().toast("File Name: $fileUri",requireActivity())

        val fileDir = requireContext().filesDir
        val file = File(fileDir, "image.pdf")

        val inputStream = uri?.let { requireActivity().contentResolver.openInputStream(it) }
        val outputStream = FileOutputStream(file)
        inputStream!!.copyTo(outputStream)
        val requestBody = file.asRequestBody("application/pdf".toMediaTypeOrNull())
        val part = MultipartBody.Part.createFormData("image", file.name, requestBody)
        val description = "Resume"
        val descriptionRequestBody = description.toRequestBody("text/plain".toMediaTypeOrNull())

        userVM.uploadToS3(requireContext(),"Bearer $token ",part,descriptionRequestBody, this)
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

        if(educationArr?.size ==0) binding.addEducationTV.visibility = View.VISIBLE
        if(skills.size ==0) binding.addSkillTV.visibility = View.VISIBLE

        var count =0
        if(userData.data!!.data!!.links?.codeChef != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/codechef.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.codechef)
        }
        else
        {
            binding.codechef.visibility = View.GONE
            count++
        }
        if(userData.data!!.data!!.links?.codeForces != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/codeforces.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.codeforces)
        }
        else
        {
            binding.codeforces.visibility = View.GONE
            count++
        }
        if(userData.data!!.data!!.links?.github != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/github.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.github)
        }
        else
        {
            binding.github.visibility = View.GONE
            count++
        }
        if(userData.data!!.data!!.links?.leetCode != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/leetcode.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.leetcode)
        }
        else
        {
            binding.leetcode.visibility = View.GONE
            count++
        }
        if(userData.data!!.data!!.links?.website != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/website.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.website)
        }
        else
        {
            binding.website.visibility = View.GONE
            count++
        }
        if(userData.data!!.data!!.links?.linkedin != null)
        {
            Glide.with(requireActivity())
                .load("https://career-connect-bkt.s3.ap-south-1.amazonaws.com/linkedin.png")
                .placeholder(R.drawable.career_connect_white_bg)
                .into(binding.linkedin)
        }
        else
        {
            binding.linkedin.visibility = View.GONE
            count++
        }

        if(count == 6 )
            binding.addLinksTV.visibility = View.VISIBLE

    }

    fun errorFn(message: String) {
        BaseActivity().toast(message, requireContext())
    }
}