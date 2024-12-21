package com.learning.careerconnect.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.learning.careerconnect.Activity.BaseActivity
import com.learning.careerconnect.Adapter.QuizTypeShownAdapter
import com.learning.careerconnect.Model.GetQuizScoreEarnedOM
import com.learning.careerconnect.Utils.Constants
import com.learning.careerconnect.databinding.FragmentQuizBinding


class QuizFragment : Fragment() {

     lateinit var binding: FragmentQuizBinding
     lateinit var quizTypeArr : ArrayList<String>
     lateinit var  quizScoreArr :ArrayList<GetQuizScoreEarnedOM.Data.Data>
     lateinit var itemAdapter: QuizTypeShownAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentQuizBinding.inflate(inflater, container, false)
        getLocalData()

        adapter(quizTypeArr)
        return binding.root
    }

    private fun getLocalData() {
        val sharedPreference = requireActivity().getSharedPreferences(Constants.Quiz_SP_PN, Context.MODE_PRIVATE)
        val quizType = sharedPreference.getString(Constants.TYPE_OF_QUIZ_User_CAN_GIVE,"")
        val quizScore = sharedPreference.getString(Constants.CERTIFICATE_EARNED,"")

        val gson = Gson()
        val listType = object : TypeToken<ArrayList<String>>() {}.type
        quizTypeArr = gson.fromJson(quizType, listType)

        val listType1 = object : TypeToken<GetQuizScoreEarnedOM>() {}.type
        val quizScoreData :GetQuizScoreEarnedOM = gson.fromJson(quizScore, listType1)
        quizScoreArr = quizScoreData.data!!.data as ArrayList<GetQuizScoreEarnedOM.Data.Data>
    }

    fun errorFn(message: String) {
        BaseActivity().toast(message, requireContext())
    }

    fun adapter(lis: ArrayList<String>) {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        itemAdapter = QuizTypeShownAdapter(lis,requireContext())
        binding.recyclerView.adapter = itemAdapter
        itemAdapter.setOnClickListener(object :
            QuizTypeShownAdapter.OnClickListener {
            override fun onClick(position: Int, model: String) {
                Log.d("rk",position.toString())
               BaseActivity().toast(model,requireContext())
            }
        })
    }
}